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
	
	// fonctions qui vérifient la bonne saisie de numéro de noeuds
	
	public boolean verifierSaisirNoeudDepart(){
    	boolean trouveOrigine = false;
    	int i=0;
		for (; i<numNoeudGraphe ; i++){
    		if(this.graphe.getListeNoeuds().get(i).getId_noeud() == origine)
    			trouveOrigine = true;
		}
		return trouveOrigine;
	}
	
	public boolean verifierSaisirNoeudDest(){
    	boolean trouveDest = false;
    	int i=0;
		for (; i<numNoeudGraphe ; i++){
    		if(this.graphe.getListeNoeuds().get(i).getId_noeud() == destination)
    			trouveDest = true;
		}
		return trouveDest;
	}
	
	
    // phase d'initialisation : on met tous les noeuds "non marqué" 
	
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
		
		//

		if (verifierSaisirNoeudDepart())
			System.out.println("Le sommet d'origine " + origine + " n'existe pas dans cette carte.");
		else if (verifierSaisirNoeudDest())
			System.out.println("Le sommet destination " + destination + " n'existe pas dans cette carte.");
		else if (origine == destination)
			System.out.println("Le plus court chemin est 0.");
		else {
			algoDijkstra();
		}
	
    }

}
