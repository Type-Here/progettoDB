import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

public class Main {
    private static List<Consegna> consegne;
    private static List<Presenza> presenze;
    private static List<Mezzo> mezzi;
    private static List<TipoMerce> merci;
    private static List<Trasportatore> trasportatori;
    private static List<CentroDistribuzione> centri;
    private static Random random = new Random();

    private static File savefile = new File("saveConsegne.txt");
    public static void main(String[] args) {
        Path commonPath = Path.of("popolaConsegne/files");
        File presFile = commonPath.resolve("presenze.txt").toFile();
        File mezziFile = commonPath.resolve("mezzi.txt").toFile();
        File categorieFile = commonPath.resolve("categorie.txt").toFile();
        File merciFile = commonPath.resolve("merci.txt").toFile();
        File centriFile = commonPath.resolve("centri.txt").toFile();
        File trasportatoriFile = commonPath.resolve("trasportatori.txt").toFile();
        consegne = new ArrayList<>();

        try {
            trasportatori = Trasportatore.getTrasportatoreFromFile(trasportatoriFile);
            presenze = Presenza.getAttendenceFromFile(presFile, trasportatori); //Carica Presenze solo di Trasportatori!
            mezzi = Mezzo.getMezzoFromFile(mezziFile, categorieFile);
            merci = TipoMerce.getTipoMerceFromFile(merciFile);
            centri = CentroDistribuzione.getCentroDistribuzioneFromFile(centriFile);

        } catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }

        Presenza.orderAttendenceBy(presenze, Comparator.comparing(Presenza::getData));

        List<Trasportatore> workersSpecificDay = new LinkedList<>();

        for(int j = 0; j < presenze.size(); j++) {
            String day = presenze.get(j).getData();
            do {
                workersSpecificDay.add(presenze.get(j++).getTrasportatore());
            } while (presenze.get(j).getData().equals(day));

            List<Mezzo> mezziDisponibili = new ArrayList<>(mezzi);
            List<TipoMerce> merciDisponibili = new ArrayList<>(merci);

            for(Trasportatore t : workersSpecificDay) {

                TipoMerce merceConsegnata = (TipoMerce) randomListRemover(merciDisponibili);
                System.out.println("Merce: " + merceConsegnata);
                Mezzo mezzoUsato = (Mezzo) randomListRemover(mezziDisponibili, e -> {
                    Mezzo a = (Mezzo) e;
                    return (a.getTipologiaMezzo().equals(merceConsegnata.getTipologiaMezzo()) &&
                            a.getCategorie().stream().anyMatch(cat -> cat.equals(merceConsegnata.getCategoria())) );
                });
                CentroDistribuzione centro = centri.get(random.nextInt(0, centri.size() - 1));

                if(merceConsegnata == null || mezzoUsato == null || centro == null || day == null || t == null){
                    throw new RuntimeException("Qualche elemento era null");
                }

                consegne.add(new Consegna(day, centro, merceConsegnata, mezzoUsato, t));
            }

            workersSpecificDay.clear();
        }

        saveData(consegne, savefile);
    }

    public static Object randomListRemover(List<?> list){
        int selected = random.nextInt(0,list.size() - 1);
        Object o = list.get(selected);
        list.remove(selected);
        return o;
    }

    public static Object randomListRemover(List<?> list, Predicate<Object> predicate){

        List<?> filtered = list.stream().filter(predicate).toList();
        Object o;
        if(filtered.size() == 1){
            o = filtered.get(0);
        } else if (filtered.isEmpty()) {

            list.forEach(System.err::println);
            throw new RuntimeException("Impossibile Continuare Mezzo Non Trovato");

        } else {
            o = filtered.get(random.nextInt(0, filtered.size() - 1));
        }
        list.remove(o);
        return o;
    }

    public static void saveData(List<Consegna> list, File file){
        int res = JOptionPane.YES_OPTION;
        if(file.exists()){
            res = JOptionPane.showConfirmDialog(null, "File Already Exists, overwrite?");
        }
        if(!file.exists() || res == JOptionPane.YES_OPTION){
            try(PrintWriter save = new PrintWriter(file) ){
                list.forEach( e -> {
                    save.print("('" + e.getData() + "',");
                    save.print("'" + e.getMerce().getCodice() + "',");
                    save.print("'" + e.getCentro().getCitta() + "',");
                    save.print("'" + e.getCentro().getZona() + "',");
                    save.print("'" + e.getMezzo().getTarga() + "',");
                    save.println("'" + e.getOperatore().getMatricola() + "'),");
                });
            } catch (FileNotFoundException e){
                throw new RuntimeException(e);
            }

        }
    }
}