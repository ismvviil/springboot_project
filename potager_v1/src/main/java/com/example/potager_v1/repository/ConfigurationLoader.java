package com.example.potager_v1.repository;

import com.example.potager_v1.model.Parcelle;
import com.example.potager_v1.model.enums.EspeceInsecte;
import com.example.potager_v1.model.enums.EspecePlante;
import com.example.potager_v1.model.Insecte;
import com.example.potager_v1.model.Plante;
import com.example.potager_v1.model.PlanteDrageonnante;
import com.example.potager_v1.model.traitement.Dispositif;
import com.example.potager_v1.model.traitement.Programme;
import com.example.potager_v1.model.traitement.TypeTraitement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ConfigurationLoader {

    private final ParcelleRepository parcelleRepository;
    private final PlanteRepository planteRepository;
    private final InsecteRepository insecteRepository;
    private final DispositifRepository dispositifRepository;
    private final ProgrammeRepository programmeRepository;

    private String nomPotager;
    private int nbIterations;
    private int sizeX;
    private int sizeY;

    /**
     * Charge un fichier de configuration XML et initialise le potager en base de données
     * @param cheminFichier Chemin vers le fichier XML
     * @return Une map contenant les parcelles indexées par "x,y"
     */
    @Transactional
    public Map<String, Parcelle> chargerConfiguration(String cheminFichier) {
        Map<String, Parcelle> parcelles = new HashMap<>();

        try {
            // Initialisation du parseur XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(cheminFichier));
            document.getDocumentElement().normalize();

            // Récupération de l'élément racine (Potager)
            Element potager = document.getDocumentElement();
            this.nomPotager = potager.getAttribute("Nom");
            this.nbIterations = Integer.parseInt(potager.getAttribute("Nb_iterations"));
            this.sizeX = Integer.parseInt(potager.getAttribute("Size_x"));
            this.sizeY = Integer.parseInt(potager.getAttribute("Size_y"));

            System.out.println("Chargement du potager : " + nomPotager);
            System.out.println("Dimensions : " + sizeX + "x" + sizeY);
            System.out.println("Nombre d'itérations : " + nbIterations);

            // Récupération des parcelles
            NodeList parcelleNodes = document.getElementsByTagName("Parcelle");
            for (int i = 0; i < parcelleNodes.getLength(); i++) {
                Element parcelleElement = (Element) parcelleNodes.item(i);
                int posX = Integer.parseInt(parcelleElement.getAttribute("Pos_x"));
                int posY = Integer.parseInt(parcelleElement.getAttribute("Pos_y"));

                // Création de la parcelle
                Parcelle parcelle = new Parcelle(posX, posY);

                // Ajout des plantes, insectes et dispositifs
                ajouterPlantes(parcelleElement, parcelle);
                ajouterInsectes(parcelleElement, parcelle);
                ajouterDispositif(parcelleElement, parcelle);

                // Sauvegarde en base de données
                parcelleRepository.save(parcelle);

                // Ajout à la map avec clé "x,y"
                parcelles.put(posX + "," + posY, parcelle);
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println("Erreur lors du chargement du fichier XML : " + e.getMessage());
            e.printStackTrace();
        }

        return parcelles;
    }

    /**
     * Ajoute les plantes définies dans l'élément XML à la parcelle
     */
    private void ajouterPlantes(Element parcelleElement, Parcelle parcelle) {
        // Traitement des plantes normales
        NodeList planteNodes = parcelleElement.getElementsByTagName("Plante");
        for (int i = 0; i < planteNodes.getLength(); i++) {
            Element planteElement = (Element) planteNodes.item(i);

            // Récupération des attributs
            String especeStr = planteElement.getAttribute("Espece");
            EspecePlante espece = EspecePlante.fromNomScientifique(especeStr);
            int ageMaturitePied = Integer.parseInt(planteElement.getAttribute("Maturite_pied"));
            int ageMaturiteFruit = Integer.parseInt(planteElement.getAttribute("Maturite_fruit"));
            int nbRecolteMax = Integer.parseInt(planteElement.getAttribute("Nb_recolte"));
            double surface = Double.parseDouble(planteElement.getAttribute("Surface"));
            double humiditeMin = Double.parseDouble(planteElement.getAttribute("Humidite_min"));
            double humiditeMax = Double.parseDouble(planteElement.getAttribute("Humidite_max"));

            // Création de la plante
            Plante plante = new Plante(espece, ageMaturitePied, ageMaturiteFruit,
                    nbRecolteMax, surface, humiditeMin, humiditeMax);

            // Ajout à la parcelle
            parcelle.ajouterPlante(plante);

            System.out.println("Plante " + plante.getNomScientifique() + " ajoutée en (" +
                    parcelle.getPosX() + "," + parcelle.getPosY() + ")");
        }

        // Traitement des plantes drageonnantes
        NodeList planteDragNodes = parcelleElement.getElementsByTagName("Plante_Drageonnante");
        for (int i = 0; i < planteDragNodes.getLength(); i++) {
            Element planteElement = (Element) planteDragNodes.item(i);

            // Récupération des attributs
            String especeStr = planteElement.getAttribute("Espece");
            EspecePlante espece = EspecePlante.fromNomScientifique(especeStr);
            int ageMaturitePied = Integer.parseInt(planteElement.getAttribute("Maturite_pied"));
            int ageMaturiteFruit = Integer.parseInt(planteElement.getAttribute("Maturite_fruit"));
            int nbRecolteMax = Integer.parseInt(planteElement.getAttribute("Nb_recolte"));
            double surface = Double.parseDouble(planteElement.getAttribute("Surface"));
            double humiditeMin = Double.parseDouble(planteElement.getAttribute("Humidite_min"));
            double humiditeMax = Double.parseDouble(planteElement.getAttribute("Humidite_max"));
            double probaColonisation = Double.parseDouble(planteElement.getAttribute("Proba_Colonisation"));

            try {
                // Création de la plante drageonnante
                PlanteDrageonnante plante = new PlanteDrageonnante(
                        espece, ageMaturitePied, ageMaturiteFruit, nbRecolteMax,
                        surface, humiditeMin, humiditeMax, probaColonisation);

                // Ajout à la parcelle
                parcelle.ajouterPlante(plante);

                System.out.println("Plante drageonnante " + plante.getNomScientifique() +
                        " (Proba: " + probaColonisation + ") ajoutée en (" +
                        parcelle.getPosX() + "," + parcelle.getPosY() + ")");
            } catch (IllegalArgumentException e) {
                System.err.println("Erreur: " + e.getMessage() +
                        " à la position (" + parcelle.getPosX() + "," + parcelle.getPosY() + ")");
            }
        }
    }

    /**
     * Ajoute les insectes définis dans l'élément XML à la parcelle
     */
    private void ajouterInsectes(Element parcelleElement, Parcelle parcelle) {
        NodeList insecteNodes = parcelleElement.getElementsByTagName("Insecte");
        for (int i = 0; i < insecteNodes.getLength(); i++) {
            Element insecteElement = (Element) insecteNodes.item(i);

            // Récupération des attributs
            String especeStr = insecteElement.getAttribute("Espece");
            EspeceInsecte espece = EspeceInsecte.fromNom(especeStr);
            String sexe = insecteElement.getAttribute("Sexe");
            int vieMax = Integer.parseInt(insecteElement.getAttribute("Vie_max"));
            int dureeVieMax = Integer.parseInt(insecteElement.getAttribute("Duree_vie_max"));
            double probaMobilite = Double.parseDouble(insecteElement.getAttribute("Proba_mobilite"));
            double resistanceInsecticide = Double.parseDouble(insecteElement.getAttribute("Resistance_insecticide"));
            int maxPortee = Integer.parseInt(insecteElement.getAttribute("Max_portee"));
            int tempsEntreRepro = Integer.parseInt(insecteElement.getAttribute("Temps_entre_repro"));

            // Création de l'insecte
            Insecte insecte = new Insecte(espece, sexe, vieMax, dureeVieMax,
                    probaMobilite, resistanceInsecticide,
                    maxPortee, tempsEntreRepro);

            // Ajout à la parcelle
            parcelle.ajouterInsecte(insecte);

            System.out.println("Insecte " + insecte.getEspece().getNom() + " (" + sexe + ") ajouté en (" +
                    parcelle.getPosX() + "," + parcelle.getPosY() + ")");
        }
    }

    /**
     * Ajoute le dispositif défini dans l'élément XML à la parcelle
     */
    private void ajouterDispositif(Element parcelleElement, Parcelle parcelle) {
        NodeList dispositifNodes = parcelleElement.getElementsByTagName("Dispositif");
        if (dispositifNodes.getLength() > 0) {
            Element dispositifElement = (Element) dispositifNodes.item(0);

            // Récupération du rayon
            int rayon = Integer.parseInt(dispositifElement.getAttribute("Rayon"));

            // Création du dispositif
            Dispositif dispositif = new Dispositif(rayon);

            // Ajout des programmes
            NodeList programmeNodes = dispositifElement.getElementsByTagName("Programme");
            for (int i = 0; i < programmeNodes.getLength(); i++) {
                Element programmeElement = (Element) programmeNodes.item(i);

                // Récupération des attributs
                String produitStr = programmeElement.getAttribute("Produit");
                TypeTraitement produit = TypeTraitement.fromLibelle(produitStr);
                int debut = Integer.parseInt(programmeElement.getAttribute("Debut"));
                int duree = Integer.parseInt(programmeElement.getAttribute("Duree"));
                int periode = Integer.parseInt(programmeElement.getAttribute("Periode"));

                // Création du programme
                Programme programme = new Programme(produit, debut, duree, periode);

                // Ajout au dispositif
                dispositif.ajouterProgramme(programme);

                System.out.println("Programme " + produit.getLibelle() + " (début: " + debut +
                        ", durée: " + duree + ", période: " + periode +
                        ") ajouté au dispositif en (" + parcelle.getPosX() +
                        "," + parcelle.getPosY() + ")");
            }

            // Association du dispositif à la parcelle
            parcelle.setDispositif(dispositif);
            dispositif.setParcelle(parcelle);

            System.out.println("Dispositif (rayon: " + rayon + ") ajouté en (" +
                    parcelle.getPosX() + "," + parcelle.getPosY() + ")");
        }
    }

    // Getters pour les informations de configuration
    public String getNomPotager() {
        return nomPotager;
    }

    public int getNbIterations() {
        return nbIterations;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }
}