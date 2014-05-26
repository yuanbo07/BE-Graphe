package core;

import java.util.ArrayList;

/**
 * La classe Noeud représente un noeud dans le graphe.
 */

public class Noeud {
	private int id_noeud;
	private float longitude;
	private float latitude;
	private int nb_successeur;
	private ArrayList<Successeur> listeSuccesseur = new ArrayList<Successeur>();
	
	/**
	 * constructeurs
	 */
	
	public Noeud(int id_noeud, float latitude, float longitude, int nb_successeur){
		this.id_noeud = id_noeud;
		this.latitude = latitude;
		this.longitude = longitude;
		this.nb_successeur = nb_successeur;
	}
	
	public Noeud(){
	}
	
	// fonction pour ajouter un successeur d'un noeud
	public void addSuccesseur(Successeur succ){
		this.listeSuccesseur.add(succ);
	}
	
	/**
	 * getters & setters
	 */
	
	public float getLongitude() {
		return longitude;
	}
	
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
	public float getLatitude() {
		return latitude;
	}
	
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	
	public int getId_noeud() {
		return id_noeud;
	}
	
	public void setId_noeud(int id_noeud) {
		this.id_noeud = id_noeud;
	}
	
	public ArrayList<Successeur> getListeSuccesseur() {
		return listeSuccesseur;
	}
	
	public void setListeSuccesseur(ArrayList<Successeur> listeSuccesseur) {
		this.listeSuccesseur = listeSuccesseur;
	}

	public int getNb_successeur() {
		return nb_successeur;
	}

	public void setNb_successeur(int nb_successeur) {
		this.nb_successeur = nb_successeur;
	}	
}