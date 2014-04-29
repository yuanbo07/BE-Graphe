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
	
	
	// renvoyer une liste de successeurs qui définient plus d'un chemin entre deux noeuds (multi-chemin)
	public ArrayList<Successeur> getListeSuccMultiChemin(Noeud noeudCourant, Noeud noeudSuivant){
		ArrayList<Successeur> listeSuccMultiChemin = new ArrayList<Successeur>();
		for (Successeur succ : noeudCourant.getListeSuccesseur()){
			if(succ.getNoeudDestination().getId_noeud() == noeudSuivant.getId_noeud())
				listeSuccMultiChemin.add(succ);
		}
		return listeSuccMultiChemin;
	}
	
	
	// On cherche l'arrête la plus courte en distance, puis renvoyer ce successeur
	public Successeur getSuccPlusCourtArrete(Noeud noeudCourant, Noeud noeudSuivant){
		// coût de plus courte arrête entre deux noeuds successifs
		int coutDistanceArrete = 0;
		// liste qui stocke les successeurs à comparer
		ArrayList<Successeur> listeSuccAComparer = new ArrayList<Successeur>();
		// iterator
		int i;
		// successeur à renvoyer, qui a la plus courte arrête
		Successeur s = new Successeur();
		
		listeSuccAComparer = getListeSuccMultiChemin(noeudCourant, noeudSuivant);
		// on initialise cout d'arrete le cout de premiere arrete qu'on a (reste à retoucher)
		coutDistanceArrete = listeSuccAComparer.get(0).getLongueurArrete();
		for (i=0;i<listeSuccAComparer.size();i++){
			int arreteCourant = listeSuccAComparer.get(i).getLongueurArrete();
			if (arreteCourant < coutDistanceArrete)
				coutDistanceArrete = arreteCourant;
				s = listeSuccAComparer.get(i);
		}
		return s;
	}

	// On cherche l'arrête plus courte en temps, puis renvoyer ce successeur
	public Successeur getSuccPlusCourtTemps(Noeud noeudCourant, Noeud noeudSuivant){
		// coût de plus courte arrête entre deux noeuds successifs
		float coutTempsArrete = 0;
		// liste qui stocke les successeurs à comparer
		ArrayList<Successeur> listeSuccAComparer = new ArrayList<Successeur>();
		// iterator
		int i;
		// successeur à renvoyer, qui a le plus court temps
		Successeur s = new Successeur();
		listeSuccAComparer = getListeSuccMultiChemin(noeudCourant, noeudSuivant);
		
		// on initialise le cout en temps de premiere arrete qu'on a (reste à retoucher)
		coutTempsArrete = listeSuccAComparer.get(0).calculTempsArrete();
		for (i=0;i<listeSuccAComparer.size();i++){
			float coutTempsCorant = listeSuccAComparer.get(i).calculTempsArrete();
			if (coutTempsCorant < coutTempsArrete)
				coutTempsArrete = coutTempsCorant;
				s = listeSuccAComparer.get(i);
		}
		return s;
	}
	
	
	public void addNoeud(Noeud n){
		this.listeNoeudChemin.add(n);
	}
	
	// getters & setters

	public int getNumCarte() {
		return numCarte;
	}

	public void setNumCarte(int numCarte) {
		this.numCarte = numCarte;
	}

	public int getNbNoeud() {
		return nbNoeud;
	}

	public void setNbNoeud(int nbNoeud) {
		this.nbNoeud = nbNoeud;
	}

	public Noeud getNoeudDepart() {
		return noeudDepart;
	}

	public void setNoeudDepart(Noeud noeudDepart) {
		this.noeudDepart = noeudDepart;
	}

	public Noeud getNoeudDestination() {
		return noeudDestination;
	}

	public void setNoeudDestination(Noeud noeudDestination) {
		this.noeudDestination = noeudDestination;
	}

	public int getZoneDepart() {
		return zoneDepart;
	}

	public void setZoneDepart(int zoneDepart) {
		this.zoneDepart = zoneDepart;
	}

	public int getZoneDestination() {
		return zoneDestination;
	}

	public void setZoneDestination(int zoneDestination) {
		this.zoneDestination = zoneDestination;
	}

	public ArrayList<Noeud> getListeNoeudChemin() {
		return listeNoeudChemin;
	}

	public void setListeNoeudChemin(ArrayList<Noeud> listeNoeudChemin) {
		this.listeNoeudChemin = listeNoeudChemin;
	}

	public int getCoutTempsChemin() {
		return coutTempsChemin;
	}

	public void setCoutTempsChemin(int coutTempsChemin) {
		this.coutTempsChemin = coutTempsChemin;
	}

	public int getCoutDistanceChemin() {
		return coutDistanceChemin;
	}

	public void setCoutDistanceChemin(int coutDistanceChemin) {
		this.coutDistanceChemin = coutDistanceChemin;
	}

}
