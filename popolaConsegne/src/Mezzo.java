import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mezzo extends ATI{
    private final String targa;
    private final List<categorieEnum> categorie;
    private final tipologiaMezzo tipologiaMezzo;

    public Mezzo(String targa, ATI.tipologiaMezzo tipologiaMezzo) {
        this.targa = targa;
        this.tipologiaMezzo = tipologiaMezzo;
        this.categorie = new ArrayList<>();
    }

    public Mezzo(String targa, List<categorieEnum> categorie, Mezzo.tipologiaMezzo tipologiaMezzo) {
        this.targa = targa;
        this.categorie = categorie;
        this.tipologiaMezzo = tipologiaMezzo;
    }

    public String getTarga() {
        return targa;
    }

    public List<categorieEnum> getCategorie() {
        return categorie;
    }

    public Mezzo.tipologiaMezzo getTipologiaMezzo() {
        return tipologiaMezzo;
    }

    public void addCategory(categorieEnum cat){
        this.categorie.add(cat);
    }

    /**
     *  Read Mezzo from a file Loads Only Targa!
     * @param fileMezzi file to read data of Mezzo from
     * @return List of Mezzo with lista categorie null
     */
    public static List<Mezzo> getMezzoFromFile(File fileMezzi){
        List<Mezzo> mezzi = new LinkedList<>();
        try( Scanner read = new Scanner(fileMezzi) ){
            while(read.hasNext()){
                String line = read.nextLine();

                Matcher targa = Pattern.compile("'([A-Z]{2}[0-9]{3,6}[A-Z]{0,2})'").matcher(line);
                Matcher tipologia = Pattern.compile("'(Treno|Autotreno|Motrice)'").matcher(line);

                if(targa.find() && tipologia.find()) {
                    mezzi.add(new Mezzo(targa.group(1), ATI.tipologiaMezzo.valueOf(tipologia.group(1).substring(0, 1))));
                }
            }

        } catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }
        return mezzi;
    }



    /**
     * Read Mezzo from a file and add Categories from another
     * @param fileMezzi file to read data of Mezzo from
     * @param fileCategorie file to read categoriesForEach Mezzo from
     * @return a list of Mezzo
     * @throws FileNotFoundException if file not found
     */
    public static List<Mezzo> getMezzoFromFile(File fileMezzi, File fileCategorie) throws FileNotFoundException {
        List<Mezzo> mezzi = new LinkedList<>();
        try( Scanner read = new Scanner(fileMezzi) ){
            while(read.hasNext()){
                String line = read.nextLine();

                Matcher targa = Pattern.compile("'([A-Z]{2}[0-9]{3,6}[A-Z]{0,2})'").matcher(line);
                Matcher tipologia = Pattern.compile("'(Treno|Autotreno|Motrice)'").matcher(line);

                if(targa.find() && tipologia.find()) {
                    mezzi.add(new Mezzo(targa.group(1), ATI.tipologiaMezzo.valueOf(tipologia.group(1).substring(0, 1))));
                }
            }

        }

        try( Scanner read = new Scanner(fileCategorie) ){
            while(read.hasNext()){
                String line = read.nextLine();

                Matcher targa = Pattern.compile("'([A-Z]{2}[0-9]{3,6}[A-Z]{0,2})'").matcher(line);
                Matcher categoria = Pattern.compile("'([A-Za-z]+)'").matcher(line);

                if(targa.find() && categoria.find()){
                    mezzi.stream().filter(m -> m.getTarga().equals(targa.group(1))).findFirst().
                            ifPresent(m -> m.addCategory(categorieEnum.valueOf(categoria.group(1).substring(0, 2).toUpperCase())));
                }
            }

        }
        return mezzi;
    }


    @Override
    public String toString() {
        return "Mezzo{" +
                "targa='" + targa + '\'' +
                ", categorie=" + categorie +
                ", tipologiaMezzo=" + tipologiaMezzo +
                '}';
    }

}
