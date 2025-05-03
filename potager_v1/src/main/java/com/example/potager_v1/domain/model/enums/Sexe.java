// Sexe.java
package com.example.potager_v1.domain.model.enums;

public enum Sexe {
    MALE("M"),
    FEMELLE("F");

    private final String code;

    Sexe(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Sexe fromCode(String code) {
        for (Sexe sexe : Sexe.values()) {
            if (sexe.getCode().equals(code)) {
                return sexe;
            }
        }
        throw new IllegalArgumentException("Code de sexe inconnu: " + code);
    }
}