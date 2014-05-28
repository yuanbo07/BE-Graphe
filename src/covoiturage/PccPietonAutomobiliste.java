package covoiturage ;

import core.* ;
import java.awt.Color;
import java.util.HashMap;

/**
 * La classe Pcc est pour calculer le plus court chemin à la fois en algorithme Dijkstra Standard et en Dijkstra A Star.
 * Dans le but de minimiser la duplication de code, aucune autre classe n'a été créée.
 */

public class PccPietonAutomobiliste extends Algo {

    protected int origine ;
    
	// nombre de sommets dans le graphe
	private int numNoeudGraphe = graphe.getListeNoeuds().size();
	
    // HashMap qui met en correspondance le numéro de noeud et son label
    protected HashMap<Integer, Label> mapCorrespondanceNoeudLabel = new HashMap<Integer, Label>();
    
	// un tas binaire pour y mettre tous les labels, afin d'ordonner le coût courant de label
	BinaryHeap<Label> tasLabel= new BinaryHeap<Label>();
	
	// le nombre de sommets parcourus (= ont été placé dans le tas)
	private int nbParcouru;
	
	// nombre maximal des éléments présents dans le tas
	private int nbMaxTas;
	
	// le nombre de sommets marqués
	private int nbMarque;
    
    // le temps d'exécution de l'algorithme
    private long tempsExecution;
	
	// boolean qui indique si l'on est en mode "test"
	private boolean enModeTest = false ;
	
	private int isPieton = 0 ;
	
	/**
	 * constructeurs
	 */
    public PccPietonAutomobiliste(Graphe gr, int origine, int isPieton) {
		super(gr);
		this.origine = origine;
		// on passe en mode test
		this.enModeTest = true ;
		this.isPieton = isPieton ;
    }

	/**
	 * fonction qui initialise l'algorithme, met tas, et met les labels des sommets dans le Hashmap
	 */
	public void initialisationAlgo(){
 	    int i = 0;
    	// on met les labels de tous les noeuds dans la map de correspondance "numéro de noeud, label"
    	for (; i<numNoeudGraphe ; i++){
    		int numNoeudCourant = graphe.getListeNoeuds().get(i).getId_noeud();
    		// si c'est le noeud origine, on crée son label, l'initialise et le met dans le tas
    		if(numNoeudCourant == origine)
    		{
    			Label labelOrigine = new Label(origine, -1, 0, false) ;
    			mapCorrespondanceNoeudLabel.put(numNoeudCourant, labelOrigine);
    			tasLabel.insert(labelOrigine);
    			nbMaxTas++;
    			nbParcouru++;
    		}
    		// sinon
    		else {
    			mapCorrespondanceNoeudLabel.put(numNoeudCourant, new Label(i));
    		}
    	}
	}
	
    /**
     * Algorithme de Dijkstra et A star, avec l'implementation d'un tas binaire.
	/* La complexité pour ces deux algorithmes : o(nlogn) .
     */
    public void algoPCC(){
    	
    	// on initialise l'algorithme
    	this.initialisationAlgo();
	    // si le tas n'est pas vide
    	while(!(tasLabel.isEmpty())) {
        	// on choisit le label ayant le plus petit coût dans le tas, on le marque et incrémente le nombre d'éléments marqués
        	Label labelCourant = tasLabel.deleteMin();
        	labelCourant.setMarquage(true);
        	nbMarque++;
        	
        	// pour ce label, on obtient le noeud correspondant
        	Noeud noeudCourant = graphe.getListeNoeuds().get(labelCourant.getId_sommetCourant());
	        for (Successeur succ : noeudCourant.getListeSuccesseur()){
	        	// si c'est un chemin non routier, on ne le traite pas
	        	if(succ.getDescripteur().getType() != 'z'){
	        		// on choisit un noeud successeur courant
	        		Noeud noeudSuccCourant = succ.getNoeudDestination();
	        		Label labelNoeudSuccCourant = mapCorrespondanceNoeudLabel.get(noeudSuccCourant.getId_noeud());
	        		// si ce noeud successeur n'est pas encore marqué par algo
	        		if(!labelNoeudSuccCourant.isMarque()){
	        			double coutSuccCourantNouveau = 0 ;
	        			// si l'utilisateur a choisi le calcul en distance
	        			coutSuccCourantNouveau = labelCourant.getCoutCourant() + succ.getTempsArete();
	        			
		    			// si le coût courant nouveau obtenu est inférieur à son coût courant avant
		    			if (coutSuccCourantNouveau < labelNoeudSuccCourant.getCoutCourant()) {
		    				// on remplace l'ancien coût avec ce nouveau coût, et change son père
		    				labelNoeudSuccCourant.setCoutCourant(coutSuccCourantNouveau);
		    				labelNoeudSuccCourant.setId_sommetPere(noeudCourant.getId_noeud());
		    			}
	        			// si ce noeud successeur n'est pas encore parcouru
	        			if(!tasLabel.isInHeap(labelNoeudSuccCourant)){
	        				if(isPieton == 0)
	        					labelNoeudSuccCourant.setParcouru_pieton(true);
	        				else
	        					labelNoeudSuccCourant.setParcouru_automobiliste(true);
	        				// on le met dans le tas, et incrémente le nombre de sommets parcourus
	        				tasLabel.insert(labelNoeudSuccCourant);
	        				nbParcouru++;
	        				dessinerSegment(noeudCourant,noeudSuccCourant);
	        			}
		    			// au final, on met à jour le tas
				    	tasLabel.update(labelNoeudSuccCourant);
		    		}
	        	}
	        	// on met à jour nombre max dans le tas
	        	updateNbElementMaxTas();
	        }
    	}
    }
    
    /**
     * fonction qui met à jour le nombre maximal des éléments dans le tas
     */
    public void updateNbElementMaxTas(){
    	if(tasLabel.currentSize > nbMaxTas)
    		nbMaxTas = tasLabel.currentSize;
    }
    
    /**
     * fonction qui dessine un segment entre deux noeuds
     */
    public void dessinerSegment(Noeud n1, Noeud n2){
    	if (isPieton == 0)
    		graphe.getDessin().setColor(Color.blue);
    	if (isPieton == 1)
    		graphe.getDessin().setColor(Color.black);
		graphe.getDessin().setWidth(2);
		graphe.getDessin().drawLine(n1.getLongitude(), n1.getLatitude(), n2.getLongitude(), n2.getLatitude());
    }

    public void run(){
        	long debut = System.nanoTime();
        		algoPCC();
        		tempsExecution = System.nanoTime() - debut;
    }
}