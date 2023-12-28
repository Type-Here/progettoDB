import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TipoMerce extends ATI{
    private String codice;
    private String name;
    private double peso;
    private tipologiaMezzo tipologiaMezzo;
    private categorieEnum categoria;

    public TipoMerce(String codice) {
        this.codice = codice;
        this.name = null;
        this.peso = 0;

        this.categoria = ATI.categorieEnum.valueOf(codice.substring(2,4));
        this.tipologiaMezzo = ATI.tipologiaMezzo.valueOf(codice.substring(0,1));
    }

    public TipoMerce(String codice, String name, double peso) {
        this.codice = codice;
        this.name = name;
        this.peso = peso;

        this.categoria = ATI.categorieEnum.valueOf(codice.substring(2,4));
        this.tipologiaMezzo = ATI.tipologiaMezzo.valueOf(codice.substring(0,1));
    }

    public String getCodice() {
        return codice;
    }

    public String getName() {
        return name;
    }

    public double getPeso() {
        return peso;
    }

    public ATI.tipologiaMezzo getTipologiaMezzo() {
        return tipologiaMezzo;
    }

    public categorieEnum getCategoria() {
        return categoria;
    }


    /**
     * Read TipoMerce from file. Format ('T-AL-005', 'Pane', 5), (gets only code!)
     * @param file file to read data from
     * @return a list of TipoMerci
     * @throws FileNotFoundException if file not found
     */
    public static List<TipoMerce> getTipoMerceFromFile(File file) throws FileNotFoundException {
        List<TipoMerce> merci = new LinkedList<>();
        try( Scanner read = new Scanner(file) ){
            while(read.hasNext()){
                String line = read.nextLine();

                Matcher id = Pattern.compile("'([A-Z]{1}-[A-Z]{2}-[0-9]{3})'").matcher(line);
                if(id.find()) {
                    merci.add(new TipoMerce(id.group(1)));
                }
            }

        }
        return merci;
    }

    @Override
    public String toString() {
        return "TipoMerce{" +
                "codice='" + codice + '\'' +
                ", tipologiaMezzo=" + tipologiaMezzo +
                ", categoria=" + categoria +
                '}';
    }
}
