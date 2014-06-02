package covoiturage ;

import core.* ;
import java.awt.Color;
import java.util.HashMap;

/**
 * La classe PccReg (Regular Pcc)
 * 
 * Cette classe représente l'algorithme général de Dijkstra (un sommet vers tous).
 * On utilise cette classe pour trouver les sommets en commun parcourus à la fois par le piéton et l'automobiliste sur la carte.
 * 
 * p.s. : Actuellement, on a implémenté ici une façon simple pour permettre le piéton de ne pas marcher plus de x minutes.
 * La méthode qu'on a implémenté pour le moment n'est pas exactement la méthode demandée dans le sujet, reste à refaire.
 */

public class PccReg extends Pcc {
	
	// le noeud origine
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
	
    // le boolean qui indique l'algo pour piéton
	private int isPieton = 0 ;
	
	// la proportion entre vitesse de l'automobiliste et la vitesse du piéton
	private int proportionVitesse ;
	
	// le temps maximal que le piéton peut marcher, en minute
	private int limitePieton = -1 ;
	
	/**
	 * constructeurs
	 */
	
	//constructeur pour l'automobiliste
    public PccReg(Graphe gr, int origine, int isPieton, int proportionVitesse) {
		this.origine = origine;
		this.isPieton = isPieton ;
		this.proportionVitesse = proportionVitesse ;
    }
    
	//constructeur pour le piéton
    public PccReg(Graphe gr, int origine, int isPieton, int proportionVitesse, int limitePieton) {
		this.origine = origine;
		this.isPieton = isPieton ;
		this.proportionVitesse = proportionVitesse ;
		this.limitePieton = limitePieton ;
    }

	/**
	 * fonction qui initialise l'algorithme
	 */
	public void initialisationAlgo(){
 	    int i = 0;
    	for (; i<numNoeudGraphe ; i++){
    		int numNoeudCourant = graphe.getListeNoeuds().get(i).getId_noeud();
    		if(numNoeudCourant == origine)
    		{
    			Label labelOrigine = new Label(origine, -1, 0, false) ;
    			mapCorrespondanceNoeudLabel.put(numNoeudCourant, labelOrigine);
    			// on marque le sommet origine selon le cas du piéton et l'automobiliste
    			if(isPieton == 1)
    				labelOrigine.setParcouru_pieton(true);
    			if(isPieton == 0)
    				labelOrigine.setParcouru_automobiliste(true);
    			tasLabel.insert(labelOrigine);
    			nbMaxTas++;
    			nbParcouru++;
    		}
    		else {
    			mapCorrespondanceNoeudLabel.put(numNoeudCourant, new Label(i));
    		}
    	}
	}
	
    public void algoPCC(){
    	
    	// on initialise l'algorithme
    	this.initialisationAlgo();
	    // si le tas n'est pas vide
    	while(!(tasLabel.isEmpty())) {
        	// on choisit le label ayant le plus petit coût dans le tas, on le marque et incrémente le nombre d'éléments marqués
        	Label labelCourant = tasLabel.deleteMin();
        	if(limitePieton != -1 && labelCourant.getCoutCourant() >= limitePieton * 60)
        		return ;
        	labelCourant.setMarquage(true);
        	nbMarque++;
        	
        	// pour ce label, on obtient le noeud correspondant
        	Noeud noeudCourant = graphe.getListeNoeuds().get(labelCourant.getId_sommetCourant());
	        for (Successeur succ : noeudCourant.getListeSuccesseur()){
	        	// la condition où le successeur peut être emprunté par le piéton
	        	boolean succValidePieton = (isPieton == 1 && succ.getDescripteur().vitesseMax() != 110 && succ.getDescripteur().vitesseMax() != 130) ;
	        	// si c'est l'automobiliste ou le piéton qui respecte les vitesses max, on prend ce successeur
	        	if(isPieton ==0 || succValidePieton){
	        	// si c'est un chemin non routier, on ne le traite pas
	        	if(succ.getDescripteur().getType() != 'z'){
	        		Noeud noeudSuccCourant = succ.getNoeudDestination();
	        		Label labelNoeudSuccCourant = mapCorrespondanceNoeudLabel.get(noeudSuccCourant.getId_noeud());
	        		// si ce noeud successeur n'est pas encore marqué par algo
	        		if(!labelNoeudSuccCourant.isMarque()){
	        			double coutSuccCourantNouveau = 0 ;
	        			// si c'est l'automobiliste et la proportion est celle de vitesse maximale
	        			if(isPieton == 0 && proportionVitesse == 6){
	        				coutSuccCourantNouveau = labelCourant.getCoutCourant() + succ.getTempsArete();
	        			}
	        			// si c'est l'automobiliste et la proportion n'est pas celle de vitesse maximale
	        			if(isPieton == 0 && proportionVitesse != 6){
	        				coutSuccCourantNouveau = labelCourant.getCoutCourant() + succ.getLongueurArete()*3600/(4*proportionVitesse*1000);
	        			}
	        			// si c'est le piéton, une méthode spécifique getTempsAretePieton() renvoie le coût en temps
	        			if(isPieton == 1){
	        				coutSuccCourantNouveau = labelCourant.getCoutCourant() + succ.getTempsAretePieton();
	        			}
	        			
		    			// si le coût nouveau est inférieur à son coût avant, on remplace le coût et change son père
		    			if (coutSuccCourantNouveau < labelNoeudSuccCourant.getCoutCourant()) {
		    				labelNoeudSuccCourant.setCoutCourant(coutSuccCourantNouveau);
		    				labelNoeudSuccCourant.setId_sommetPere(noeudCourant.getId_noeud());
		    			}
	        			// si ce noeud successeur n'est pas encore parcouru
	        			if(!tasLabel.isInHeap(labelNoeudSuccCourant)){
	        				if(isPieton == 1){
	        					// si on set pas limitation pour le piéton
	        					if(limitePieton == -1)
	        						labelNoeudSuccCourant.setParcouru_pieton(true);
	        					else {
	    	        				// si le coût courant est inférieur à x minutes, on marque ce sommet successeur, sinon on le marque pas
	        						if(labelNoeudSuccCourant.getCoutCourant() <= limitePieton*60)
		        						labelNoeudSuccCourant.setParcouru_pieton(true);
	        					}
	        				}
	        				// si c'est l'automobiliste
	        				if(isPieton == 0)
	        					labelNoeudSuccCourant.setParcouru_automobiliste(true);
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
    }
    
    /**
     * fonction qui dessine un segment entre deux noeuds
     */
    public void dessinerSegment(Noeud n1, Noeud n2){
    	if (isPieton == 1)
    		graphe.getDessin().setColor(Color.blue);
    	if (isPieton == 0)
    		graphe.getDessin().setColor(Color.black);
		graphe.getDessin().setWidth(2);
		graphe.getDessin().drawLine(n1.getLongitude(), n1.getLatitude(), n2.getLongitude(), n2.getLatitude());
    }
    
    /**
     * fonction qui met à jour le nombre maximal des éléments dans le tas
     */
    public void updateNbElementMaxTas(){
    	if(tasLabel.currentSize > nbMaxTas)
    		nbMaxTas = tasLabel.currentSize;
    }

    /**
     * fonction principale qui lance l'algorithme
     */ 
    public void run(){
        	long debut = System.nanoTime();
        		algoPCC();
        	tempsExecution = System.nanoTime() - debut;
    }

	/**
	 * getters & setters
	 */
    
	public int getNbParcouru() {
		return nbParcouru;
	}

	public int getNbMaxTas() {
		return nbMaxTas;
	}

	public int getNbMarque() {
		return nbMarque;
	}

	public long getTempsExecution() {
		return tempsExecution;
	}
}