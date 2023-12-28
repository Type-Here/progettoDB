import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static List<Mezzo> mezzi;
    private static final File savefile = Path.of("popolaCategorieMezzi").resolve("saveCategorie.txt").toFile();

    /**
     * Randomize categories for each vehicle and add them to its category list
     */
    public static void main(String[] args) {
        File mezziFile = Path.of("popolaConsegne/files").resolve("mezzi.txt").toFile();
        mezzi = Mezzo.getMezzoFromFile(mezziFile);

        String[] cat = {"Infiammabile", "Tossico", "Stabile", "Fragile", "Alimentare"};
        Random randomizer = new Random();

        for(Mezzo m : mezzi){
            List<String> categories = new ArrayList<>(List.of(cat));
            int randomNum = randomizer.nextInt(2,6);
            if(m.getTarga().startsWith("AT") && randomNum < 5) randomNum++; //Give a +1 to trains (since they're usually more capable)
            for(int i = 0; i < randomNum; i++){
                System.out.print(i + " ");
                m.addCategory(ATI.categorieEnum.getEnumByValue(categories.remove(randomizer.nextInt(0, categories.size()))));
            }

        }
        System.out.println("Lista:");
        mezzi.forEach(System.out::println);

        saveDataMezzi(savefile);
    }

    /**
     * Save to File for SQL format.
     * ('Targa', 'Categoria') for each category of a vehicle
     * @param file to write on
     */
    public static void saveDataMezzi(File file){
        try(PrintWriter saver = new PrintWriter(file)){
            for(Mezzo m : mezzi){
                for(ATI.categorieEnum e : m.getCategorie()) {
                    saver.println("('" + m.getTarga() + "','" + e.getNomeCategoria() + "'),");
                }
            }
        } catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }
    }


}