package com.example.potager_v1.model.enums;

public enum EspecePlante {
    TOMATE("Solanum lycopersicum", false),
    FRAISE_VESCA("Fragoria vesca", true),
    FRAISE_PRESTA("Fragoria presta", true),
    POMME_DE_TERRE("Tuberosum", false);

    private final String nomScientifique;
    private final boolean drageonnante;

    /**
     * Constructeur
     * @param nomScientifique Nom scientifique de l'espèce
     * @param drageonnante Indique si l'espèce est drageonnante
     */
    EspecePlante(String nomScientifique, boolean drageonnante) {
        this.nomScientifique = nomScientifique;
        this.drageonnante = drageonnante;
    }

    /**
     * Récupère le nom scientifique de l'espèce
     * @return Le nom scientifique
     */
    public String getNomScientifique() {
        return nomScientifique;
    }

    /**
     * Indique si l'espèce est drageonnante
     * @return true si l'espèce est drageonnante, false sinon
     */
    public boolean isDrageonnante() {
        return drageonnante;
    }

    /**
     * Récupère une espèce à partir de son nom scientifique
     * @param nomScientifique Nom scientifique de l'espèce
     * @return L'espèce correspondante, ou null si aucune ne correspond
     */
    public static EspecePlante fromNomScientifique(String nomScientifique) {
        for (EspecePlante espece : values()) {
            if (espece.getNomScientifique().equals(nomScientifique)) {
                return espece;
            }
        }
        return null;
    }
}
