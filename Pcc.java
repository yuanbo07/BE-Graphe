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
	
	// on a le tas pour y mettre tous les labels (pour ordonner le coutCourant)
	BinaryHeap<Label> tasLabel= new BinaryHeap<Label>();
	
	// une liste qui stocke les labels de tous les noeuds
	ArrayList<Label> listeLabel = new ArrayList<Label>();

	public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg) ;
		
		// Demander la zone et le sommet départ.
	
		this.zoneOrigine = gr.getZone () ;
		this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;
	
		// Demander la zone et le sommet destination.
		this.zoneDestination = gr.getZone () ;
		this.destination = readarg.lireInt ("Numero du sommet destination ? ");
    }
	
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

	// initialisation de tous les labels
	
    public void initialisation(){
    	
 	    int i = 0;
    	// on met labels de tous les noeuds dans la liste
    	for (i=0; i<numNoeudGraphe ; i++){
    		if(this.graphe.getListeNoeuds().get(i).getId_noeud() == origine)
    		{
    			Label labelOrigine = new Label(origine, -1, 0, false) ;
    			listeLabel.add(labelOrigine);
    			tasLabel.insert(labelOrigine);
    		}
    		else
    			listeLabel.add(new Label(i));
    	}
    }
    
    // algorithme de dijkstra
    
    public void algoDijkstra(){
    	int i =0;
    	// on a le label ayant le plus petit cout dans le tas 
    	Label labelCourant = tasLabel.deleteMin();
    	Noeud noeudCourant = this.graphe.getListeNoeuds().get(labelCourant.getId_sommetCourant());
    	
    	for (Successeur succ : noeudCourant.getListeSuccesseur())
    		labelCourant.setCoutCourant(labelCourant.getCoutCourant() + succ.getLongueurArrete());
    }

    public void run() {

		System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;
		
		// si la saisie est erronnée, inutile de lancer Dijkstra

		if (!verifierSaisirNoeudDepart())
			System.out.println("Le sommet d'origine " + origine + " n'existe pas dans cette carte.");
		else if (!verifierSaisirNoeudDest())
			System.out.println("Le sommet destination " + destination + " n'existe pas dans cette carte.");
		else if (origine == destination)
			System.out.println("Le plus court chemin est 0.");
		// si la saisie est bonne, lancer Dijkstra
		else {
			initialisation();
			algoDijkstra();
		}
    }
}
