package core;

import java.util.ArrayList;

public class Successeur {
	
	private int nb_segment;
	private ArrayList<Segment> listeSegment = new ArrayList<Segment>();
	private Noeud noeudSuivant;
	private Noeud noeudDestination;
	private int longueurRoute;
	
	
	
	
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
	public int getLongueurRoute() {
		return longueurRoute;
	}
	public void setLongueurRoute(int longueurRoute) {
		this.longueurRoute = longueurRoute;
	}
	
	

}
