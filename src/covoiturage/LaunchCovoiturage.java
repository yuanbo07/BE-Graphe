package covoiturage;

import core.*;
import base.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * La classe LaunchCovoiturage
 * 
 * On lancer les trois algorithmes Dijkstra, récupère le meilleur point de rencontre,
 * affiche le coût total en temps et les informations, finalement dessine les trois chemins sur la carte.
 */

public class LaunchCovoiturage extends Algo {
	
	// les numéro de noeuds du piéton / automobiliste / destination saisis par l'utilisateur
	private int numPieton ;
	private int numAutomobiliste ;
	private int numDestination ;
	// label du point de rencontre pour le covoiturage
	private LabelCovoiturage labelPointRencontre ;
	// le point de rencontre pour le covoiturage
	private int pointRencontre ;
	// le nombre de sommets parcourus par trois algorithmes
	private int nbParcouruTotal ;
	// le nombre de sommets maximals dans le tas pendant trois algorithmes
	private int nbTasMax ;
	// le temps d'exécution pendant toute la recherche du point de rencontre
	private long tempsExecutionTotal ;
	// le temps total minimal utilisé pour le covoiturage
	private double coutTotalCovoiturage ;
	// la proportion entre vitesse de l'automobiliste et la vitesse du piéton
	private int proportionVitesse ;
	// la faisabilité d'un tel covoiturage (le piéton et l'automobiliste peuvent au moins tous les deux atteindre la destination)
	private boolean faisabiliteCovoiturage = false;
	// le temps maximal que le piéton peut marcher, en minute
	private int limitePieton ;
    // hashmap pour stocker & faciliter l'usage de noeuds en commun
	public HashMap<Integer, LabelCovoiturage> mapListeNoeudCommun = new HashMap<Integer, LabelCovoiturage>();
	// le tas pour renvoyer le coût minimal en temps
	BinaryHeap<LabelCovoiturage> tas = new BinaryHeap<LabelCovoiturage>();
	
	/**
	 * constructeurs
	 */
	public LaunchCovoiturage(Graphe gr, Readarg readarg){
		super(gr);
		this.numPieton = readarg.lireInt("Le numéro de noeud d'origine du piéton?");
		this.numAutomobiliste = readarg.lireInt("Le numéro de noeud d'origine de l'automobiliste?");
		this.numDestination = readarg.lireInt("Le numéro de noeud de destination?");
		this.proportionVitesse = readarg.lireInt("Vous voulez que l'automobiliste conduit à quelle vitesse par rapport au piéton ?\n " +
				"(1) même vitesse (2) 2 fois plus vite (3) 5 fois plus vite (4) 10 fois plus vite (5) 20 fois plus vite (6) vitesse maximale");
		this.limitePieton = readarg.lireInt("Le piéton peut au maximal marcher combien de minutes ? (-1 si pas de limite)");
		this.pointRencontre = numDestination ;
		lancerAlgoCovoiturage();
	}
	
	public void lancerAlgoCovoiturage()
	{
		if(verifierSaisie()){
			
	    	long debut = System.nanoTime();
			// lancer l'algorithme Dijkstra "un vers tous" pour le piéton
			PccReg algoParcoursPieton = new PccReg(graphe, numPieton, 1, 1, limitePieton);
			algoParcoursPieton.run();
			
			// lancer l'algorithme Dijkstra "un vers tous" pour l'automobiliste
			PccReg algoParcoursAutomobiliste = new PccReg(graphe, numAutomobiliste, 0, proportionVitesse);
			algoParcoursAutomobiliste.run();
			
			System.out.println("nb max tas pieton:"+algoParcoursPieton.getNbMaxTas());
			System.out.println("nb max tas auto:"+algoParcoursAutomobiliste.getNbMaxTas());
	
			// on cherche tous les noeuds à la fois parcourus par le piéton et l'automobiliste, et les ajoutent dans hashmap
			for(int i =0;i < Algo.graphe.getListeNoeuds().size();i++){
				if(algoParcoursPieton.mapCorrespondanceNoeudLabel.get(i).isParcouru_pieton() && 
						algoParcoursAutomobiliste.mapCorrespondanceNoeudLabel.get(i).isParcouru_automobiliste())
						{
						// on obtient leur coût
						double coutAutomobiliste = algoParcoursAutomobiliste.mapCorrespondanceNoeudLabel.get(i).getCoutCourant() ;
						double coutPieton = algoParcoursPieton.mapCorrespondanceNoeudLabel.get(i).getCoutCourant() ;
						LabelCovoiturage s = new LabelCovoiturage(i, coutPieton, coutAutomobiliste);
						mapListeNoeudCommun.put(i,s);
					}
				}
			
			// on lance la recherche inverse à partir de destination, seuls les sommets dans le hashmap seront marqués
			PccInverse algoParcoursDest = new PccInverse(graphe, numDestination, mapListeNoeudCommun, proportionVitesse);
			algoParcoursDest.run();
	
			System.out.println("nb max tas dest:"+algoParcoursDest.getNbMaxTas());
			
			System.out.println("nb parcouru auto:"+algoParcoursAutomobiliste.getNbParcouru());
			System.out.println("nb parcouru pieton :"+algoParcoursPieton.getNbParcouru());
			System.out.println("nb parcouru dest :"+algoParcoursDest.getNbParcouru());
			
			System.out.println("nb marque auto:"+algoParcoursAutomobiliste.getNbMarque());
			System.out.println("nb marque pieton :"+algoParcoursPieton.getNbMarque());
			System.out.println("nb marque dest :"+algoParcoursDest.getNbMarque());
			
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
			
			// on obtient le meilleur point de rencontre, le pire des cas est le sommet destination
			if(!tas.isEmpty()){
				labelPointRencontre = tas.findMin() ;
				pointRencontre = labelPointRencontre.getId_sommet() ;
				faisabiliteCovoiturage = true ;
			}
			else{
				System.out.println("Le piéton et l'automobiliste sont impossible de se rencontrer.");
			}
			
			// la recherche du point de rencontre est terminé, on arrête le temps d'exécution
			tempsExecutionTotal = System.nanoTime() - debut;
			
			if(faisabiliteCovoiturage){
				// si le covoiturage est faisable, on récupère les informations
				coutTotalCovoiturage = Math.max(labelPointRencontre.getCoutAutomobiliste(),labelPointRencontre.getCoutPieton())+labelPointRencontre.getCoutDestination() ;
				nbParcouruTotal = algoParcoursAutomobiliste.getNbParcouru() + algoParcoursPieton.getNbParcouru() + algoParcoursDest.getNbParcouru() ;
				nbTasMax = Math.max(Math.max(algoParcoursAutomobiliste.getNbMaxTas(), algoParcoursPieton.getNbMaxTas()),algoParcoursDest.getNbMaxTas());

				// on construit les trois plus court chemin 
				PccPieton cheminPieton = new PccPieton(graphe,numPieton,pointRencontre);
				cheminPieton.run() ;
				cheminPieton.construirePlusCourtChemin();
				cheminPieton.afficherPlusCourtChemin() ;
				
				Pcc cheminAutomobiliste = new Pcc(graphe,numAutomobiliste,pointRencontre);
				cheminAutomobiliste.run() ;
				cheminAutomobiliste.construirePlusCourtChemin();
				cheminAutomobiliste.afficherPlusCourtChemin();
				
				Pcc cheminCovoiturage = new Pcc(graphe,pointRencontre,numDestination);
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
				System.out.println("Le piéton doit marcher : "+ Utils.tempsEnMinToString(labelPointRencontre.getCoutPieton()));
				System.out.println("L'automobiliste doit conduire : "+ Utils.tempsEnMinToString(labelPointRencontre.getCoutAutomobiliste()));
				System.out.println("Le trajet en commun en voiture est : "+ Utils.tempsEnMinToString(labelPointRencontre.getCoutDestination()));
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