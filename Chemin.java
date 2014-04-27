package core;

import java.util.ArrayList;

public class Chemin {
	private Noeud noeudDepart;
	private Noeud noeudDestination;
	private ArrayList<Noeud> listeNoeudChemin= new ArrayList<Noeud>();
	private int coutTempsChemin;
	private int coutDistanceChemin;
	
	
	public Chemin(Noeud noeudDepart, Noeud noeudDestination){
		this.noeudDepart = noeudDepart;
		this.noeudDestination = noeudDestination;
	}
	
	
	//calculer le cout en distance d'un chemin
	
	public void calculCoutDistanceChemin(Noeud noeudDepart, Noeud noeudDestination){
		
		noeudDepart.getListeSuccesseur().get(index)
	}
	
	
	//calculer le cout en temps d'un chemin
	
	public void calculCoutTempsChemin(Noeud noeudDepart, Noeud noeudDestination){
		
		noeudDepart.getListeSuccesseur().get(index)
	}

}
