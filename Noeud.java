package core;

import java.util.ArrayList;

public class Noeud {
	private float longitude;
	private float latitude;
	private int id_noeud;
	private ArrayList<Successeur> listeSuccesseur = new ArrayList<Successeur>();
	
	
	// getter & setter
	
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
	
}

