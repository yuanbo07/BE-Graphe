package core;

/**
 * La classe Label qui gère à la fois l'algorithme de Dijkstra.
 * On considère ici que label en algorithme de Dijkstra, 
 * est identique à label en A*, mais avec un coût d'estimation toujours valant 0.
 */

public class Label implements Comparable<Label> {
	
	// un booléen valant vrai si le sommet est définitivement fixé par l'algorithme
	private boolean marquage;
	// le numéro du sommet qui est associé à ce label
	private int id_sommetCourant;
	// le numéro du sommet précédent dans la recherche de plus court chemin
	private int id_sommetPere;
	// le coût en distance ou en temps, entre le sommet courant et le sommet origine 
	private double coutCourant;
	// le coût estimation en distance ou en temps, entre le sommet courant et le sommet destinataire
	private double coutEstimation = 0;
	// la somme de coutCourant et coutEstimation
	private double coutCourantAvecEstimation = 0;
	
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
	 * Pour algorithme de Dijkstra, cette dernière partie ne sert qu'à retourner 0 (absence de coût d'estimation)
	 */
	public int compareTo(Label l) {
		
		int compareResult = 0;
		this.coutCourantAvecEstimation = this.coutCourant + this.coutEstimation ;
		
		if(this.getCoutCourantAvecEstimation() < l.getCoutCourantAvecEstimation())
			compareResult = -1;
		if(this.getCoutCourantAvecEstimation() > l.getCoutCourantAvecEstimation())
			compareResult = 1;
		// si le coût courant avec estimation est le même, on compare avec le coût estimation
		if(this.getCoutCourantAvecEstimation() == l.getCoutCourantAvecEstimation()){
			if(this.getCoutEstimation() < l.getCoutEstimation())
				compareResult =-1;
			if(this.getCoutEstimation() > l.getCoutEstimation())
				compareResult = 1;
			if(this.getCoutEstimation() == l.getCoutEstimation())
				compareResult = 0;
		}
		return compareResult;
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

	public double getCoutCourantAvecEstimation() {
		return coutCourantAvecEstimation;
	}

	public void setCoutEstimation(double coutEstimation) {
		this.coutEstimation = coutEstimation;
	}
}
