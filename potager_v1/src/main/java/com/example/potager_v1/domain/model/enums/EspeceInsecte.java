// EspeceInsecte.java
package com.example.potager_v1.domain.model.enums;

public enum EspeceInsecte {
    COCCINELLE("Coccinellidae"),
    PUCERON("Aphidoidea"),
    ARAIGNEE("Araneae"),
    FOURMI("Formicidae"),
    HANNETON("Melolontha"),
    ABEILLE("Apis"),
    GUEPE("Vespidae");

    private final String famille;

    EspeceInsecte(String famille) {
        this.famille = famille;
    }

    public String getFamille() {
        return famille;
    }

    public static EspeceInsecte fromString(String name) {
        for (EspeceInsecte espece : EspeceInsecte.values()) {
            if (espece.name().equalsIgnoreCase(name)) {
                return espece;
            }
        }
        throw new IllegalArgumentException("Espèce d'insecte inconnue: " + name);
    }

    public boolean estPredateur() {
        return this == COCCINELLE || this == ARAIGNEE;
    }
}