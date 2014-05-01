package core;

public class Label implements Comparable<Label> {
	
	/*
	 * marquage : un booléen (valant vrai si le sommet est définitivement fixé par l'algorithme)
	 * sommetCourant: le sommet associé à ce label (sommet ou numéro de sommet)
	 * sommetPere : le sommet précédent sur le chemin correspondant au plus court chemin courant
	 * coutCourant : la valeur courante du plus court chemin depuis l'origine vers le sommet
	 */
	
	private int id_sommetCourant;
	private int id_sommetPere;
	private double coutCourant;
	private boolean marquage;
	
	// un label créé par défault : pas de père, coût infini, pas marqué
	
	public Label(int id_sommetCourant){
		this.id_sommetCourant = id_sommetCourant;
		this.id_sommetPere = -1;
		this.coutCourant = Double.MAX_VALUE;
		this.marquage = false;
	}
	
	public Label(int id_sommetCourant, int id_sommetPere, double coutCourant, boolean marquage){
		this.id_sommetCourant = id_sommetCourant;
		this.id_sommetPere = id_sommetPere;
		this.coutCourant = coutCourant;
		this.marquage = marquage;
	}
	


	// Compares this object with the specified object for order. 
	// Returns a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object. 
	
	public int compareTo(Label lb1) {
		if(this.getCoutCourant() == lb1.getCoutCourant())
			return 0;
		else if(this.getCoutCourant() > lb1.getCoutCourant())
			return 1;
		else
			return -1;
	}
	
	// getters & setters
	
	public double getCoutCourant() {
		return coutCourant;
	}

	public void setCoutCourant(double coutCourant) {
		this.coutCourant = coutCourant;
	}

	public boolean isMarque() {
		return marquage;
	}

	public void setMarquage(boolean marquage) {
		this.marquage = marquage;
	}

	public int getId_sommetCourant() {
		return id_sommetCourant;
	}

	public void setId_sommetCourant(int id_sommetCourant) {
		this.id_sommetCourant = id_sommetCourant;
	}

	public int getId_sommetPere() {
		return id_sommetPere;
	}

	public void setId_sommetPere(int id_sommetPere) {
		this.id_sommetPere = id_sommetPere;
	}
}
