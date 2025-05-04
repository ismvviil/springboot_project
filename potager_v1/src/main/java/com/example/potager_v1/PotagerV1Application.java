package com.example.potager_v1;

import com.example.potager_v1.model.Parcelle;
import com.example.potager_v1.model.Simulation;
import com.example.potager_v1.model.enums.EspeceInsecte;
import com.example.potager_v1.model.enums.EspecePlante;
import com.example.potager_v1.model.Insecte;
import com.example.potager_v1.model.Plante;
import com.example.potager_v1.model.traitement.Dispositif;
import com.example.potager_v1.model.traitement.Programme;
import com.example.potager_v1.model.traitement.TypeTraitement;
import com.example.potager_v1.repository.ConfigurationLoader;
import com.example.potager_v1.service.InsecteService;
import com.example.potager_v1.service.PlanteService;
import com.example.potager_v1.service.SimulationService;
import com.example.potager_v1.service.TraitementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class PotagerV1Application implements CommandLineRunner {

	@Autowired
	private SimulationService simulationService;

	@Autowired
	private PlanteService planteService;

	@Autowired
	private InsecteService insecteService;

	@Autowired
	private TraitementService traitementService;

	@Autowired
	private ConfigurationLoader configurationLoader;

	public static void main(String[] args) {
		SpringApplication.run(PotagerV1Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("=== DÉMARRAGE DU TEST GLOBAL DE L'APPLICATION POTAGER AUTOMATISÉ ===");

		// Test 1: Chargement d'une configuration XML
		testChargementConfiguration();

		// Test 2: Création et gestion d'une simulation
		Simulation simulation = testGestionSimulation();

		//Test 3: Exécution d'une simulation pas à pas
		testExecutionPasAPas(simulation);

		// Test 4: Statistiques et état de la simulation
		testStatistiquesSimulation(simulation);

		// Test 5: Test des services spécifiques
		testServicesSpecifiques(simulation);

		// Test 6: Exécution de la simulation complète
		//testSimulationComplete(simulation);

		// Fin des tests
		System.out.println("\n=== FIN DES TESTS - APPLICATION POTAGER AUTOMATISÉ ===");
		//System.exit(0); // Arrêt de l'application après les tests
	}

	/**
	 * Test du chargement d'une configuration XML
	 */
	private void testChargementConfiguration() {
		System.out.println("\n=== TEST 1: CHARGEMENT D'UNE CONFIGURATION XML ===");

		try {
			// Chemin vers le fichier XML à tester
			String cheminFichier = "src/main/resources/potager_test.xml";

			// Chargement de la configuration (teste le ConfigurationLoader)
			System.out.println("Chargement du fichier: " + cheminFichier);
			Map<String, Parcelle> parcelles = configurationLoader.chargerConfiguration(cheminFichier);

			// Vérification du résultat
			System.out.println("Configuration chargée avec succès");
			System.out.println("Nom du potager: " + configurationLoader.getNomPotager());
			System.out.println("Dimensions: " + configurationLoader.getSizeX() + "x" + configurationLoader.getSizeY());
			System.out.println("Nombre d'itérations: " + configurationLoader.getNbIterations());
			System.out.println("Nombre de parcelles: " + parcelles.size());

			// Analyse du contenu
			int nbPlantes = 0;
			int nbInsectes = 0;
			int nbDispositifs = 0;

			for (Parcelle parcelle : parcelles.values()) {
				nbPlantes += parcelle.getPlantes().size();
				nbInsectes += parcelle.getInsectes().size();
				if (parcelle.aDispositif()) {
					nbDispositifs++;
				}
			}

			System.out.println("Plantes chargées: " + nbPlantes);
			System.out.println("Insectes chargés: " + nbInsectes);
			System.out.println("Dispositifs chargés: " + nbDispositifs);

			System.out.println("Test de chargement de configuration: RÉUSSI");
		} catch (Exception e) {
			System.err.println("Erreur lors du chargement de la configuration: " + e.getMessage());
			e.printStackTrace();
			System.err.println("Test de chargement de configuration: ÉCHEC");
		}
	}

	/**
	 * Test de création et gestion d'une simulation
	 */
	private Simulation testGestionSimulation() {
		System.out.println("\n=== TEST 2: CRÉATION ET GESTION D'UNE SIMULATION ===");

		try {
			// Initialisation d'une simulation
			String cheminFichier = "src/main/resources/potager_test.xml";
			System.out.println("Initialisation d'une simulation à partir de: " + cheminFichier);
			Simulation simulation = simulationService.initialiserSimulation(cheminFichier);

			// Vérification de l'initialisation
			System.out.println("Simulation initialisée avec succès");
			System.out.println("ID: " + simulation.getId());
			System.out.println("Nom: " + simulation.getNom());
			System.out.println("Dimensions: " + simulation.getSizeX() + "x" + simulation.getSizeY());
			System.out.println("Nombre maximal d'itérations: " + simulation.getNbIterationsMax());
			System.out.println("Pas actuel: " + simulation.getPasSimulationActuel());
			System.out.println("En cours: " + simulation.isEnCours());

			// Test du démarrage
			System.out.println("\nDémarrage de la simulation...");
			simulation = simulationService.demarrerSimulation(simulation.getId());
			System.out.println("Simulation démarrée. État: " + (simulation.isEnCours() ? "En cours" : "Arrêtée"));

			// Test de l'arrêt
			System.out.println("\nArrêt de la simulation...");
			simulation = simulationService.arreterSimulation(simulation.getId());
			System.out.println("Simulation arrêtée. État: " + (simulation.isEnCours() ? "En cours" : "Arrêtée"));

			// Récupération de toutes les simulations
			List<Simulation> simulations = simulationService.getAllSimulations();
			System.out.println("\nNombre total de simulations en base: " + simulations.size());

			System.out.println("Test de création et gestion d'une simulation: RÉUSSI");
			return simulation;

		} catch (Exception e) {
			System.err.println("Erreur lors de la gestion de la simulation: " + e.getMessage());
			e.printStackTrace();
			System.err.println("Test de création et gestion d'une simulation: ÉCHEC");
			return null;
		}
	}

	/**
	 * Test d'exécution d'une simulation pas à pas
	 */
	private void testExecutionPasAPas(Simulation simulation) {
		System.out.println("\n=== TEST 3: EXÉCUTION D'UNE SIMULATION PAS À PAS ===");

		if (simulation == null) {
			System.err.println("Simulation non disponible, test ignoré");
			return;
		}

		try {
			// Démarrage de la simulation
			simulation = simulationService.demarrerSimulation(simulation.getId());

			// Exécution de 5 pas de simulation
			System.out.println("Exécution de 5 pas de simulation...");
			for (int i = 0; i < 5; i++) {
				simulation = simulationService.executerPas(simulation.getId());
				System.out.println("Pas " + simulation.getPasSimulationActuel() + " exécuté");
			}

			// Exécution groupée de plusieurs pas
			System.out.println("\nExécution groupée de 10 pas supplémentaires...");
			simulation = simulationService.executerPlugieursPas(simulation.getId(), 10);
			System.out.println("Pas actuel après exécution groupée: " + simulation.getPasSimulationActuel());

			// Arrêt de la simulation
			simulation = simulationService.arreterSimulation(simulation.getId());

			System.out.println("Test d'exécution d'une simulation pas à pas: RÉUSSI");

		} catch (Exception e) {
			System.err.println("Erreur lors de l'exécution pas à pas: " + e.getMessage());
			e.printStackTrace();
			System.err.println("Test d'exécution d'une simulation pas à pas: ÉCHEC");
		}
	}

	/**
	 * Test de récupération des statistiques et de l'état d'une simulation
	 */
	private void testStatistiquesSimulation(Simulation simulation) {
		System.out.println("\n=== TEST 4: STATISTIQUES ET ÉTAT DE LA SIMULATION ===");

		if (simulation == null) {
			System.err.println("Simulation non disponible, test ignoré");
			return;
		}

		try {
			// Récupération de l'état complet
			Map<String, Object> etat = simulationService.getEtatSimulation(simulation.getId());

			// Affichage des informations de base
			Simulation sim = (Simulation) etat.get("simulation");
			System.out.println("Nom de la simulation: " + sim.getNom());
			System.out.println("Pas actuel: " + sim.getPasSimulationActuel());
			System.out.println("En cours: " + sim.isEnCours());

			// Affichage des statistiques
			Map<String, Object> stats = (Map<String, Object>) etat.get("statistiques");
			System.out.println("\nStatistiques:");
			System.out.println("- Nombre de parcelles: " + stats.get("nbParcelles"));
			System.out.println("- Nombre de plantes: " + stats.get("nbPlantes"));
			System.out.println("- Nombre d'insectes: " + stats.get("nbInsectes"));
			System.out.println("- Nombre de dispositifs: " + stats.get("nbDispositifs"));
			System.out.println("- Plantes matures avec fruits: " + stats.get("nbPlantesMaturesAvecFruits"));

			// Détail des espèces
			System.out.println("\nRépartition des plantes par espèce:");
			Map<String, Integer> compteurPlantes = (Map<String, Integer>) stats.get("compteurEspecesPlantes");
			for (Map.Entry<String, Integer> entry : compteurPlantes.entrySet()) {
				System.out.println("- " + entry.getKey() + ": " + entry.getValue());
			}

			System.out.println("\nRépartition des insectes par espèce:");
			Map<String, Integer> compteurInsectes = (Map<String, Integer>) stats.get("compteurEspecesInsectes");
			for (Map.Entry<String, Integer> entry : compteurInsectes.entrySet()) {
				System.out.println("- " + entry.getKey() + ": " + entry.getValue());
			}

			System.out.println("Test de statistiques et état de la simulation: RÉUSSI");

		} catch (Exception e) {
			System.err.println("Erreur lors de la récupération des statistiques: " + e.getMessage());
			e.printStackTrace();
			System.err.println("Test de statistiques et état de la simulation: ÉCHEC");
		}
	}

	/**
	 * Test des services spécifiques (plantes, insectes, traitements)
	 */
	private void testServicesSpecifiques(Simulation simulation) {
		System.out.println("\n=== TEST 5: SERVICES SPÉCIFIQUES ===");

		if (simulation == null) {
			System.err.println("Simulation non disponible, test ignoré");
			return;
		}

		try {
			// Test 5.1: Service des plantes
			testPlanteService();

			// Test 5.2: Service des insectes
			testInsecteService();

			// Test 5.3: Service des traitements
			testTraitementService();

		} catch (Exception e) {
			System.err.println("Erreur lors du test des services spécifiques: " + e.getMessage());
			e.printStackTrace();
			System.err.println("Test des services spécifiques: ÉCHEC");
		}
	}

	/**
	 * Test du service de gestion des plantes
	 */
	private void testPlanteService() {
		System.out.println("\n=== TEST 5.1: SERVICE DES PLANTES ===");

		try {
			// Création d'une plante pour test
			Plante plante = new Plante(EspecePlante.TOMATE, 5, 7, 3, 0.5, 0.4, 0.8);
			System.out.println("Plante créée: " + plante.getNomScientifique());
			System.out.println("Age initial: " + plante.getAge());
			System.out.println("Mature: " + plante.isMature());

			// Test de mise à jour
			for (int i = 0; i < 10; i++) {
				plante.miseAJour();
				System.out.println("Jour " + (i + 1) + " - Age: " + plante.getAge() + ", Mature: " + plante.isMature() + ", Peut produire: " + plante.peutProduireFruits());
			}

			// Test de récolte
			if (plante.peutProduireFruits()) {
				boolean recolte = plante.recolter();
				System.out.println("Récolte effectuée: " + recolte);
				System.out.println("Nombre de récoltes effectuées: " + plante.getNbRecoltesEffectuees());
			}

			System.out.println("Test du service des plantes: RÉUSSI");

		} catch (Exception e) {
			System.err.println("Erreur lors du test du service des plantes: " + e.getMessage());
			e.printStackTrace();
			System.err.println("Test du service des plantes: ÉCHEC");
		}
	}

	/**
	 * Test du service de gestion des insectes
	 */
	private void testInsecteService() {
		System.out.println("\n=== TEST 5.2: SERVICE DES INSECTES ===");

		try {
			// Création d'insectes pour test
			Insecte insecteM = new Insecte(EspeceInsecte.PUCERON, "M", 10, 30, 0.6, 0.2, 5, 10);
			Insecte insecteF = new Insecte(EspeceInsecte.PUCERON, "F", 12, 30, 0.5, 0.3, 6, 8);

			System.out.println("Insecte mâle créé: " + insecteM.getEspece().getNom() + " (Vie: " + insecteM.getVie() + ")");
			System.out.println("Insecte femelle créé: " + insecteF.getEspece().getNom() + " (Vie: " + insecteF.getVie() + ")");

			// Test de mise à jour et reproduction
			for (int i = 0; i < 10; i++) {
				insecteM.miseAJour();
				insecteF.miseAJour();

				System.out.println("Jour " + (i + 1));
				System.out.println("- Mâle - Age: " + insecteM.getAge());
				System.out.println("- Femelle - Age: " + insecteF.getAge() + ", Peut se reproduire: " + insecteF.peutSeReproduire());

				if (insecteF.peutSeReproduire()) {
					int nbDescendants = insecteF.seReproduire();
					System.out.println("  Reproduction: " + nbDescendants + " descendants");
				}
			}

			// Test d'application d'insecticide
			boolean mortM = insecteM.appliquerInsecticide();
			boolean mortF = insecteF.appliquerInsecticide();
			System.out.println("Application d'insecticide:");
			System.out.println("- Mâle mort: " + mortM);
			System.out.println("- Femelle morte: " + mortF);

			System.out.println("Test du service des insectes: RÉUSSI");

		} catch (Exception e) {
			System.err.println("Erreur lors du test du service des insectes: " + e.getMessage());
			e.printStackTrace();
			System.err.println("Test du service des insectes: ÉCHEC");
		}
	}

	/**
	 * Test du service de gestion des traitements
	 */
	private void testTraitementService() {
		System.out.println("\n=== TEST 5.3: SERVICE DES TRAITEMENTS ===");

		try {
			// Création d'un dispositif pour test
			Dispositif dispositif = new Dispositif(2);
			System.out.println("Dispositif créé avec rayon: " + dispositif.getRayon());

			// Ajout de programmes
			Programme programmeEau = new Programme(TypeTraitement.EAU, 1, 2, 4);
			Programme programmeEngrais = new Programme(TypeTraitement.ENGRAIS, 5, 1, 10);
			Programme programmeInsecticide = new Programme(TypeTraitement.INSECTICIDE, 10, 1, 20);

			dispositif.ajouterProgramme(programmeEau);
			dispositif.ajouterProgramme(programmeEngrais);
			dispositif.ajouterProgramme(programmeInsecticide);

			System.out.println("Programmes ajoutés: " + dispositif.getProgrammes().size());

			// Test d'activation des programmes
			System.out.println("\nTest d'activation des programmes:");
			for (int pas = 1; pas <= 20; pas++) {
				System.out.print("Pas " + pas + " - Actifs: ");
				boolean auMoinsUnActif = false;

				for (Programme programme : dispositif.getProgrammes()) {
					if (programme.estActif(pas)) {
						System.out.print(programme.getProduit().getLibelle() + " ");
						auMoinsUnActif = true;
					}
				}

				if (!auMoinsUnActif) {
					System.out.print("Aucun");
				}
				System.out.println();
			}

			System.out.println("Test du service des traitements: RÉUSSI");

		} catch (Exception e) {
			System.err.println("Erreur lors du test du service des traitements: " + e.getMessage());
			e.printStackTrace();
			System.err.println("Test du service des traitements: ÉCHEC");
		}
	}

	/**
	 * Test d'exécution d'une simulation limitée mais complète
	 */
	private void testSimulationComplete(Simulation simulation) {
		System.out.println("\n=== TEST 6: EXÉCUTION D'UNE SIMULATION LIMITÉE ===");

		if (simulation == null) {
			System.err.println("Simulation non disponible, test ignoré");
			return;
		}

		try {
			// Redémarrage de la simulation
			simulation = simulationService.demarrerSimulation(simulation.getId());
			simulation = simulationService.executerPlugieursPas(simulation.getId(), 2); // Quelques pas pour avancer un peu

			// Lancement de la simulation avec un nombre limité de pas
			System.out.println("Lancement de la simulation limitée...");
			long debut = System.currentTimeMillis();

			// Limiter à 30 pas maximum pour éviter les boucles infinies
			int maxPas = 30;
			int pasInitial = simulation.getPasSimulationActuel();
			int pasActuel = pasInitial;

			for (int i = 0; i < maxPas && simulation.isEnCours(); i++) {
				simulation = simulationService.executerPas(simulation.getId());
				pasActuel = simulation.getPasSimulationActuel();
				System.out.println("Progression: " + (pasActuel - pasInitial) + "/" + maxPas +
						" (pas " + pasActuel + "/" + simulation.getNbIterationsMax() + ")");
			}

			long fin = System.currentTimeMillis();
			long duree = fin - debut;

			// Vérification de l'état final
			System.out.println("Simulation terminée en " + duree + " ms");
			System.out.println("Pas final: " + simulation.getPasSimulationActuel() + "/" + simulation.getNbIterationsMax());
			System.out.println("État final: " + (simulation.isEnCours() ? "En cours" : "Arrêtée"));

			// Si la simulation est toujours en cours, l'arrêter manuellement
			if (simulation.isEnCours()) {
				System.out.println("Arrêt manuel de la simulation après " + maxPas + " pas");
				simulation = simulationService.arreterSimulation(simulation.getId());
			}

			// Récupération des statistiques finales
			Map<String, Object> etat = simulationService.getEtatSimulation(simulation.getId());
			Map<String, Object> stats = (Map<String, Object>) etat.get("statistiques");

			System.out.println("\nStatistiques finales:");
			System.out.println("- Plantes: " + stats.get("nbPlantes"));
			System.out.println("- Insectes: " + stats.get("nbInsectes"));
			System.out.println("- Plantes matures avec fruits: " + stats.get("nbPlantesMaturesAvecFruits"));

			System.out.println("Test d'exécution d'une simulation limitée: RÉUSSI");

		} catch (Exception e) {
			System.err.println("Erreur lors de l'exécution limitée: " + e.getMessage());
			e.printStackTrace();
			System.err.println("Test d'exécution d'une simulation limitée: ÉCHEC");
		}
	}
}