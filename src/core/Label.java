package core;

/**
 * La classe Label qui gère à la fois l'algorithme de Dijkstra, A* et les problèmes de covoiturage.
 * On considère ici que label en algorithme de Dijkstra, 
 * est identique à label en A*, mais avec un coût d'estimation toujours valant 0.
 */

public class Label implements Comparable<Label> {
	
	// un booléen valant vrai si le sommet est définitivement fixé par l'algorithme
	private boolean marquage;
	// un booléen valant vrai si le sommet est parcouru par le piéton
	private boolean parcouru_pieton = false;
	// un booléen valant vrai si le sommet est parcouru par l'automobiliste
	private boolean parcouru_automobiliste = false;
	// un booléen valant vrai si le sommet est parcouru par destination
	private boolean parcouru_destination = false;
	// le numéro du sommet qui est associé à ce label
	private int id_sommetCourant;
	// le numéro du sommet précédent dans la recherche de plus court chemin
	private int id_sommetPere;
	// le coût en distance ou en temps, entre le sommet courant et le sommet origine 
	private double coutCourant;
	// le coût estimation en distance ou en temps, entre le sommet courant et le sommet destinataire
	private double coutEstimation;
	// le coût entre destination et le noeud courant, utilisé pour les problèmes de covoiturage 
	private double coutDestination;
	
	/**
	 * constructeurs 
	 */
	// par défault un label : pas de père, coût infini, non marqué
	public Label(int id_sommetCourant){
		this.id_sommetCourant = id_sommetCourant;
		this.id_sommetPere = -1;
		this.coutCourant = Double.MAX_VALUE;
		this.marquage = false;
	}
	
	// label personalisé, utilisé lors de la création de label sommet origine
	public Label(int id_sommetCourant, int id_sommetPere, double coutCourant, boolean marquage){
		this.id_sommetCourant = id_sommetCourant;
		this.id_sommetPere = id_sommetPere;
		this.coutCourant = coutCourant;
		this.marquage = marquage;
	}
	
	/**
	 * La méthode compareTo() sert à comparer "cet objet" avec l'objet spécifique passé en entrée afin de les ordonner
	 * Le retour de cette fonction est un entier négative, zéro, ou positif, selon les cas où "cet objet" est 
	 * inférieur, égale, ou supérieur à l'objet spécifique passé en entrée.
	 * 
	 * Pour algorithme de A Star, en cas d'égalité, on considèrera l'objet ayant le plus petit coût d'estimation 
	 * Pour algorithme de Dijkstra, le coût d'estimation est toujours 0
	 * Le coût de destination est utilisé pour problèmes de covoiturage, valant 0 en cas de Dijkstra Standard et A*
	 */
	public int compareTo(Label l) {
		int compareResult = 0 ;
		if (this.coutCourant + this.coutEstimation + this.coutDestination < 
				l.getCoutCourant() + l.getCoutEstimation() + l.getCoutDestination())
			compareResult = -1;
		else if (this.coutCourant + this.coutEstimation + + this.coutDestination >
				l.getCoutCourant() + l.getCoutEstimation() + l.getCoutDestination())
			compareResult = 1;
		else {
			if(this.getCoutEstimation() < l.getCoutEstimation())
				compareResult =-1;
			if(this.getCoutEstimation() > l.getCoutEstimation())
				compareResult = 1;
			if(this.getCoutEstimation() == l.getCoutEstimation())
				compareResult = 0;
		}
		return compareResult ;
	}
	
	/**
	 * getters & setters
	 */
	public boolean isMarque() {
		return marquage;
	}
	
	public double getCoutCourant() {
		return coutCourant;
	}

	public void setCoutCourant(double coutCourant) {
		this.coutCourant = coutCourant;
	}

	public void setMarquage(boolean marquage) {
		this.marquage = marquage;
	}
	
	// deux fonctions qui indiquent si le sommet est parcouru par le piéton / automobiliste
	
	public boolean isParcouru_pieton() {
		return parcouru_pieton;
	}

	public boolean isParcouru_automobiliste() {
		return parcouru_automobiliste;
	}
	
	// deux fonctions qui marque le sommet comme "parcouru" par le piéton / automobiliste
	
	public void setParcouru_pieton(boolean parcouru_pieton) {
		this.parcouru_pieton = parcouru_pieton;
	}

	public void setParcouru_automobiliste(boolean parcouru_automobiliste) {
		this.parcouru_automobiliste = parcouru_automobiliste;
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
	
	public double getCoutEstimation() {
		return coutEstimation;
	}

	public void setCoutEstimation(double coutEstimation) {
		this.coutEstimation = coutEstimation;
	}

	public double getCoutDestination() {
		return coutDestination;
	}

	public void setCoutDestination(double coutDestination) {
		this.coutDestination = coutDestination;
	}

	public boolean isParcouru_destination() {
		return parcouru_destination;
	}

	public void setParcouru_destination(boolean parcouru_destination) {
		this.parcouru_destination = parcouru_destination;
	}
}
