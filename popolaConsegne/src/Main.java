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
    private static final Random random = new Random();

    private static final File savefile = Path.of("popolaConsegne").resolve("saveConsegne.txt").toFile();

    /**
     * This Code generate a PseudoRandom population of 'Consegne' for our DataBase Project.
     * After loading all necessary data, it sorts the Attendance Registry by date.
     * It gets a list of all operators working that day ('Trasportatori').
     * Then assigns to each worker of that day a good (TipoMerce), a distribution center (CentroDistribuzione) and a vehicle (Mezzo).
     * Each good has a code that refers to its vehicle transportability:
     * - Es T -> Train.
     * And its category:
     * - FR -> Fragile.
     * So the selection of the vehicle must be carefully evaluated even if randomized.
     * Moreover, our DB Project specified that each good should be sent to a specific center upmost once a day.
     * (There are checks for that too)
     */
    public static void main(String[] args) {
        Path commonPath = Path.of("popolaConsegne/files");
        File presFile = commonPath.resolve("presenze.txt").toFile();
        File mezziFile = commonPath.resolve("mezzi.txt").toFile();
        File categorieFile = commonPath.resolve("categorie.txt").toFile();
        File merciFile = commonPath.resolve("merci.txt").toFile();
        File centriFile = commonPath.resolve("centri.txt").toFile();
        File trasportatoriFile = commonPath.resolve("trasportatori.txt").toFile();
        consegne = new ArrayList<>();

        /*Load Data from Files and save it to each list
        * NOTE: Each method should be working with simple .txt with data for each row, but also in .sql formatted text*/
        try {
            trasportatori = Trasportatore.getTrasportatoreFromFile(trasportatoriFile);
            presenze = Presenza.getAttendenceFromFile(presFile, trasportatori); //Carica Presenze solo di Trasportatori!
            mezzi = Mezzo.getMezzoFromFile(mezziFile, categorieFile);
            merci = TipoMerce.getTipoMerceFromFile(merciFile);
            centri = CentroDistribuzione.getCentroDistribuzioneFromFile(centriFile);

        } catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }

        /* Sort Attendance Registry dy Date (String) */
        Presenza.orderAttendenceBy(presenze, Comparator.comparing(Presenza::getData));

        List<Trasportatore> workersSpecificDay = new LinkedList<>();

        for(int j = 0; j < presenze.size(); j++) {

            /*Gets all workers of a specific  day*/
            String day = presenze.get(j).getData();
            do {
                workersSpecificDay.add(presenze.get(j++).getTrasportatore());
            } while (j < presenze.size() && presenze.get(j).getData().equals(day));

            /*To be Reset for each day*/
            List<Mezzo> mezziDisponibili = new ArrayList<>(mezzi);
            List<Consegna> consegneGiornaliere = new ArrayList<>();

            /*System.out.println("MerciDis: " + merci.size() + " MezziDis: " + mezziDisponibili.size());*/ //For Debug
            /*System.out.println("Trasportatori: "+ workersSpecificDay.size() + " Day: " + day); */

            /* Create a delivery (Consegna) for each worker (Trasportatore) of that specific day*/
            for(Trasportatore t : workersSpecificDay) {

                /*Select Random but Appropriate Merce and Centro (no repetition of same element together)*/
                List<? super ATI> MerceAndCentroSelected = getMerceAndCentro(consegneGiornaliere);
                TipoMerce merceConsegnata = (TipoMerce) MerceAndCentroSelected.get(0);
                CentroDistribuzione centro = (CentroDistribuzione) MerceAndCentroSelected.get(1);

                /*Get an appropriate but random Vehicle for that specific category of good*/
                Mezzo mezzoUsato;
                try {
                    mezzoUsato = (Mezzo) randomListRemover(mezziDisponibili, e -> {
                        Mezzo a = (Mezzo) e;
                        return (a.getTipologiaMezzo().equals(merceConsegnata.getTipologiaMezzo()) &&
                                a.getCategorie().stream().anyMatch(cat -> cat.equals(merceConsegnata.getCategoria())));
                    });
                } catch (Exception e) {
                    System.err.println("\nMERCE che dà errore:" + merceConsegnata);
                    throw new RuntimeException(e);
                }

                /* Check To avoid eventual null values */
                if (merceConsegnata == null || mezzoUsato == null || centro == null || day == null || t == null) {
                    throw new RuntimeException("Qualche elemento era null");
                }
                /*Add Consegna to Generic and DaySpecific list (needed for checks above)*/
                Consegna c = new Consegna(day, centro, merceConsegnata, mezzoUsato, t);
                consegne.add(c);
                consegneGiornaliere.add(c);

            }
            /*Clears Workers List for that day*/
            workersSpecificDay.clear();
        }

        /*Save Data To File*/
        saveData(savefile);
    }

    /**
     * Check if a specific combination of TipoMerce and CentroDistribuzione is already in a delivery list
     * @param cons delivery list
     * @param m TipoMerce
     * @param c CentroDistribuzione
     * @return true if present, false otherwise
     */
    private static boolean isAlreadyInConsegna(List<Consegna> cons, TipoMerce m, CentroDistribuzione c){
       return cons.stream().anyMatch(x -> x.getMerce().equals(m) && x.getCentro().equals(c));
    }

    /**
     * Gets a Pseudo-Random list in which the first Element is a TipoMerce, the second a CentroDistribuzione.
     * It first checks if the combination is already in a delivery of cons list (&lt;Consegna&gt; -> delivery)
     * @param cons list of deliveries to check for collisions.
     * @return a List of ATIs where <br/> - Element in Pos 0 = TipoMerce chosen <br/>- Element in Pos 1 = CentroDistribuzione chosen
     */
    private static List<? super ATI> getMerceAndCentro(List<Consegna> cons){
        List<? super ATI> ret = new ArrayList<>();
        TipoMerce merceConsegnata;
        CentroDistribuzione centro;

        do {
            merceConsegnata = (TipoMerce) randomListSelector(merci);
            /*
            * This Code Below is to reduce the Probability of Getting a TipoMerce of a Train.
            * This to avoid Conflicts with our population where goods sendable via train were too frequent with train availability
            * In a random selected population
            * */

            /* Without random bool this control below reduce a Train sendable good chance to be selected by 33% (a bit too much) */
            //if(merceConsegnata.getCodice().startsWith("T")) merceConsegnata = (TipoMerce) randomListSelector(merci);

            /*This should reduce Train sendable goods by a factor of
             *1/2(randomBoolean) * 17/51 (our population) * 1/2 = 17/102 ~  16.67% less than random choice */
            if(merceConsegnata.getCodice().startsWith("T") && random.nextBoolean()){
                merceConsegnata = (TipoMerce) randomListSelector(merci);
            }
            centro = centri.get(random.nextInt(0, centri.size()));

        } while (isAlreadyInConsegna(cons, merceConsegnata, centro));

        ret.add(merceConsegnata);
        ret.add(centro);

        return ret;
    }


    /**
     * Select a Random Element from the List in param
     * @param list List to choose from
     * @return Object randomly selected
     */
    public static Object randomListSelector(List<?> list){
        return list.get( random.nextInt(0,list.size()) );
    }


    /**
     * Select a Random Element from the List in param and Removed It from the List
     * @param list to remove from
     * @return The removed Object
     */
    public static Object randomListRemover(List<?> list){
        int selected = random.nextInt(0,list.size());
        Object o = list.get(selected);
        list.remove(selected);
        return o;
    }

    /**
     * Overloaded Method Remove a random Element from a List that correspond to predicate
     * @param list list to choose from (object will be removed)
     * @param predicate to filter elements before random choice
     * @return The Removed Object
     * @throws Exception if list or filtered list is empty (no object correspond to predicate)
     */
    public static Object randomListRemover(List<?> list, Predicate<Object> predicate) throws Exception {

        List<?> filtered = list.stream().filter(predicate).toList();
        Object o;
        if(filtered.size() == 1){
            o = filtered.get(0);
        } else if (filtered.isEmpty()) {

            list.forEach(System.err::println);
            throw new Exception("Impossibile Continuare Oggetto Non Trovato");

        } else {
            o = filtered.get(random.nextInt(0, filtered.size()));
        }
        list.remove(o);
        return o;
    }

    /**
     * Saves List&lt;Consegna>&gt; to File in SQL Format. ConfirmationDialog if file already exists.
     * It needs same adjustment before being used in SQL (es. last comma needs to be changed in a semicolon;
     * @param file to Save
     */
    public static void saveData(File file){
        int res = JOptionPane.YES_OPTION;
        if(file.exists()){
            res = JOptionPane.showConfirmDialog(null, "File Already Exists, overwrite?");
        }
        if(!file.exists() || res == JOptionPane.YES_OPTION){
            try(PrintWriter save = new PrintWriter(file) ){
                consegne.forEach( e -> {
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