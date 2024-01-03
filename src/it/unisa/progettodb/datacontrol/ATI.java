package it.unisa.progettodb.datacontrol;

public abstract class ATI {
    public enum categorieEnum {AL("Alimentare"), TO("Tossico"),
            ST("Stabile"), FR("Fragile"), IN("Infiammabile");
        private final String nomeCategoria;
        categorieEnum(String nomeCategoria) {
            this.nomeCategoria = nomeCategoria;
        }

        public String getNomeCategoria() {
            return nomeCategoria;
        }

        public static categorieEnum getEnumByValue(String value){
            for(categorieEnum e : categorieEnum.values()){
                if(e.getNomeCategoria().equals(value)) return e;
            }

            throw new IllegalArgumentException("No Enum Available for this String");
        }
    }

    public enum tipologiaMezzo {
        T,M,A;
        public static String getTipologiaMezzoName(tipologiaMezzo tM){
            if(tM.equals(tipologiaMezzo.T)) return "Treno";
            if(tM.equals(tipologiaMezzo.M)) return "Motrice";
            if(tM.equals(tipologiaMezzo.A)) return "Autotreno";
            throw new IllegalArgumentException("Invalid Enum for tipologiaMezzo");
        }
    }
}
