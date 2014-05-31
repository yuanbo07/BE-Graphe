package Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import core.* ;


public class RandomTable {
	
	public Graphe graphe ;
	public int isEnTemps;
	public int choixAlgo;
	
	public RandomTable(int size, int choixAlgo, int isEnTemps, Graphe gr) {
		this.size = size;
		this.graphe = gr;
		this.choixAlgo = choixAlgo ;
		this.isEnTemps = isEnTemps ;
	}
	public void generate() {
		myList = new ArrayList<Element>();
		Random randomGenerator = new Random();
		for (int i =0; i < size; ++i){
			int k = randomGenerator.nextInt(size);
			myList.add(new Element(graphe, choixAlgo, isEnTemps, k));
		}
	}
	public Element sortAndReturnFirst() {
		Collections.sort(myList);
		return myList.get(0);		
	}
	@Override
	public String toString() {

		return myList.toString();
	}
	private 
	int size; 
	List<Element> myList ;
}