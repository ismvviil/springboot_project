package com.example.potager_v1.model.insect;

import com.example.potager_v1.model.enums.EspeceInsecte;

import java.util.Random;

/**
 * Classe représentant un insecte dans le potager
 */
public class Insecte {
    private EspeceInsecte espece;
    private String sexe; // "M" ou "F"
    private int vie; // Indice de bonne santé actuel
    private int vieMax; // Indice de bonne santé maximal
    private int dureeVieMax; // Durée de vie maximale en pas de simulation
    private double probaMobilite; // Probabilité de se déplacer
    private double resistanceInsecticide; // Probabilité de survivre au traitement
    private int maxPortee; // Nombre maximal d'insectes lors d'une reproduction
    private int tempsEntreRepro; // Temps minimum entre deux reproductions
    private int age = 0; // Âge en pas de simulation
    private int tempsDepuisDerniereRepro = 0; // Temps depuis la dernière reproduction
    private int pasSansNourriture = 0; // Nombre de pas consécutifs sans nourriture

    private Random random = new Random();

    /**
     * Constructeur de l'insecte
     * @param espece Espèce de l'insecte
     * @param sexe Sexe de l'insecte ("M" ou "F")
     * @param vieMax Indice de bonne santé maximal
     * @param dureeVieMax Durée de vie maximale en pas de simulation
     * @param probaMobilite Probabilité de se déplacer
     * @param resistanceInsecticide Probabilité de survivre au traitement
     * @param maxPortee Nombre maximal d'insectes lors d'une reproduction
     * @param tempsEntreRepro Temps minimum entre deux reproductions
     */
    public Insecte(EspeceInsecte espece, String sexe, int vieMax, int dureeVieMax,
                   double probaMobilite, double resistanceInsecticide,
                   int maxPortee, int tempsEntreRepro) {
        this.espece = espece; // Correction : pas besoin de valueOf ici
        this.sexe = sexe;
        this.vieMax = vieMax;
        this.vie = vieMax; // Au début, l'insecte est en pleine santé
        this.dureeVieMax = dureeVieMax;
        this.probaMobilite = probaMobilite;
        this.resistanceInsecticide = resistanceInsecticide;
        this.maxPortee = maxPortee;
        this.tempsEntreRepro = tempsEntreRepro;
    }

    /**
     * Constructeur de l'insecte à partir d'un nom d'espèce
     * @param especeNom Nom de l'espèce de l'insecte
     * @param sexe Sexe de l'insecte ("M" ou "F")
     * @param vieMax Indice de bonne santé maximal
     * @param dureeVieMax Durée de vie maximale en pas de simulation
     * @param probaMobilite Probabilité de se déplacer
     * @param resistanceInsecticide Probabilité de survivre au traitement
     * @param maxPortee Nombre maximal d'insectes lors d'une reproduction
     * @param tempsEntreRepro Temps minimum entre deux reproductions
     */
    public Insecte(String especeNom, String sexe, int vieMax, int dureeVieMax,
                   double probaMobilite, double resistanceInsecticide,
                   int maxPortee, int tempsEntreRepro) {
        this.espece = EspeceInsecte.fromNom(especeNom);
        this.sexe = sexe;
        this.vieMax = vieMax;
        this.vie = vieMax; // Au début, l'insecte est en pleine santé
        this.dureeVieMax = dureeVieMax;
        this.probaMobilite = probaMobilite;
        this.resistanceInsecticide = resistanceInsecticide;
        this.maxPortee = maxPortee;
        this.tempsEntreRepro = tempsEntreRepro;
    }

    /**
     * Méthode appelée à chaque pas de simulation pour mettre à jour l'état de l'insecte
     */
    public void miseAJour() {
        // Incrémentation de l'âge
        age++;

        // Incrémentation du temps depuis la dernière reproduction
        tempsDepuisDerniereRepro++;
    }

    /**
     * Nourrit l'insecte, réinitialisant son compteur de pas sans nourriture
     */
    public void nourrir() {
        pasSansNourriture = 0;
    }

    /**
     * Indique que l'insecte n'a pas réussi à se nourrir pendant ce pas
     * @return true si l'insecte est mort de faim, false sinon
     */
    public boolean pasNourri() {
        pasSansNourriture++;

        // Si l'insecte ne s'est pas nourri pendant 5 pas consécutifs, il meurt
        if (pasSansNourriture >= 5) {
            vie = 0;
            return true;
        }

        // Sinon, il perd un point de santé
        vie = Math.max(0, vie - 1);
        return vie <= 0;
    }

    /**
     * Vérifie si l'insecte va tenter de se déplacer
     * @return true si l'insecte tente de se déplacer, false sinon
     */
    public boolean tenterDeplacement() {
        return random.nextDouble() < probaMobilite;
    }

    /**
     * Applique un traitement insecticide à l'insecte
     * @return true si l'insecte meurt, false s'il résiste
     */
    public boolean appliquerInsecticide() {
        // Si l'insecte résiste (en fonction de sa résistance aux insecticides)
        if (random.nextDouble() < resistanceInsecticide) {
            return false; // L'insecte survit
        }

        // Sinon, l'insecte meurt
        vie = 0;
        return true;
    }

    /**
     * Vérifie si l'insecte peut se reproduire
     * @return true si l'insecte peut se reproduire, false sinon
     */
    public boolean peutSeReproduire() {
        return sexe.equals("F") && tempsDepuisDerniereRepro >= tempsEntreRepro;
    }

    /**
     * Effectue la reproduction, réinitialisant le compteur de temps
     * @return le nombre d'insectes créés (entre 1 et maxPortee)
     */
    public int seReproduire() {
        if (!peutSeReproduire()) {
            return 0;
        }

        // Réinitialisation du compteur
        tempsDepuisDerniereRepro = 0;

        // Nombre aléatoire d'insectes créés entre 1 et maxPortee
        return 1 + random.nextInt(maxPortee);
    }

    /**
     * Crée un nouvel insecte du même type pour la reproduction
     * Le sexe est déterminé aléatoirement
     * @return Une nouvelle instance d'insecte
     */
    public Insecte creerDescendant() {
        // Détermination aléatoire du sexe (50% de chance pour chaque sexe)
        String nouveauSexe = random.nextBoolean() ? "M" : "F";

        // Création d'un nouvel insecte avec les mêmes caractéristiques
        return new Insecte(
                espece,
                nouveauSexe,
                vieMax,
                dureeVieMax,
                probaMobilite,
                resistanceInsecticide,
                maxPortee,
                tempsEntreRepro
        );
    }

    /**
     * Vérifie si l'insecte est mort (vie = 0 ou âge > dureeVieMax)
     * @return true si l'insecte est mort, false sinon
     */
    public boolean estMort() {
        return vie <= 0 || age >= dureeVieMax;
    }

    /**
     * Vérifie si l'insecte est nuisible pour les plantes
     * @return true si l'insecte est nuisible, false sinon
     */
    public boolean estNuisible() {
        return espece.isNuisible();
    }

    // Getters
    public EspeceInsecte getEspece() {
        return espece;
    }

    public String getSexe() {
        return sexe;
    }

    public int getVie() {
        return vie;
    }

    public int getVieMax() {
        return vieMax;
    }

    public int getAge() {
        return age;
    }

    public double getProbaMobilite() {
        return probaMobilite;
    }

    public double getResistanceInsecticide() {
        return resistanceInsecticide;
    }
}