package core;

public class Label {
	
	
	/* Pour mettre en oeuvre l'algorithme de Dijkstra, on associe à chaque sommet un label composé d'un quadruplet 
	 * (marquage, cout, pere, sommet courant)
	 * 
	 * marquage : un booléen (valant vrai si le sommet est définitivement fixé par l'algorithme)
	 * cout : la valeur courante du plus court chemin depuis l'origine vers le sommet
	 * pere : correspond au sommet précédent sur le chemin correspondant au plus court chemin courant
	 * Le sommet courant est le sommet associé à ce label (sommet ou numéro de sommet)
	 */
	
	private Noeud sommetCourant;
	private Noeud sommetPere;
	private double coutCourant;
	private boolean marquage;
	
	public Label(Noeud sommetCourant, Noeud sommetPere, double coutCourant){
		this.sommetCourant = sommetCourant;
		this.sommetPere = sommetPere;
		this.coutCourant = coutCourant;
		this.marquage = false;
	}

	
	// getters & setters
	
	public Noeud getSommetCourant() {
		return sommetCourant;
	}

	public void setSommetCourant(Noeud sommetCourant) {
		this.sommetCourant = sommetCourant;
	}

	public Noeud getSommetPere() {
		return sommetPere;
	}

	public void setSommetPere(Noeud sommetPere) {
		this.sommetPere = sommetPere;
	}

	public double getCoutCourant() {
		return coutCourant;
	}

	public void setCoutCourant(double coutCourant) {
		this.coutCourant = coutCourant;
	}

	public boolean isMarquage() {
		return marquage;
	}

	public void setMarquage(boolean marquage) {
		this.marquage = marquage;
	}

}
