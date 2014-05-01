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
    
	private int numNoeudGraphe = this.graphe.getListeNoeuds().size();

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
	
	
	public void verifierSaisirNoeudDepartEtDest(){
    	int i =0;
    	boolean trouveOrigine = false;
    	boolean trouveDest = false;
		for (; i<numNoeudGraphe ; i++){
    		if(this.graphe.getListeNoeuds().get(i).getId_noeud() == origine)
    			trouveOrigine = true;
    		if(this.graphe.getListeNoeuds().get(i).getId_noeud() == destination)
    			trouveDest = true;
		}
		if (!trouveOrigine)
			System.out.println("Le sommet d'origine que vous avez indiqué n'est pas dans cette carte.");
		if (!trouveDest)
			System.out.println("Le sommet destination que vous avez indiqué n'est pas dans cette carte.");
	}
	
    public void initialisation(){
    	
 	    int i = 0;
  
    	// on a le tas pour y mettre tous les labels (pour ordonner le coutCourant)
    	BinaryHeap<Label> tasLabel= new BinaryHeap<Label>();
    	
    	// une liste qui stocke les labels de tous les noeuds
    	ArrayList<Label> listeLabel = new ArrayList<Label>();
    	
    	// on met labels de tous les noeuds dans la liste
    	for (i=0; i<numNoeudGraphe ; i++){
    		if(this.graphe.getListeNoeuds().get(i).getId_noeud() == origine)
    			listeLabel.add(new Label(origine, -1, 0, false));
    		else
    			listeLabel.add(new Label(i));
    		
    		
    	}
    }
    
    
    // algorithme de dijkstra
    
    public void algoDijkstra(){
    	
    	
    }

    public void run() {

		System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;
		this.verifierSaisirNoeudDepartEtDest();
		this.initialisation();
	
    }

}
