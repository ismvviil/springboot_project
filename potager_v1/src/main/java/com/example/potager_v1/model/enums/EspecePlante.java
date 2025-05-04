// EspecePlante.java
package com.example.potager_v1.model.enums;

public enum EspecePlante {
    TOMATE("Solanum lycopersicum", false),
    FRAISE_VESCA("Fragoria vesca", true),
    FRAISE_PRESTA("Fragoria presta", true),
    POMME_DE_TERRE("Tuberosum", false);

    private final String nomScientifique;
    private final boolean drageonnante;

    EspecePlante(String nomScientifique, boolean drageonnante) {
        this.nomScientifique = nomScientifique;
        this.drageonnante = drageonnante;
    }

    public String getNomScientifique() {
        return nomScientifique;
    }

    public boolean isDrageonnante() {
        return drageonnante;
    }

    public static EspecePlante fromNomScientifique(String nomScientifique) {
        for (EspecePlante espece : values()) {
            if (espece.getNomScientifique().equals(nomScientifique)) {
                return espece;
            }
        }
        return null;
    }
}