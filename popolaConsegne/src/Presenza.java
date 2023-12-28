import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Presenza {
    private final Trasportatore trasportatore;
    private final String data;

    public Presenza(Trasportatore trasportatore, String data) {
        this.trasportatore = trasportatore;
        this.data = data;
    }

    public Trasportatore getTrasportatore() {
        return trasportatore;
    }

    public String getData() {
        return data;
    }

    public static void orderAttendenceBy(List<Presenza> list, Comparator<Presenza> comparator){
        list.sort(comparator);
    }

    /**
     * Read Presenza from file: Format ('TR0002','2021-02-22')
     * @param file file to read data from
     * @return a list of attendances
     * @throws FileNotFoundException if file not found
     */
    public static List<Presenza> getAttendenceFromFile(File file, List<Trasportatore> trasportatori) throws FileNotFoundException {
        List<Presenza> presenze= new LinkedList<>();
        try( Scanner read = new Scanner(file) ){
            while(read.hasNext()){
                String line = read.nextLine();

                Matcher id = Pattern.compile("'([A-Z]{2}[0-9]{4})'").matcher(line);
                Matcher data = Pattern.compile("'([0-9]{4}-[0-9]{1,2}-[0-9]{1,2})'").matcher(line);
                if(id.find() && data.find()) {
                    Trasportatore trasp = trasportatori.stream().filter(t -> t.getMatricola().equals(id.group(1)))
                            .findFirst().orElse(null);
                    if (trasp == null) continue; //Evita di Caricare NON Trasportatori (Impiegati o Dirigenti)

                    presenze.add(new Presenza(trasp, data.group(1)));
                }
            }

        }
        return presenze;
    }

}
