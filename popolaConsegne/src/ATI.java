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

    public enum tipologiaMezzo {T,M,A}
}
