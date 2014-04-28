package core;

import java.util.ArrayList;

public class Chemin {
	private int numCarte;
	private int nbNoeud;
	private Noeud noeudDepart;
	private Noeud noeudDestination;
	private int zoneDepart;
	private int zoneDestination;
	private ArrayList<Noeud> listeNoeudChemin= new ArrayList<Noeud>();
	private int coutTempsChemin;
	private int coutDistanceChemin;

	// Constructeur
	
	public Chemin(int numCarte, int nbNoeud, Noeud noeudDepart, Noeud noeudDestination, int zoneDepart, int zoneDestination){
		this.numCarte = numCarte;
		this.nbNoeud = nbNoeud;
		this.noeudDepart = noeudDepart;
		this.noeudDestination = noeudDestination;
		this.zoneDepart = zoneDepart;
		this.zoneDestination = zoneDestination;
	}
	
	//calculer le cout en distance d'un chemin
	
	public void calculCoutDistanceChemin(Noeud noeudDepart, Noeud noeudDestination){
		int coutDistance = 0;
		int coutDistanceArrete = 0;
		for (Successeur succ : noeudDepart.getListeSuccesseur())
			if (noeudDepart.getId_noeud() == this.noeudDepart.getId_noeud())
				
	}
	
	
	//calculer le cout en temps d'un chemin
	
	public void calculCoutTempsChemin(Noeud noeudDepart, Noeud noeudDestination){

	}
	
	
	public void addNoeud(Noeud noeud){
		this.listeNoeudChemin.add(noeud);
	}

}
