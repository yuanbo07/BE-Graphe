package covoiturage;

/**
 * La classe LabelCovoiturage
 * 
 * Pour améliorer la performance, on simplifie le label ici par un quadruplet :
 * (numéro du sommet, coût en temps du piéton, coût en temps de l'automobiliste, coût en temps de recherche destination)
 */

public class LabelCovoiturage implements Comparable<LabelCovoiturage> {
	
	private int id_sommet ;
	private double coutPieton = 0;
	private double coutAutomobiliste = 0;
	private double coutDestination = 0;
	
	/**
	 * constructeurs 
	 */
	public LabelCovoiturage(int id_sommet, double coutPieton, double coutAutomobiliste){
		this.id_sommet = id_sommet;
		this.coutPieton = coutPieton;
		this.coutAutomobiliste = coutAutomobiliste;
	}
	
	// on prend le max entre le coût du piéton et le coût de l'automobiliste (possibilité d'attente)
	public int compareTo(LabelCovoiturage l) {
		int compareResult = 0 ;
		if (Math.max(this.coutPieton, this.coutAutomobiliste) +  + this.coutDestination < 
				Math.max(l.getCoutPieton(),l.getCoutAutomobiliste()) + l.getCoutDestination())
			compareResult = -1;
		else if (Math.max(this.coutPieton,this.coutAutomobiliste) + this.coutDestination > 
			Math.max(l.getCoutPieton(), l.getCoutAutomobiliste()) + l.getCoutDestination())
			compareResult = 1;
		else
			compareResult = 0;
		return compareResult ;
	}

	/**
	 * getters & setters
	 */
	
	public int getId_sommet() {
		return id_sommet;
	}

	public double getCoutPieton() {
		return coutPieton;
	}

	public double getCoutAutomobiliste() {
		return coutAutomobiliste;
	}

	public double getCoutDestination() {
		return coutDestination;
	}

	public void setId_sommet(int id_sommet) {
		this.id_sommet = id_sommet;
	}

	public void setCoutPieton(double coutPieton) {
		this.coutPieton = coutPieton;
	}

	public void setCoutAutomobiliste(double coutAutomobiliste) {
		this.coutAutomobiliste = coutAutomobiliste;
	}

	public void setCoutDestination(double coutDestination) {
		this.coutDestination = coutDestination;
	}
}
