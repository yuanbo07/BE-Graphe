package Sort;
import core.* ;

public class Element implements Comparable<Element> {
	
	public Graphe graphe ;
	private int value ;
	public int isEnTemps ;
	public int choixAlgo ;

	public Element(Graphe gr, int choixAlgo, int isEnTemps , int value) {
		this.value = value;
		this.graphe = gr ;
		this.isEnTemps = isEnTemps ;
		this.choixAlgo = choixAlgo ;
	}

	// Overriding compareTo
	@Override
	public int compareTo(Element e) {
		// TODO Auto-generated method stub
		if (Constants.printDebug){
			System.out.println("Run Pcc bewteen node "+ this + " and node "+ e); 
		}
		// si les deux "value" (numéro de noeud) sont tous présents dans cette carte
		if(graphe.getListeNoeuds().get(e.value) != null && graphe.getListeNoeuds().get(this.value) != null){
			// on passe la valeur de "cet" élément et la valeur de "e" (ces deux numéro de noeuds) dans l'algorithme de Pcc
			Pcc test = new Pcc(graphe, this.value, e.value, choixAlgo, isEnTemps);
			test.run();
		}
		return this.value - e.value;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}