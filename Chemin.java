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
	private double coutEnTempsChemin;
	private double distanceCoutEnTempsChemin;
	private double coutEnDistanceChemin;
	private double tempsCoutEnDistanceChemin;

	// Constructeur
	
	public Chemin(int numCarte, int nbNoeud, Noeud noeudDepart, Noeud noeudDestination, int zoneDepart, int zoneDestination){
		this.numCarte = numCarte;
		this.nbNoeud = nbNoeud;
		this.noeudDepart = noeudDepart;
		this.noeudDestination = noeudDestination;
		this.zoneDepart = zoneDepart;
		this.zoneDestination = zoneDestination;
	}
	
	// renvoyer une liste de tous successeurs qui définient plus d'un chemin entre deux noeuds (multi-chemin)
	
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
		double coutDistanceArrete = 0;
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
			double arreteCourant = listeSuccAComparer.get(i).getLongueurArrete();
			if (arreteCourant < coutDistanceArrete)
				coutDistanceArrete = arreteCourant;
				s = listeSuccAComparer.get(i);
		}
		return s;
	}

	// On cherche l'arrête plus courte en temps, puis renvoyer ce successeur
	
	public Successeur getSuccPlusCourtTemps(Noeud noeudCourant, Noeud noeudSuivant){
		// coût de plus courte arrête entre deux noeuds successifs
		double coutTempsArrete = 0;
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
			double coutTempsCourant = listeSuccAComparer.get(i).calculTempsArrete();
			if (coutTempsCourant < coutTempsArrete)
				coutTempsArrete = coutTempsCourant;
				s = listeSuccAComparer.get(i);
		}
		return s;
	}
	
	// pour obtenir un chemin plus court en distance entre noeudDepart et noeudDestination
	
	public void calculCheminPlusCourtDistance(){
		// les deux noeuds qui vont parcourir tout le chemin
		Noeud noeudCourant;
		Noeud noeudSuivant;
		double coutDistanceArrete = 0;
		double coutTempsArrete = 0;
		double coutEnDistanceChemin = 0;
		double tempsCoutEnDistanceChemin = 0;
		int i = 0;

		// on prend toujours le plus courte arrête entre deux noeuds successifs
		for (i=0;i<listeNoeudChemin.size()-1;i++){
			noeudCourant = listeNoeudChemin.get(i);
			noeudSuivant = listeNoeudChemin.get(i+1);
			coutDistanceArrete = getSuccPlusCourtArrete(noeudCourant, noeudSuivant).getLongueurArrete();
			coutTempsArrete = getSuccPlusCourtArrete(noeudCourant, noeudSuivant).calculTempsArrete();
			coutEnDistanceChemin += coutDistanceArrete;
			tempsCoutEnDistanceChemin += coutTempsArrete;
		}
		this.coutEnDistanceChemin = coutEnDistanceChemin;
		this.tempsCoutEnDistanceChemin = tempsCoutEnDistanceChemin;
	}
	
	
	// pour obtenir un chemin plus court en temps entre noeudDepart et noeudDestination
	
	public void calculCheminPlusCourtTemps(){
		// les deux noeuds qui vont parcourir tout le chemin
		Noeud noeudCourant;
		Noeud noeudSuivant;
		double coutDistanceArrete = 0;
		double coutTempsArrete = 0;
		double coutEnTempsChemin = 0;
		double distanceCoutEnTempsChemin = 0;
		int i = 0;

		// on prend toujours le plus court temps entre deux noeuds successifs
		for (i=0;i<listeNoeudChemin.size()-1;i++){
			noeudCourant = listeNoeudChemin.get(i);
			noeudSuivant = listeNoeudChemin.get(i+1);
			coutTempsArrete = getSuccPlusCourtTemps(noeudCourant, noeudSuivant).calculTempsArrete();
			coutDistanceArrete = getSuccPlusCourtTemps(noeudCourant, noeudSuivant).getLongueurArrete();
			coutEnTempsChemin += coutTempsArrete;
			distanceCoutEnTempsChemin += coutDistanceArrete;
		}
		this.coutEnTempsChemin = coutEnTempsChemin;
		this.distanceCoutEnTempsChemin = distanceCoutEnTempsChemin;
	}

	// mettre en forme la distance en km et renvoyer le String
	
	public String distanceEnkmToString(double distance) {

		String s = null;
		double km = 0;
		
		if(distance >= 1000) {
			km = (double)distance/1000;
			s = km + " km" ;
		}
		else{
			s = distance + " m";
		}
		return s;
	}
	
	
	// mettre en forme le temps en min et renvoyer le String
	
	public String tempsEnMinToString(double temps) {

		int h;
		int m;
		int s;
		
		String stringTemps = null;

		if(temps > 3600){
			h = (int)(temps/3600);
			m = (int)(temps/60);
			s = (int) (temps - 3600 * h - 60 * m) ;
			stringTemps = h + " h" + m + " min" + s + " s";
		};
		if(temps >= 60) {
			m = (int)(temps/60);
			s = (int) (temps - 60 * m) ;
			stringTemps = m + " min" + s + " s";
		};
		if(temps < 60)
		{
			stringTemps = temps + " s";
		}
		return stringTemps;
	}
	
	// ajouter un noeud dans ce chemin
	
	public void addNoeud(Noeud n){
		this.listeNoeudChemin.add(n);
	}
	
	
	// cette fonction gère affichage
	
	public void affichageInformationChemin(){
		
		// exécuter & conversion des résultats
		calculCheminPlusCourtTemps();
		calculCheminPlusCourtDistance();

	    System.out.println("******INFORMATION DU CHEMIN************");
	    System.out.println();
	    System.out.println("Ce chemin contient " + getNbNoeud() + " noeuds.");
	    System.out.println("Ce chemin contient " + getListeSuccMultiChemin(noeudDepart, noeudDestination).size() + " multi-chemin.");
	    System.out.println("Cas 1 : Le coût d'un chemin en distance :");
	    System.out.println("Distance du chemin : " + distanceEnkmToString(coutEnDistanceChemin));
	    System.out.println("Le temps dans ce cas : " + tempsEnMinToString(tempsCoutEnDistanceChemin));
	    System.out.println();
	    System.out.println("Cas 2 : Le coût d'un chemin en temps:");
	    System.out.println("Temps du chemin : " + tempsEnMinToString(coutEnTempsChemin));
	    System.out.println("La distance dans ce cas : " + distanceEnkmToString(distanceCoutEnTempsChemin));
	    System.out.println();
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

	public double getCoutEnTempsChemin() {
		return coutEnTempsChemin;
	}

	public void setCoutEnTempsChemin(double coutEnTempsChemin) {
		this.coutEnTempsChemin = coutEnTempsChemin;
	}

	public double getDistanceCoutEnTempsChemin() {
		return distanceCoutEnTempsChemin;
	}

	public void setDistanceCoutEnTempsChemin(int distanceCoutEnTempsChemin) {
		this.distanceCoutEnTempsChemin = distanceCoutEnTempsChemin;
	}

	public double getCoutEnDistanceChemin() {
		return coutEnDistanceChemin;
	}

	public void setCoutEnDistanceChemin(int coutEnDistanceChemin) {
		this.coutEnDistanceChemin = coutEnDistanceChemin;
	}

	public double getTempsCoutEnDistanceChemin() {
		return tempsCoutEnDistanceChemin;
	}

	public void setTempsCoutEnDistanceChemin(double tempsCoutEnDistanceChemin) {
		this.tempsCoutEnDistanceChemin = tempsCoutEnDistanceChemin;
	}
}
