package covoiturage;

import core.Algo;
import core.BinaryHeap;
import core.Graphe;
import core.Label;
import core.Pcc;
import base.Readarg;
import java.io.IOException;

public class LancerCovoiturage extends Algo {
	
	private int numPieton ;
	private int numAutomobiliste ;
	private int numDestination ;
	BinaryHeap<Label> tasNoeudEnCommun = new BinaryHeap<Label>();
	
	public LancerCovoiturage(Graphe gr, Readarg readarg){
		super(gr);
		this.numPieton = readarg.lireInt("Le numéro de noeud d'origine du piéton?");
		this.numAutomobiliste = readarg.lireInt("Le numéro de noeud d'origine de l'automobiliste?");
		this.numDestination = readarg.lireInt("Le numéro de noeud de destination?");
		lancerAlgoCovoiturage();
	}
	
	public void lancerAlgoCovoiturage()
	{
		// lancer l'algorithme Dijkstra "un vers tous" pour le piéton
		PccPietonAutomobiliste algoParcourPieton = new PccPietonAutomobiliste(graphe, numPieton, 0);
		algoParcourPieton.run();
		// lancer l'algorithme Dijkstra "un vers tous" pour l'automobiliste
		PccPietonAutomobiliste algoParcourAutomobiliste = new PccPietonAutomobiliste(graphe, numAutomobiliste, 1);
		algoParcourAutomobiliste.run();
		
		// on cherche tous les noeuds à la fois parcourus par le piéton et l'automobiliste
		for(int i =0;i<this.graphe.getListeNoeuds().size();i++){
			if(algoParcourPieton.mapCorrespondanceNoeudLabel.get(i).isParcouru_pieton() && 
					algoParcourAutomobiliste.mapCorrespondanceNoeudLabel.get(i).isParcouru_automobiliste())
				tasNoeudEnCommun.insert(algoParcourPieton.mapCorrespondanceNoeudLabel.get(i));
			}
		
		// on lance l'algorithme Dijkstra "un vers tous" pour le noeud destination
		PccDestination algoParcourDestination = new PccDestination(graphe, numDestination, tasNoeudEnCommun);
		algoParcourDestination.run();
		// on recupère le meilleur point de rencontre
		Label bestPoint = algoParcourDestination.tasNoeudEnCommun.findMin();
		// on obtient son id
		int id_bestPoint = bestPoint.getId_sommetCourant() ;
		
		
		/**
		 * Les trois fonctions au dessous ne sont pas nécessaires 
		 */
		
		// on lance Dijkstra "un vers un" entre le piéton et le meilleur point de rencontre
		Pcc pietonPcc = new Pcc(graphe, numPieton , id_bestPoint, 0, 1);
		pietonPcc.run();
		pietonPcc.construirePlusCourtChemin();
		//pietonPcc.afficherPlusCourtChemin();
		
		// on lance Dijkstra "un vers un" entre l'automobiliste et le meilleur point de rencontre
		Pcc automobilistePcc = new Pcc(graphe, numAutomobiliste , id_bestPoint, 0, 1);
		automobilistePcc.run();
		automobilistePcc.construirePlusCourtChemin();
		//automobilistePcc.afficherPlusCourtChemin();
		
		// enfin, on lance Dijkstra "un vers un" entre le meilleur point de rencontre et la destination
		Pcc covoiturePcc = new Pcc(graphe, id_bestPoint , numDestination, 0, 1);
		covoiturePcc.run();
		covoiturePcc.construirePlusCourtChemin();
		covoiturePcc.afficherPlusCourtChemin();
	}
	
	public void run() throws IOException {
	}

}