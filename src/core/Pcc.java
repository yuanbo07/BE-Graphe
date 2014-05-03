package core ;

import java.awt.Color;
import java.io.* ;
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
	
	// nombre maximal des elements présents dans le tas
	private int nbMaxTas;
	
	// le plus court chemin obtenu par algorithme de Dijkstra
	private Chemin plusCourtChemin;
	
	// le nombre de sommets marqués
	private int nbMarque;
	
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
    	
    	// initialiser
    	this.initialisationAlgoDijkstra();
  
    	// tourner l'algo jusqu'à noeud destination soit marqué
		do{
	    	// si le tas n'est pas vide
	    	if(!(tasLabel.isEmpty())) {
	        	// on choisie label ayant le plus petit cout dans le tas
	        	Label labelCourant = tasLabel.deleteMin();
	        	// on le marque
	        	labelCourant.setMarquage(true);
	        	nbMaxTas--;
	        	nbMarque++;
	        	
	        	Noeud noeudCourant = this.graphe.getListeNoeuds().get(labelCourant.getId_sommetCourant());
		        	// pour tous ses successeurs (tous les arcs sortants)
		        	for (Successeur succ : noeudCourant.getListeSuccesseur()){
		        		// on choisie le noeud succ courant
		        		Noeud noeudSuccCourant = succ.getNoeudDestination();
		        		// on a son label à partir de son id
		        		Label labelNoeudSuccCourant = mapCorrespondanceNoeudLabel.get(noeudSuccCourant.getId_noeud());
		        		System.out.println("" + labelNoeudSuccCourant.getId_sommetCourant());
		        		
		        		// si ce noeud successeur n'est pas encore marqué
			    		if(!labelNoeudSuccCourant.isMarque()){
			    			double cout = 0;
			    			// on met à jour son cout courant (de sommet d'origine jusqu'ici)
			    			cout = labelCourant.getCoutCourant() + succ.getLongueurArrete();
			    			// si cette fois, le cout total obtenu est inférieur à son cout total avant
			    			if (cout < labelNoeudSuccCourant.getCoutCourant()) {
			    				// on remplace l'ancien cout avec ce nouveau cout total 
			    				labelNoeudSuccCourant.setCoutCourant(cout);
			    				// on change son père à ce nouveau noeud 
			    				labelNoeudSuccCourant.setId_sommetPere(noeudCourant.getId_noeud());
			    			}
			    			
			    			// si ce noeud est déjà dans le tas
			    			if(tasLabel.isInHeap(labelNoeudSuccCourant)){
		    				// on met à jour le tas pour éventuel changement de cout de noeud courant
					    		tasLabel.update(labelNoeudSuccCourant);
				    		}
			    			// sinon
			    			else{
			    				// on le met dans le tas
			    				tasLabel.insert(labelNoeudSuccCourant);
			    				nbMaxTas++;
			    				updateNbElementMaxTas();
			    				// on dessine un segment entre noeudCourant et son succCourant
								this.graphe.getDessin().setColor(Color.GREEN);
								this.graphe.getDessin().setWidth(5);
								this.graphe.getDessin().drawLine(noeudCourant.getLongitude(), noeudCourant.getLatitude(), noeudSuccCourant.getLongitude(), noeudSuccCourant.getLatitude());
			    			}
			    		}
		        	} 
	    	}
	    	else {
		    		System.out.println("Il n'existe pas de chemin entre noueud "+ origine + " et noeud " +destination);
		    		break;
	    	}
    	} while(!mapCorrespondanceNoeudLabel.get(destination).isMarque());
		
		System.out.println("Le nombre maximal dans le tas est : " + nbMaxTas);
		System.out.println();
    }
    
    // fonction qui met à jour le nb maximal des elements dans le tas
    
    public void updateNbElementMaxTas(){
    	if(tasLabel.size() > nbMaxTas)
    		nbMaxTas = tasLabel.size();
    }
    
    // afficher le plus court chemin
    
    public void afficherPlusCourtChemin(){
    	
    	Noeud noeudDepart = this.graphe.getListeNoeuds().get(origine);
    	Noeud noeudDestination = this.graphe.getListeNoeuds().get(destination);
    	Chemin plusCourtChemin = new Chemin(noeudDepart, noeudDestination);
    	// on met d'abords le noeud de distination dans le chemin
    	plusCourtChemin.addNoeud(noeudDestination);	
    	Label labelCourant = mapCorrespondanceNoeudLabel.get(destination);
    	
    	// on cherche parmi tous les noeuds marqués
    	for(int i=0;i < nbMarque;i++){
    		// si ce n'est pas le noeud de départ
    		while(labelCourant.getId_sommetPere() != -1){
    			// on cherche son père et l'ajoute dans le chemin
    			Noeud noeudCourant = this.graphe.getListeNoeuds().get(labelCourant.getId_sommetPere());
    			plusCourtChemin.addNoeud(noeudCourant);
    			labelCourant = mapCorrespondanceNoeudLabel.get(labelCourant.getId_sommetPere());
    		}
    		// on renverse le chemin afin qu'il soit origine->destination
    		plusCourtChemin.renverserChemin();
    	}
		plusCourtChemin.dessinerChemin(this.graphe.getDessin());
    }
    

    public void run() {

		System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;
		
		// si la saisie est erronée, inutile de lancer Dijkstra

		if (!verifierSaisirNoeudDepart())
			System.out.println("Le sommet d'origine " + origine + " n'existe pas dans cette carte.");
		else if (!verifierSaisirNoeudDest())
			System.out.println("Le sommet destination " + destination + " n'existe pas dans cette carte.");
		else if (origine == destination)
			System.out.println("Le plus court chemin est 0.");
		// si la saisie est bonne, lancer Dijkstra
		else {
			algoDijkstra();
			afficherPlusCourtChemin();
		}
    }
}
