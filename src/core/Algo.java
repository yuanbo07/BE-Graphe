package core ;

import java.io.* ;

import base.* ;

/**
 * Classe abstraite représentant un algorithme (connexite, plus court chemin, etc.)
 */

public abstract class Algo {

    protected PrintStream sortie ;
    protected Graphe graphe ;
    
    protected Algo(Graphe gr, PrintStream fichierSortie, Readarg readarg) {
		this.graphe = gr ;
		this.sortie = fichierSortie ;	
    }
    
    protected Algo(Graphe gr) {
		this.graphe = gr ;
    }
    
    public abstract void run() throws IOException ;
}