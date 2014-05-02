package core ;

import java.awt.Color;
import java.io.* ;
import java.util.ArrayList;
import java.util.HashMap;
import base.Readarg ;

public class Pcc extends Algo {

    // Numero des sommets origine et destination
    protected int zoneOrigine ;
    protected int origine ;

    protected int zoneDestination ;
    protected int destination ;
    
    // pour mettre en correspondance le numéro de noeud et son label
    private HashMap<Integer, Label> mapCorrespondanceNoeudLabel = new HashMap<Integer, Label>();
    
	// on a le tas pour y mettre tous les labels (pour ordonner le coutCourant)
	BinaryHeap<Label> tasLabel= new BinaryHeap<Label>();
	
	// nombre de sommets dans le graphe
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
    
  
	
	// fonction qui initialise le tas et met labels des sommets dans hashmap
	
	public void initialisationAlgoDijkstra(){
		
 	    int i = 0;
 	    
    	// on met labels de tous les noeuds dans la map correspondance Noeud-Label
    	for (; i<numNoeudGraphe ; i++){
    		int numNoeudCourant = this.graphe.getListeNoeuds().get(i).getId_noeud();
    		if(numNoeudCourant == origine)
    		{
    			Label labelOrigine = new Label(origine, -1, 0, false) ;
    			mapCorrespondanceNoeudLabel.put(numNoeudCourant, labelOrigine);
    			tasLabel.insert(labelOrigine);
    		}
    		else {
    			mapCorrespondanceNoeudLabel.put(numNoeudCourant, new Label(i));
    		}
    	}
	}
    	
    	
    // algorithme de dijkstra avec le tas
    
    public void algoDijkstra(){
    	
    	System.out.println("la taille de tas avant initialisation: " + tasLabel.getCurrentSize());
    	System.out.println("hashmap : " + mapCorrespondanceNoeudLabel.size());
    	initialisationAlgoDijkstra();
    	System.out.println("la taille de tas après initialisation: " + tasLabel.getCurrentSize());
    	System.out.println("hashmap après : " + mapCorrespondanceNoeudLabel.size());
    	System.out.println("hashmap test : " + mapCorrespondanceNoeudLabel.get(this.graphe.getListeNoeuds().get(origine).getId_noeud()));
        System.out.println("la taille de tas dans la boucle : " + tasLabel.getCurrentSize());
        	
        	/* 
        	 * phase déroulement algo Dijkstra
        	 */
    		
    	// si le tas n'est pas vide
    	if(!(tasLabel.isEmpty())) {
        	// on choisie label ayant le plus petit cout dans le tas
        	Label labelCourant = tasLabel.deleteMin();
        	Noeud noeudCourant = this.graphe.getListeNoeuds().get(labelCourant.getId_sommetCourant());
        	// on le marque
        	labelCourant.setMarquage(true);
    			System.out.println("1");
	        	// pour tous ses successeurs (tous les arcs sortants)
	        	for (Successeur succ : noeudCourant.getListeSuccesseur()){
	        		// on choisie le noeud succ courant
	        		Noeud noeudSuccCourant = succ.getNoeudDestination();
	        		// on a son label
	        		Label labelNoeudSuccCourant = mapCorrespondanceNoeudLabel.get(noeudSuccCourant);
	        		// si ce noeud successeur n'est pas encore marqué
	    			System.out.println("2");
		    		if(!labelNoeudSuccCourant.isMarque()){
		    			double cout = 0;
		    			// on met à jour son cout courant (de sommet d'origine jusqu'ici)
		    			cout = labelCourant.getCoutCourant() + succ.getLongueurArrete();
		    			// si cette fois, le cout total obtenu est inférieur à son cout total avant
		    			System.out.println("3");
		    			if (cout < labelNoeudSuccCourant.getCoutCourant()) {
		    				// on remplace l'ancien cout avec ce nouveau cout total 
		    				labelNoeudSuccCourant.setCoutCourant(cout);
		    				// on change son père à ce nouveau noeud 
		    				labelNoeudSuccCourant.setId_sommetPere(noeudCourant.getId_noeud());
		    			}
		    			
		    			// si ce noeud n'est pas dans le tas
		    			if(!tasLabel.isInHeap(labelNoeudSuccCourant)){
			    			System.out.println("4");
		    				// on met ce label dans le tas
		    				tasLabel.insert(labelNoeudSuccCourant);
		    				// on dessine un segment entre noeudCourant et son succCourant
							this.graphe.getDessin().setColor(Color.cyan);
							this.graphe.getDessin().setWidth(5);
							this.graphe.getDessin().drawLine(noeudCourant.getLongitude(), noeudCourant.getLatitude(), noeudSuccCourant.getLongitude(), noeudSuccCourant.getLatitude());
		    			}
		    			else
				    		tasLabel.update(labelNoeudSuccCourant);
		    		}
	        	} 
    		}
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
			algoDijkstra();
		}
    }
}