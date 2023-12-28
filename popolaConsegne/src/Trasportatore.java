import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Trasportatore {
    private String matricola;

    public Trasportatore(String matricola) {
        this.matricola = matricola;
    }

    public String getMatricola() {
        return matricola;
    }

    public static List<Trasportatore> getTrasportatoreFromFile(File file) throws FileNotFoundException {
        List<Trasportatore> trasportatori = new LinkedList<>();
        try( Scanner read = new Scanner(file) ){
            while(read.hasNext()){
                String line = read.nextLine();

                Matcher id = Pattern.compile("'([A-Z]{2}[0-9]{4})'").matcher(line);
                if(id.find()){
                    trasportatori.add(new Trasportatore(id.group(1)));
                }

            }

        }

        //trasportatori.forEach(e -> System.out.println(e.getMatricola()));

        return trasportatori;
    }

}
