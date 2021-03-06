package core;

import java.io.IOException;
import base.Readarg;

/**
 * La classe simpleTest est pour le but de lancer l'algorithme pour tous les chemins possibles entre sommets dans un graphe
 * Ce test est utilisé pour tester les cartes de petites tailles
 */

public class SimpleTest extends Algo {
	
	public int choixAlgo;
	public int isEnTemps;
	
	/**
	 * constructeurs
	 */
	public SimpleTest(Graphe gr, Readarg readarg){
		super(gr) ;
		this.choixAlgo = readarg.lireInt ("Lancer le test en Dijkstra Standard(0) ou Dijkstra A Star(1) ?");
		this.isEnTemps = readarg.lireInt ("Lancer le test en distance(0) ou temps(1) ?");
		runSimpleTest() ;
	}
	
	public void runSimpleTest(){
		// pour tous les noeuds dans une carte, on lance l'algorithme Pcc
		int size = Algo.graphe.getListeNoeuds().size() ;
		System.out.println("size is : " + size);
		for(int i=0; i < size ; i++) {
			for(int j=0; j < size ; j++) {
				// si les deux noeuds sont bien dans la carte
				if(graphe.getListeNoeuds().get(i) != null && graphe.getListeNoeuds().get(j) != null){
					Pcc test = new Pcc(graphe, i, j, choixAlgo, isEnTemps);
					test.run();
				}
			}
		}
	}
	
	public void run() throws IOException {
	}
}