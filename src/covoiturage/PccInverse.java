package covoiturage;

import core.*;
import java.awt.Color;
import java.util.HashMap;

/**
 * La classe PccInverse
 * 
 * PccInverse est utilisé pour marquer, parmi tous les noeuds en communs parcourus par le piéton et l'automobiliste,
 * lesquels qui ont été parcourus par la destination. Cela nous donnera tous les noeuds de rencontres possibles.
 */

public class PccInverse extends Pcc {
	
	// noeud destination
    protected int destination ;
	
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
	
    // hashmap pour le problème covoiturage
	public HashMap<Integer, LabelCovoiturage> mapListeNoeudCommun = new HashMap<Integer, LabelCovoiturage>();
	
	// la proportion entre vitesse de l'automobiliste et la vitesse du piéton
	private int proportionVitesse ;
	
    // le temps d'exécution de l'algorithme
    private long tempsExecution;
	
	/**
	 * constructeurs
	 */
	
    public PccInverse(Graphe gr, int destination, HashMap<Integer, LabelCovoiturage> mapListeNoeudCommun, int proportionVitesse) {
		this.destination = destination;
		this.mapListeNoeudCommun = mapListeNoeudCommun ;
		this.proportionVitesse = proportionVitesse ;
    }

	/**
	 * fonction qui initialise l'algorithme, met tas, et met les labels des sommets dans le Hashmap
	 */
	public void initialisationAlgo(){
 	    int i = 0;
    	// on met les labels de tous les noeuds dans la map de correspondance "numéro de noeud, label"
    	for (; i<numNoeudGraphe ; i++){
    		int numNoeudCourant = graphe.getListeNoeuds().get(i).getId_noeud();
    		// si c'est le noeud destinataire, on crée son label, l'initialise et le met dans le tas
    		if(numNoeudCourant == destination)
    		{
    			Label labelDestination = new Label(destination, -1, 0, false) ;
    			mapCorrespondanceNoeudLabel.put(destination, labelDestination);
    			tasLabel.insert(labelDestination);
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
		        for (Successeur pred : noeudCourant.getListePredecesseur()){
		        	// si c'est un chemin non routier, on ne le traite pas
		            	if(pred.getDescripteur().getType() != 'z'){
		        		// on choisit un noeud predecesseur courant
		        		Noeud noeudPredCourant = pred.getNoeudPere();
		        		Label labelNoeudPredCourant = mapCorrespondanceNoeudLabel.get(noeudPredCourant.getId_noeud());
		        		// si ce noeud predecesseur n'est pas encore marqué par algo
		        		if(!labelNoeudPredCourant.isMarque()){
		        			double coutPredCourantNouveau = 0 ;
		        			if (proportionVitesse != 6)
		        				coutPredCourantNouveau = labelCourant.getCoutCourant() + pred.getLongueurArete()*3600/(proportionVitesse*4*1000);
		        			else
		        				coutPredCourantNouveau = labelCourant.getCoutCourant() + pred.getTempsArete() ;
		        				
	        				// si le coût courant nouveau obtenu est inférieur à son coût courant avant
			    			if (coutPredCourantNouveau < labelNoeudPredCourant.getCoutCourant()) {
			    				// on remplace l'ancien coût avec ce nouveau coût, et change son père
			    				labelNoeudPredCourant.setCoutCourant(coutPredCourantNouveau);
			    				labelCourant.setId_sommetPere(noeudPredCourant.getId_noeud());
			    			}
		        			// si ce noeud predcesseur n'est pas encore parcouru
		        			if(!tasLabel.isInHeap(labelNoeudPredCourant)){
		        				// on le met dans le tas, et incrémente le nombre de sommets parcourus
		        				tasLabel.insert(labelNoeudPredCourant);
		        				nbParcouru++;
		        				dessinerSegment(noeudCourant,noeudPredCourant);
		        				if(mapListeNoeudCommun.containsKey(pred.getNoeudPere().getId_noeud()))
				        			labelNoeudPredCourant.setParcouru_destination(true);
		        			}
					    	tasLabel.update(labelNoeudPredCourant);
			    		}
		        	}
		        	// on met à jour nombre max dans le tas
		        	updateNbElementMaxTas();
		        }
	    	}
    }
    
    public void updateNbElementMaxTas(){
    	if(tasLabel.currentSize > nbMaxTas)
    		nbMaxTas = tasLabel.currentSize;
    }
    
    /**
     * fonction qui dessine un segment entre deux noeuds
     */
    public void dessinerSegment(Noeud n1, Noeud n2){
		graphe.getDessin().setColor(Color.GREEN);
		graphe.getDessin().setWidth(2);
		graphe.getDessin().drawLine(n1.getLongitude(), n1.getLatitude(), n2.getLongitude(), n2.getLatitude());
    }

    /**
     * fonction principale qui lance l'algorithme, affiche les résultats et le temps d'exécution
     */ 
    public void run(){
    	long debut = System.nanoTime();
    		algoPCC();
    	tempsExecution = System.nanoTime() - debut;
    }

	public int getNbParcouru() {
		return nbParcouru;
	}

	public int getNbMaxTas() {
		return nbMaxTas;
	}

	public int getNbMarque() {
		return nbMarque;
	}

	public void setNbParcouru(int nbParcouru) {
		this.nbParcouru = nbParcouru;
	}

	public void setNbMaxTas(int nbMaxTas) {
		this.nbMaxTas = nbMaxTas;
	}

	public void setNbMarque(int nbMarque) {
		this.nbMarque = nbMarque;
	}

	public long getTempsExecution() {
		return tempsExecution;
	}

	public void setTempsExecution(long tempsExecution) {
		this.tempsExecution = tempsExecution;
	}
}