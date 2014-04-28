package core;

import java.util.ArrayList;

import base.Descripteur;

public class Successeur {
	
	private int nb_segment;
	private ArrayList<Segment> listeSegment = new ArrayList<Segment>();
	private Noeud noeudSuivant;
	private Noeud noeudDestination;
	private int longueurArrete;
	private Descripteur descripteur;
	private int succ_zone;
	
	public Successeur(int succ_zone, Noeud noeudDestination, int num_desc, int longueurArrete, int nb_segment){
		super();
		this.nb_segment = nb_segment;
		this.noeudSuivant = noeudSuivant;
		this.noeudDestination = noeudDestination;
		this.longueurArrete = longueurArrete;
		this.descripteur = descripteur;
		this.succ_zone = succ_zone;
	}


	// getter & setter
	
	public int getNb_segment() {
		return nb_segment;
	}
	public void setNb_segment(int nb_segment) {
		this.nb_segment = nb_segment;
	}
	public ArrayList<Segment> getListeSegment() {
		return listeSegment;
	}
	public void setListeSegment(ArrayList<Segment> listeSegment) {
		this.listeSegment = listeSegment;
	}
	public Noeud getNoeudSuivant() {
		return noeudSuivant;
	}
	public void setNoeudSuivant(Noeud noeudSuivant) {
		this.noeudSuivant = noeudSuivant;
	}
	public Noeud getNoeudDestination() {
		return noeudDestination;
	}
	public void setNoeudDestination(Noeud noeudDestination) {
		this.noeudDestination = noeudDestination;
	}
	public int getLongueurArrete() {
		return longueurArrete;
	}
	public void setLongueurArrete(int longueurArrete) {
		this.longueurArrete = longueurArrete;
	}
	
	public int getNum_zone() {
		return num_zone;
	}
	public void setNum_zone(int num_zone) {
		this.num_zone = num_zone;
	}
	public Descripteur getDescripteur() {
		return descripteur;
	}
	public void setDescripteur(Descripteur descripteur) {
		this.descripteur = descripteur;
	}
	
	

}
