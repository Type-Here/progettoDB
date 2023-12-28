import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CentroDistribuzione extends ATI{
    private final String citta;
    private final String zona;
    private final int distanza;

    public CentroDistribuzione(String citta, String zona) {
        this.citta = citta;
        this.zona = zona;
        this.distanza = 0;
    }

    public CentroDistribuzione(String citta, String zona, int distanza) {
        this.citta = citta;
        this.zona = zona;
        this.distanza = distanza;
    }

    public String getCitta() {
        return citta;
    }

    public String getZona() {
        return zona;
    }

    public int getDistanza() {
        return distanza;
    }

    /**
     * Read CentroDistribuzione from file: Format ('Bari', 'Centrale', 240),  (gets ONLY Città and Zona!!)
     * @param file file to read data from
     * @return a list of CentroDistribuzione
     * @throws FileNotFoundException if file not found
     */
    public static List<CentroDistribuzione> getCentroDistribuzioneFromFile(File file) throws FileNotFoundException {
        List<CentroDistribuzione> centri = new LinkedList<>();
        try( Scanner read = new Scanner(file) ){
            while(read.hasNext()){
                String line = read.nextLine();

                Matcher id = Pattern.compile("'([A-Za-z]+)', '([A-Za-z-]+)'").matcher(line);

                if(id.find()) {
                    centri.add(new CentroDistribuzione(id.group(1), id.group(2)));
                }
            }

        }
        return centri;
    }

    /**
     * Overridden Method Equals
     * @param o Object to Compare
     * @return true if same object or same Città and Zona.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CentroDistribuzione that = (CentroDistribuzione) o;
        return this.citta.equals(that.citta) && this.zona.equals(that.zona);
    }

}
