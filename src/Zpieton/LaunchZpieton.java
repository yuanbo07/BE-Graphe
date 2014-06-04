package Zpieton;

import core.*;
import covoiturage.*;
import base.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * La classe Zpieton est pour résoudre le problème du covoiturage avec un guidage A*
 */

public class LaunchZpieton extends Algo {
	
	// les numéro de sommet du piéton / automobiliste / destination saisis par l'utilisateur
	private int numPieton ;
	private int numAutomobiliste ;
	private int numDestination ;
	// label du meilleur point de rencontre pour le covoiturage
	private LabelCovoiturage labelPointRencontre ;
	// le numéro de meuilleur point de rencontre pour le covoiturage
	private int pointRencontre ;
	// le nombre de sommets parcourus par trois algorithmes
	private int nbParcouruTotal ;
	// le nombre de sommets maximals dans le tas pendant le déroulement de trois algorithmes
	private int nbTasMax ;
	// le temps d'exécution pendant toute la recherche du point de rencontre
	private long tempsExecutionTotal ;
	// le temps total minimal utilisé pour le covoiturage
	private double coutTotalCovoiturage ;
	// la faisabilité d'un tel covoiturage (le piéton et l'automobiliste peuvent "au moins" tous les deux atteindre la destination)
	private boolean faisabiliteCovoiturage = false;
	// le temps maximal que le piéton peut marcher, en minute
	private int limitePieton ;
    // hashmap qui stocke les sommets parcourus par le piéton
	private HashMap<Integer, LabelCovoiturage> mapListeNoeudMarquePieton = new HashMap<Integer, LabelCovoiturage>();
    // hashmap pour stocker & faciliter l'usage de noeuds en commun
	private HashMap<Integer, LabelCovoiturage> mapListeNoeudCommun = new HashMap<Integer, LabelCovoiturage>();
	// le tas pour renvoyer le coût minimal en temps
	private BinaryHeap<LabelCovoiturage> tas = new BinaryHeap<LabelCovoiturage>();
	
	/**
	 * constructeurs
	 */
	public LaunchZpieton(Graphe gr, Readarg readarg){
		super(gr);
		this.numPieton = readarg.lireInt("Le numéro de noeud d'origine du piéton?");
		this.numAutomobiliste = readarg.lireInt("Le numéro de noeud d'origine de l'automobiliste?");
		this.numDestination = readarg.lireInt("Le numéro de noeud de destination?");
		this.limitePieton = readarg.lireInt("Le piéton peut au maximal marcher combien de minutes ? (-1 si pas de limite)");
		lancerAlgoZpieton();
	}
	
	public void lancerAlgoZpieton()
	{
		int i = 0 ;
		// si la saisie par l'utilisateur est correcte
		if(verifierSaisie()){
			// on commence à calculer le temps d'exécution
	    	long debut = System.nanoTime();
	    	
			/**
			 * 	étape 1	:
			 * 
			 *	Lancer l'algorithme Dijkstra "un vers tous" par le piéton
			 *	La zone "Zpiéton" est l'ensemble de sommets dont leur coût en temps < x min 
			 */
	    	
			PccReg algoParcoursPieton = new PccReg(graphe, numPieton, 1, 1, limitePieton);
			algoParcoursPieton.run();

			// on cherche tous les noeuds marqués par le piéton, et les insère dans un hashmap
			for(i=0 ; i < Algo.graphe.getListeNoeuds().size();i++){
				Label l = algoParcoursPieton.getMapCorrespondanceNoeudLabel().get(i) ;
				// si le sommet est marqué par piéton
				if(l.isParcouru_pieton()){
					// on l'insère dans le hashmap
					LabelCovoiturage s = new LabelCovoiturage(i, l.getCoutCourant(), 0);
					mapListeNoeudMarquePieton.put(i, s);
					}
			}
			
			/**
			 * 	étape 2	:
			 * 
			 *	Lancer A* par l'automobiliste : origine est l'origine de l'automobiliste, destination est l'origine du piéton.
			 *	Dès qu'on rencontre un sommet parcouru par piéton(le premier sommet rencontré dans Zpiéton) : on se transforme
			 *	en Dijkstra "un vers tous", afin de parcourir tous les sommets dans Zpiéton.
			 *
			 *	Pour tous les sommets choisis dont le coût en distance > vitessePiéton * tempsMarchePiéton (rayon du circle),
			 *	on s'arrête.
			 */
			
			// lancer l'algorithme A* par l'automobiliste, puis cherche la zone Zpiéton avec Dijkstra "un vers tous"
			// une explication détaillée est fournie dans la classe PccZpieton
			PccZpieton algoParcoursAutomobiliste = new PccZpieton(graphe, numAutomobiliste, numPieton, mapListeNoeudMarquePieton, limitePieton);
			algoParcoursAutomobiliste.run();			
			
			// on cherche tous les noeuds en commun entre le piéton et l'automobiliste, et les ajoutent dans le hashmap
			for (Integer key : algoParcoursAutomobiliste.getMapListeNoeudEnCommun().keySet()) {
				double coutAutomobiliste = algoParcoursAutomobiliste.getMapListeNoeudEnCommun().get(key).getCoutCourant() ;
				double coutPieton = mapListeNoeudMarquePieton.get(key).getCoutPieton() ;
				LabelCovoiturage s = new LabelCovoiturage(key, coutPieton, coutAutomobiliste);
				mapListeNoeudCommun.put(key,s);
			}

			/**
			 * 	étape 3	:
			 * 
			 *	Lancer Dijkstra "un vers tous" à partir du sommet destination, et la suite est identique avec "Covoiturage".
			 */
			
			// on lance la recherche inverse à partir de destination, seuls les sommets dans le hashmap seront marqués
			PccInverse algoParcoursDest = new PccInverse(Algo.graphe, numDestination, mapListeNoeudCommun, 6);
			algoParcoursDest.run();
			
			// on parcourt le hashmap et insère tous les points de rencontres possibles dans le tas, et les ordonner	
			for (Integer key : mapListeNoeudCommun.keySet()) {
				if(algoParcoursDest.mapCorrespondanceNoeudLabel.get(key).isParcouru_destination())
				{
					double coutDest = algoParcoursDest.mapCorrespondanceNoeudLabel.get(key).getCoutCourant();
					mapListeNoeudCommun.get(key).setCoutDestination(coutDest);
					tas.insert(mapListeNoeudCommun.get(key));
					tas.update(mapListeNoeudCommun.get(key));
				}
			}

			// on obtient le meilleur point de rencontre, le pire des cas est le sommet de destination
			if(!tas.isEmpty()){
				labelPointRencontre = tas.findMin() ;
				pointRencontre = labelPointRencontre.getId_sommet() ;
				faisabiliteCovoiturage = true ;
			}
			else{
				System.out.println("Le piéton et l'automobiliste sont impossibles de se rencontrer.");
			}
				
			// la recherche du point de rencontre est terminé, on arrête le temps d'exécution
			tempsExecutionTotal = System.nanoTime() - debut;
			
			// si le covoiturage est faisable
			if(faisabiliteCovoiturage){
				// on récupère les informations à afficher
				coutTotalCovoiturage = Math.max(labelPointRencontre.getCoutAutomobiliste(),labelPointRencontre.getCoutPieton())+labelPointRencontre.getCoutDestination() ;
				nbParcouruTotal = algoParcoursAutomobiliste.getNbParcouru() + algoParcoursPieton.getNbParcouru() + algoParcoursDest.getNbParcouru() ;
				nbTasMax = Math.max(Math.max(algoParcoursAutomobiliste.getNbMaxTas(), algoParcoursPieton.getNbMaxTas()),algoParcoursDest.getNbMaxTas());

				// on construit les trois plus court chemin et les affiche
				PccPieton cheminPieton = new PccPieton(Algo.graphe,numPieton,pointRencontre);
				cheminPieton.run() ;
				cheminPieton.construirePlusCourtChemin();
				cheminPieton.afficherPlusCourtChemin() ;
				
				Pcc cheminAutomobiliste = new Pcc(Algo.graphe,numAutomobiliste,pointRencontre);
				cheminAutomobiliste.run() ;
				cheminAutomobiliste.construirePlusCourtChemin();
				cheminAutomobiliste.afficherPlusCourtChemin();
				
				Pcc cheminCovoiturage = new Pcc(Algo.graphe,pointRencontre,numDestination);
				cheminCovoiturage.run() ;
				cheminCovoiturage.construirePlusCourtChemin();
				cheminCovoiturage.afficherPlusCourtChemin();
				
		    	System.out.println();
				System.out.println("****** INFORMATION DU COVOITURAGE ******");
		    	System.out.println();
				System.out.println("Le meilleur point de rencontre est : "+pointRencontre);
				System.out.println("La durée totale minimale est : "+ Utils.tempsEnMinToString(coutTotalCovoiturage));
		    	System.out.println();
				/* 
				 * Le temps utilisé par le piéton et l'automobiliste peut être différent,
				 * dans certains cas, on économise le temps, si le piéton attend un peu l'arrivée de l'automobiliste,
				 * que le piéton marche vers les autres points par lui-même.
				 */
				System.out.println("En prenant ce point de rencontre : ");
				System.out.println(" - Le piéton doit marcher : "+ Utils.tempsEnMinToString(labelPointRencontre.getCoutPieton()));
				System.out.println(" - L'automobiliste doit conduire : "+ Utils.tempsEnMinToString(labelPointRencontre.getCoutAutomobiliste()));
				System.out.println(" - Le trajet en commun en voiture est : "+ Utils.tempsEnMinToString(labelPointRencontre.getCoutDestination()));
		    	System.out.println();
				System.out.println(nbParcouruTotal + " sommets ont été parcourus pendant toute la résolution du point de rencontre.");
				System.out.println(nbTasMax + " est le nombre de sommets maximals dans le tas pendante toute la recherche.");
				System.out.println("Le temps d'exécution total est : "+tempsExecutionTotal);
				System.out.println("dont : ");
				System.out.println(" - algorithme piéton : "+algoParcoursPieton.getTempsExecution());
				System.out.println(" - algorithme automobiliste : "+algoParcoursAutomobiliste.getTempsExecution());
				System.out.println(" - algorithme recherche inverse : "+algoParcoursDest.getTempsExecution());
				System.out.println(" - partie traitement : " + (tempsExecutionTotal - algoParcoursDest.getTempsExecution()- algoParcoursAutomobiliste.getTempsExecution() - algoParcoursPieton.getTempsExecution()));
				System.out.println();
				System.out.println("Le nombre de tous les points de rencontres possibles est "+mapListeNoeudCommun.size());
			}
		}
		else {
			System.out.println();
			System.out.println("La saisie n'est pas correcte, veuillez saisir les numéros de noeuds à nouveau.");
			System.out.println();
		}
	}
	
	/**
	 * la fonction qui vérifie la saisie de l'utilisateur
	 */
	public boolean verifierSaisie(){
		
    	boolean trouveNumPieton = false;
    	boolean trouveNumAutomobiliste = false;
    	boolean trouveNumDest = false;
    	int i=0;
		for (; i< Algo.graphe.getListeNoeuds().size() ; i++){
			int numCourant = Algo.graphe.getListeNoeuds().get(i).getId_noeud();
    		if(numCourant == numPieton)
    			trouveNumPieton = true;
    		else if(numCourant == numAutomobiliste)
    			trouveNumAutomobiliste = true ;
    		else if(numCourant == numDestination)
    			trouveNumDest = true;
		}
		return (trouveNumPieton && trouveNumAutomobiliste && trouveNumDest);
	}
	
	public void run() throws IOException {
	}
}