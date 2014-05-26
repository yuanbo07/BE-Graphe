package Sort;
/**
 * @author 
 *
 */

import java.io.IOException;
import base.* ;
import core.* ;

/**
 * Le test en Sort permet de choisir chaque fois aléatoirement deux sommets dans une graphe,
 * et lancer l'algorithme de Pcc.
 * L'algoritheme sera lancé par la méthode "compareTo()" dans la classe Element.
 */

public class LaunchTest extends Algo {
	
	// pour que l'utilisateur puisse choisir le type de l'algorithme et en distance / temps pour Pcc
	public int choixAlgo;
	public int isEnTemps;
	
	public LaunchTest(Graphe gr, Readarg readarg) {
		super(gr) ;
		graphe = gr ;
		this.choixAlgo = readarg.lireInt ("Lancer le test en Dijkstra Standard(0) ou Dijkstra A Star(1) ?");
		this.isEnTemps = readarg.lireInt ("Lancer le test en distance(0) ou temps(1) ?");
		// on lance le Sort test
		SortTest();
	}

	/**
	 * @param args
	 */
	public void SortTest(){
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		int size = graphe.getListeNoeuds().size();
		RandomTable table  = new RandomTable(size, choixAlgo, isEnTemps, graphe);
		table.generate();
		if (Constants.printTable)
			System.out.println("Generated table()" + table);
		System.out.println("First element : " + table.sortAndReturnFirst());
		if (Constants.printTable)
			System.out.println("Sorted Table" + table);
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Total run time : " +totalTime/1000 + "s");
	}

	public void run() throws IOException {
	}
}