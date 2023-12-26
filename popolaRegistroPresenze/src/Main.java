import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static List<String> workers;

    public static void main(String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        workers = new ArrayList<>();

        File IDs = Path.of("popolaRegistroPresenze").resolve("dipendenti.txt").toFile();
        readIDFromFile(IDs);
        //workers.forEach(System.out::println);
        System.out.println("Size = " + workers.size());

        List<String> toSave = generateRandomWorkingDays(workers);
        saveToFile(toSave);

    }

    /**
     * Read data from File Where each Worker ID (Matricola) is in specified regex format
     * @param file to Read From
     */
    public static void readIDFromFile(File file){
        try (Scanner read = new Scanner(file)){
            while(read.hasNext()){
                String linea = read.nextLine().trim();
                Matcher matcher = Pattern.compile("'[A-Z]{2}[0-9]{4}'").matcher(linea);
                if(matcher.find()){
                    workers.add(matcher.group());
                }

            }

        } catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param workers list of workers ID in format < 'XX0001' >
     * @return list With Working Days such as ('XX001', '2020-01-01') in string format
     */
    public static List<String> generateRandomWorkingDays(List<String> workers){
        long start = LocalDate.of(2021, 1, 1).toEpochDay();
        long stop = LocalDate.now().toEpochDay();

        Random random = new Random();
        List<String> listPlusDays = new ArrayList<>();

        for(String x : workers){
            List<LocalDate> dates = new ArrayList<>();
            int daysOfWork = random.nextInt(460, 510);

            int i = 0;
            while (i < daysOfWork) {
                LocalDate randomDate = LocalDate.ofEpochDay(random.nextLong(start,stop));

                /* Reduce Probability of WeekEnd Work of 50% but not totally removes it*/
                if(random.nextInt(0,1) == 1) {
                    DayOfWeek weekOfRandomDay = randomDate.getDayOfWeek();
                    if (weekOfRandomDay == DayOfWeek.SATURDAY) randomDate = randomDate.plusDays(2);
                    else if (weekOfRandomDay == DayOfWeek.SUNDAY) randomDate = randomDate.plusDays(2);
                }

                /* Check If Not already picked:
                 * Necessary since no multiple instances per Worker ID are permitted with same Date! */
                if(dates.stream().noneMatch(randomDate::equals)){
                    dates.add(randomDate);

                    /* Add to List with MySQL Format */
                    listPlusDays.add( "(" + x + ",'" + randomDate + "')" );
                    i++;
                }

            }
        }
        return listPlusDays;
    }

    /**
     * Print inFile A List of Strings
     * @param toBeSaved List<String> to be saved
     */
    public static void saveToFile(List<String> toBeSaved){
        File save = Path.of("saveData.txt").toFile();
        int res = -1;
        if(save.exists()){
            res = JOptionPane.showConfirmDialog(null, "File Already Exists. Continue?");
        }
            if(res == JOptionPane.YES_OPTION || !save.exists()){
                try(PrintWriter saveFile = new PrintWriter(save)){
                    toBeSaved.forEach(saveFile::println);
                } catch (FileNotFoundException ex){
                    throw new RuntimeException(ex);
                }

            } else {
                System.err.println("User-defined choice not to overwrite");
            }
    }

}
