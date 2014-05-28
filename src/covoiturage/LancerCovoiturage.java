package covoiturage;

import core.Algo;
import core.Graphe;
import core.Label;
import base.Readarg;
import java.io.IOException;
import java.util.ArrayList;

public class LancerCovoiturage extends Algo {
	
	private int numPieton ;
	private int numAutomobiliste ;
	private int numDestination ;
	
	public LancerCovoiturage(Graphe gr, Readarg readarg){
		super(gr);
		graphe = gr ;
		this.numPieton = readarg.lireInt("Le numéro de noeud d'origine du piéton?");
		this.numAutomobiliste = readarg.lireInt("Le numéro de noeud d'origine de l'automobiliste?");
		this.numDestination = readarg.lireInt("Le numéro de noeud de destination?");
		// lancer l'algo covoiturage
		lancerAlgoCovoiturage();
	}
	
	public void lancerAlgoCovoiturage()
	{
		// lancer l'algorithme Dijkstra un vers tous pour piéton
		PccPietonAutomobiliste algoParcourPieton = new PccPietonAutomobiliste(graphe, numPieton, 0);
		algoParcourPieton.run();
		// lancer l'algorithme Dijkstra un vers tous pour automobiliste
		PccPietonAutomobiliste algoParcourAutomobiliste = new PccPietonAutomobiliste(graphe, numAutomobiliste, 1);
		algoParcourAutomobiliste.run();
		// on cherche tous les noeuds dans le graphe qui sont à la fois parcouru par piéton et automobiliste
		for(int i =0;i<this.graphe.getListeNoeuds().size();i++){
			if(algoParcourPieton.mapCorrespondanceNoeudLabel.get(i).isParcouru_pieton() && 
					algoParcourAutomobiliste.mapCorrespondanceNoeudLabel.get(i).isParcouru_automobiliste())
				listeNoeudCommun.add(algoParcourPieton.mapCorrespondanceNoeudLabel.get(i));
		}

		// lancer l'algorithme Dijkstra un vers tous pour destination
		PccDestination algoParcourDestination = new PccDestination(graphe, numDestination);
		algoParcourDestination.run();
		
		for (int j =0; j < this.graphe.getListeNoeuds().size() ; j++){
			if (algoParcourPieton.mapCorrespondanceNoeudLabel.get(j).isParcouru_pieton() && 
					algoParcourAutomobiliste.mapCorrespondanceNoeudLabel.get(j).isParcouru_automobiliste() &&
						algoParcourDestination.mapCorrespondanceNoeudLabel.get(j).isParcouru_destination())
			}
		}

	public void run() throws IOException {
	}

}