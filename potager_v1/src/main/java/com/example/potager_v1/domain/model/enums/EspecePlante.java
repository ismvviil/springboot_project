// EspecePlante.java
package com.example.potager_v1.domain.model.enums;

public enum EspecePlante {
    TOMATE("Solanum lycopersicum"),
    POMME_DE_TERRE("Tuberosum"),
    FRAISE("Fragoria vesca"),
    FRAISE_PRESTA("Fragoria presta"),
    CAROTTE("Daucus carota"),
    SALADE("Lactuca sativa"),
    AUBERGINE("Solanum melongena"),
    COURGETTE("Cucurbita pepo");

    private final String nomScientifique;

    EspecePlante(String nomScientifique) {
        this.nomScientifique = nomScientifique;
    }

    public String getNomScientifique() {
        return nomScientifique;
    }

    public static EspecePlante fromNomScientifique(String nom) {
        for (EspecePlante espece : EspecePlante.values()) {
            if (espece.getNomScientifique().equals(nom)) {
                return espece;
            }
        }
        throw new IllegalArgumentException("Espèce de plante inconnue: " + nom);
    }
}