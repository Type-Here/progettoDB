public class Consegna extends ATI {
    private String data;
    private TipoMerce merce;
    private Mezzo mezzo;
    private Trasportatore operatore;
    private CentroDistribuzione centro;

    public Consegna(String data, CentroDistribuzione centro, TipoMerce merce, Mezzo mezzo, Trasportatore operatore ) {
        this.data = data;
        this.merce = merce;
        this.mezzo = mezzo;
        this.operatore = operatore;
        this.centro = centro;
    }

    public String getData() {
        return data;
    }

    public TipoMerce getMerce() {
        return merce;
    }

    public Mezzo getMezzo() {
        return mezzo;
    }

    public Trasportatore getOperatore() {
        return operatore;
    }

    public CentroDistribuzione getCentro() {
        return centro;
    }
}
