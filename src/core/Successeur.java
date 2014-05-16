package core;

import base.Descripteur;

public class Successeur {

	private int nb_segment;
	private Noeud noeudDestination;
	private double longueurArrete;
	private Descripteur descripteur;
	private int succ_zone;
	
	/**
	 * constructeurs
	 */
	
	public Successeur(int nb_segment, Noeud noeudDestination, int longueurArrete, Descripteur descripteur, int succ_zone){
		super();
		this.nb_segment = nb_segment;
		this.noeudDestination = noeudDestination;
		this.longueurArrete = longueurArrete;
		this.descripteur = descripteur;
		this.succ_zone = succ_zone;
	}

	public Successeur(){
	}
	
	/**
	 * fonction qui calcule le temps utilisé pour une arrête
	 * le temps renvoyé est en unité "seconde"
	 */
	public double calculTempsArrete(){
		double t = this.longueurArrete * 3600 / (this.descripteur.vitesseMax()*1000);
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
	
	public double getLongueurArrete() {
		return longueurArrete;
	}
	
	public double getTempsArrete() {
		return this.calculTempsArrete();
	}
	
	public void setLongueurArrete(int longueurArrete) {
		this.longueurArrete = longueurArrete;
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
