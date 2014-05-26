package core;

import base.Descripteur;

/**
 *   Classe représentant un successeur (un arc sortant)
 */

public class Successeur {

	private int succ_zone;
	private int nb_segment;
	private Noeud noeudDestination;
	private double longueurArete;
	private Descripteur descripteur;
	
	/**
	 * constructeurs
	 */
	
	public Successeur(int nb_segment, Noeud noeudDestination, int longueurArete, Descripteur descripteur, int succ_zone){
		super();
		this.nb_segment = nb_segment;
		this.noeudDestination = noeudDestination;
		this.longueurArete = longueurArete;
		this.descripteur = descripteur;
		this.succ_zone = succ_zone;
	}

	public Successeur(){
	}
	
	/**
	 * fonction qui calcule le temps utilisé pour une arête, en fonction de sa distance et sa vitesse maximale
	 * le temps renvoyé est en unité "seconde"
	 */
	public double calculTempsArete(){
		double t = this.longueurArete * 3600/ (this.descripteur.vitesseMax()*1000) ;
		return t;
	}
	
	/**
	 * getters & setters
	 */
	
	public int getNb_segment() {
		return nb_segment;
	}
	
	public void setNb_segment(int nb_segment) {
		this.nb_segment = nb_segment;
	}
	
	public Noeud getNoeudDestination() {
		return noeudDestination;
	}
	
	public void setNoeudDestination(Noeud noeudDestination) {
		this.noeudDestination = noeudDestination;
	}
	
	public double getLongueurArete() {
		return longueurArete;
	}
	
	public double getTempsArete() {
		return this.calculTempsArete();
	}
	
	public void setlongueurArete(int longueurArete) {
		this.longueurArete = longueurArete;
	}
	
	public int getNum_zone() {
		return succ_zone;
	}
	
	public void setNum_zone(int num_zone) {
		this.succ_zone = num_zone;
	}
	
	public Descripteur getDescripteur() {
		return descripteur;
	}
	
	public void setDescripteur(Descripteur descripteur) {
		this.descripteur = descripteur;
	}
}
