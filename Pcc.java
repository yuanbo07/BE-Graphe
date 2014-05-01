package core ;

import java.io.* ;
import java.util.ArrayList;
import base.Readarg ;

public class Pcc extends Algo {

    // Numero des sommets origine et destination
    protected int zoneOrigine ;
    protected int origine ;

    protected int zoneDestination ;
    protected int destination ;

	public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg) ;
		
		// Demander la zone et le sommet départ.
	
		this.zoneOrigine = gr.getZone () ;
		this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;
	
		// Demander la zone et le sommet destination.
		this.zoneDestination = gr.getZone () ;
		this.destination = readarg.lireInt ("Numero du sommet destination ? ");
    }
	
	/*
	 * Principe de Dijkstra
	 * 
	 * 1. phase d'initialisation
	 * on initialise noeud départ
	 * on initialise cout de tous les sommets : infini -> marque = false
	 * 
	 * 2. on cherche successeurs de sommet origin
	 * 
	 * 3. on met à jour la distance
	 * 
	 */
	
	
    // phase d'initialisation : on met tous les noeuds "non marqué" 
	
    public void initialisation(){
    	
    	// le nombre de noeuds dans le graphe
    	int numNoeudGraphe = this.graphe.getListeNoeuds().size();
    	  
    	int i = 0;
  
    	// on a le tas pour y mettre tous les labels (pour ordonner le coutCourant)
    	BinaryHeap<Label> tasLabel= new BinaryHeap<Label>();
    	
    	// une liste qui stocke les labels de tous les noeuds
    	ArrayList<Label> listeLabel = new ArrayList<Label>();
    	
    	// on initialise le noeud de départ
    	listeLabel.add(new Label(origine));
    	
    	// on met le noeud départ dans le tas
    	tasLabel.insert(listeLabel.get(0));
    	
    	// on met labels de tous les autres noeuds dans la liste
    	for (i=0; i<numNoeudGraphe ; i++){
    		// si ce n'est pas noeud origine, on l'ajoute dans la liste
    		if(this.graphe.getListeNoeuds().get(i).getId_noeud() != origine)
    			listeLabel.add(new Label(i));
    	}
    }
    
    
    // algorithme de dijkstra
    
    public void algoDijkstra(){
    }

    public void run() {

	System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;

	this.initialisation();
	// A vous d'implementer la recherche de plus court chemin.
	
	
    }

}
