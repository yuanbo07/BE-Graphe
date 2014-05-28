package core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import base.Dessin;

/**
 *   Classe représentant un chemin
 */

public class Chemin {
	
	// variables qui représentent un chemin
	private int numCarte;
	private int nbNoeud;
	private Noeud noeudDepart;
	private Noeud noeudDestination;
	private int zoneDepart;
	private int zoneDestination;
	private ArrayList<Noeud> listeNoeudChemin= new ArrayList<Noeud>();
	
	// variables qui représentent les différents coûts associés à un chemin
	private double coutEnTempsChemin = 0;
	private double distanceCoutEnTempsChemin = 0;
	private double coutEnDistanceChemin = 0;
	private double tempsCoutEnDistanceChemin = 0;

    /**
     * constructeur
     */
	public Chemin(int numCarte, int nbNoeud, Noeud noeudDepart, Noeud noeudDestination, int zoneDepart, int zoneDestination){
		this.numCarte = numCarte;
		this.nbNoeud = nbNoeud;
		this.noeudDepart = noeudDepart;
		this.noeudDestination = noeudDestination;
		this.zoneDepart = zoneDepart;
		this.zoneDestination = zoneDestination;
	}
	
	// constructeur par défault pour construire le plus court chemin
	public Chemin(){
	}
	
    /**
     * renvoyer une liste de successeurs qui ont plus d'un chemin entre deux noeuds 
     * c'est le cas dit "Multi-chemin"
     */
	public ArrayList<Successeur> getListeSuccMultiChemin(Noeud noeudCourant, Noeud noeudSuivant){
		ArrayList<Successeur> listeSuccMultiChemin = new ArrayList<Successeur>();
		for (Successeur succ : noeudCourant.getListeSuccesseur()){
			if(succ.getNoeudDestination().getId_noeud() == noeudSuivant.getId_noeud())
				listeSuccMultiChemin.add(succ);
		}
		return listeSuccMultiChemin;
	}
	
    /**
     * on cherche l'arête la plus courte en distance, puis on renvoie ce successeur
     */
	public Successeur getSuccPlusCourtArete(Noeud noeudCourant, Noeud noeudSuivant){
		// coût de la plus courte arête entre deux noeuds successifs
		double coutDistanceArete = 0;
		// liste qui stocke les successeurs à comparer entre eux
		ArrayList<Successeur> listeSuccAComparer = new ArrayList<Successeur>();
		// iterator
		int i = 0;
		// le successeur qui a la plus courte arête, à renvoyer par cette fonction
		Successeur s = new Successeur();
		// on crée une liste "Multi-chemin"
		listeSuccAComparer = getListeSuccMultiChemin(noeudCourant, noeudSuivant);
		// on initialise le coût d'arête avec le coût de première arête qu'on a obtenu
		coutDistanceArete = listeSuccAComparer.get(0).getLongueurArete();
		// on cherche le plus courte arête
		for (;i<listeSuccAComparer.size();i++){
			double areteCourant = listeSuccAComparer.get(i).getLongueurArete();
			if (areteCourant < coutDistanceArete)
				coutDistanceArete = areteCourant;
				s = listeSuccAComparer.get(i);
		}
		return s;
	}

    /**
     * on cherche l'arête la plus courte en temps, puis on renvoie ce successeur
     * le principe est le même avec celui en distance
     */
	public Successeur getSuccPlusCourtTemps(Noeud noeudCourant, Noeud noeudSuivant){
		double coutTempsArete = 0;
		ArrayList<Successeur> listeSuccAComparer = new ArrayList<Successeur>();
		int i = 0;
		Successeur s = new Successeur();
		listeSuccAComparer = getListeSuccMultiChemin(noeudCourant, noeudSuivant);
		coutTempsArete = listeSuccAComparer.get(0).calculTempsArete();
		for (;i<listeSuccAComparer.size();i++){
			double coutTempsCourant = listeSuccAComparer.get(i).calculTempsArete();
			if (coutTempsCourant < coutTempsArete)
				coutTempsArete = coutTempsCourant;
				s = listeSuccAComparer.get(i);
		}
		return s;
	}
	
    /**
     * deux fonctions qui calculent le chemin plus court en distance / en temp, entre noeudDepart et noeudDestination
     * et calcule aussi le temps / distance utilisé dans cette situation, c'est pour traiter les cas de "Multi-chemin"
     */
	public void calculCheminPlusCourtDistance(){
		// les deux noeuds qui vont parcourir tout le chemin
		Noeud noeudCourant;
		Noeud noeudSuivant;
		double coutDistanceArete = 0;
		double coutEnDistanceChemin = 0;
		double coutTempsArete = 0;
		double tempsCoutEnDistanceChemin = 0;
		int i = 0;

		for (;i<listeNoeudChemin.size()-1;i++){
			noeudCourant = listeNoeudChemin.get(i);
			noeudSuivant = listeNoeudChemin.get(i+1);
			// on prend toujours la plus courte arête entre deux noeuds successifs
			coutDistanceArete = getSuccPlusCourtArete(noeudCourant, noeudSuivant).getLongueurArete();
			coutTempsArete = getSuccPlusCourtArete(noeudCourant, noeudSuivant).calculTempsArete();
			coutEnDistanceChemin += coutDistanceArete;
			tempsCoutEnDistanceChemin += coutTempsArete;
		}
		this.coutEnDistanceChemin = coutEnDistanceChemin;
		this.tempsCoutEnDistanceChemin = tempsCoutEnDistanceChemin;
	}
	
	public void calculCheminPlusCourtTemps(){
		// les deux noeuds qui vont parcourir tout le chemin
		Noeud noeudCourant;
		Noeud noeudSuivant;
		double coutTempsArete = 0;
		double coutEnTempsChemin = 0;
		double coutDistanceArete = 0;
		double distanceCoutEnTempsChemin = 0;
		int i = 0;

		for (i=0;i<listeNoeudChemin.size()-1;i++){
			noeudCourant = listeNoeudChemin.get(i);
			noeudSuivant = listeNoeudChemin.get(i+1);
			// on prend toujours le plus court temps entre deux noeuds successifs
			coutTempsArete = getSuccPlusCourtTemps(noeudCourant, noeudSuivant).calculTempsArete();
			coutDistanceArete = getSuccPlusCourtTemps(noeudCourant, noeudSuivant).getLongueurArete();
			coutEnTempsChemin += coutTempsArete;
			distanceCoutEnTempsChemin += coutDistanceArete;
		}
		this.coutEnTempsChemin = coutEnTempsChemin;
		this.distanceCoutEnTempsChemin = distanceCoutEnTempsChemin;
	}

    /**
	 * fonction qui met en forme la distance en unité "kilomètre", puis renvoie le String
	 * paramètre en entrée est en "mètre"
     */
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
	
    /**
	 * fonction qui met en forme le temps en unité "minute", puis renvoie le String
	 * paramètre en entrée est en "second"
     */
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
	
    /**
	 * ajouter un noeud dans le chemin
     */
	public void addNoeud(Noeud n){
		this.listeNoeudChemin.add(n);
		nbNoeud ++;
	}
	
    /**
	 * renverser l'ordre des noeuds dans le chemin
     */
	public void renverserChemin(){
		Collections.reverse(listeNoeudChemin);
	}
	
    /**
	 * dessiner le chemin dans la carte graphique
	 * drawLine(long1, lat1, long2, lat2)
     */
	public void dessinerChemin(Dessin d){
    	Random rand = new Random();
    	float r = rand.nextFloat();
    	float g = rand.nextFloat();
    	float b = rand.nextFloat();
    	Color randomColor = new Color(r, g, b);
		d.setColor(randomColor);
		d.setWidth(5);
		for (int i = 0 ; i< this.listeNoeudChemin.size()-1 ; i++){
			float longitudeCourant = this.listeNoeudChemin.get(i).getLongitude();
			float latitudeCourant = this.listeNoeudChemin.get(i).getLatitude();
			float longitudeSuivant = this.listeNoeudChemin.get(i+1).getLongitude();
			float latitudeSuivant = this.listeNoeudChemin.get(i+1).getLatitude();
			d.drawLine(longitudeCourant, latitudeCourant, longitudeSuivant, latitudeSuivant);
		}
	}

    /**
     * ces deux fonctions sont utilisées pour afficher le coût en distance ou en temps
     */
	public void affichageCoutEnDistance(){
    	System.out.println();
	    System.out.println("Coût en distance :");
    	System.out.println();
	    System.out.println("La distance du chemin : " + this.getCoutEnDistanceChemin());
	    System.out.println("Le temps pour parcourir ce chemin dans ce cas : " + this.getTempsCoutEnDistanceChemin());
    	System.out.println();
	}
	
	public void affichageCoutEnTemps(){
    	System.out.println();
	    System.out.println("Coût en temps :");
    	System.out.println();
	    System.out.println("Le temps du chemin : " + this.getCoutEnTempsChemin());
	    System.out.println("La distance du chemin dans ce cas : " + this.getDistanceCoutEnTempsChemin());
    	System.out.println();
	}
	
    /**
     * fonction qui affiche des informations sur un chemin
     */
	public void affichageInformationChemin(){
		
		// exécuter
		calculCheminPlusCourtTemps();
		calculCheminPlusCourtDistance();
		
		// affichage
	    System.out.println("****** INFORMATION DU CHEMIN ******");
	    System.out.println();
	    System.out.println("Ce chemin contient " + getNbNoeud() + " noeuds.");
	    System.out.println("Ce chemin contient " + getListeSuccMultiChemin(noeudDepart, noeudDestination).size() + " multi-chemin.");
	    affichageCoutEnTemps();
	    affichageCoutEnDistance();
	    System.out.println();
	}

    /**
     * les quatre fonctions "getters" qui retournent résultats en bonne unité (String)
     */

	public String getCoutEnTempsChemin() {
		calculCheminPlusCourtTemps();
		return tempsEnMinToString(coutEnTempsChemin);
	}

	public String getDistanceCoutEnTempsChemin() {
		calculCheminPlusCourtTemps();
		return distanceEnkmToString(distanceCoutEnTempsChemin);
	}

	public String getCoutEnDistanceChemin() {
		calculCheminPlusCourtDistance();
		return distanceEnkmToString(coutEnDistanceChemin);
	}

	public String getTempsCoutEnDistanceChemin() {
		calculCheminPlusCourtDistance();
		return tempsEnMinToString(tempsCoutEnDistanceChemin);
	}
	
    /**
     * les quatre fonctions "getters" qui retournent résultats en mode "test"
     * le coût en distance est en "mètre", et coût en temps en "seconde"
     */

	public double getCoutEnTempsCheminModeTest() {
		calculCheminPlusCourtTemps();
		return (double)((int)(coutEnTempsChemin*100))/100;
	}

	public double getDistanceCoutEnTempsCheminModeTest() {
		calculCheminPlusCourtTemps();
		return distanceCoutEnTempsChemin;
	}

	public double getCoutEnDistanceCheminModeTest() {
		calculCheminPlusCourtDistance();
		return coutEnDistanceChemin;
	}

	public double getTempsCoutEnDistanceCheminModeTest() {
		calculCheminPlusCourtDistance();
		return tempsCoutEnDistanceChemin;
	}
	
    /**
     * getters & setters
     */
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
	
	public void setCoutEnTempsChemin(double coutEnTempsChemin) {
		this.coutEnTempsChemin = coutEnTempsChemin;
	}

	public void setDistanceCoutEnTempsChemin(double distanceCoutEnTempsChemin) {
		this.distanceCoutEnTempsChemin = distanceCoutEnTempsChemin;
	}

	public void setCoutEnDistanceChemin(double coutEnDistanceChemin) {
		this.coutEnDistanceChemin = coutEnDistanceChemin;
	}

	public void setTempsCoutEnDistanceChemin(double tempsCoutEnDistanceChemin) {
		this.tempsCoutEnDistanceChemin = tempsCoutEnDistanceChemin;
	}
}
