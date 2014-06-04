package covoiturage ;

import java.awt.Color;
import java.util.HashMap;
import core.*;

/**
 * La classe PccPieton
 * 
 * Cette classe ne sert qu'à dessiner le Pcc entre le point départ du piéton et le meilleur point de rencontre.
 * La raison que l'on n'utilise pas le Pcc ici, c'est parce qu'on a les contraintes particulières pour le piéton (routes inempruntables),
 */

public class PccPieton extends Pcc {
	
	// sommet origine et sommet destinataire
    protected int origine ;
    protected int destination ;

	// nombre de sommets dans le graphe
	private int numNoeudGraphe = graphe.getListeNoeuds().size();
	
	// le nombre de sommets marqués
	private int nbMarque;
	
    // HashMap qui met en correspondance le numéro de noeud et son label
    private HashMap<Integer, Label> mapCorrespondanceNoeudLabel = new HashMap<Integer, Label>();
    
	// un tas binaire pour y mettre tous les labels, afin d'ordonner le coût courant de label
	private BinaryHeap<Label> tasLabel= new BinaryHeap<Label>();
	
	// le plus court chemin obtenu par l'algorithme
	private Chemin plusCourtChemin = new Chemin();
	
	/**
	 * constructeurs
	 */	
	// constructeur spécifique pour lancer le test
    public PccPieton(Graphe gr, int origine, int destination) {
		this.origine = origine;
		this.destination = destination;
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
    		}
    		// sinon
    		else {
    			mapCorrespondanceNoeudLabel.put(numNoeudCourant, new Label(i));
    		}
    	}
	}
	
    /**
     * Algorithme de Dijkstra du type "un vers un"
     */
    public void algoPCC(){
    	
    	// on initialise l'algorithme
    	this.initialisationAlgo();
    	// tourner l'algorithme jusqu'à le noeud destination soit marqué
		do {
	    	// si le tas n'est pas vide
	    	if(!(tasLabel.isEmpty())) {
	        	// on choisit le label ayant le plus petit coût dans le tas, on le marque et incrémente le nombre d'éléments marqués
	        	Label labelCourant = tasLabel.deleteMin();
	        	labelCourant.setMarquage(true);
	        	nbMarque++;
	        	
	        	// pour ce label, on obtient le noeud correspondant
	        	Noeud noeudCourant = graphe.getListeNoeuds().get(labelCourant.getId_sommetCourant());
		        for (Successeur succ : noeudCourant.getListeSuccesseur()){
		        	
		        	boolean succValidePieton = succ.getDescripteur().vitesseMax() != 110 && succ.getDescripteur().vitesseMax() != 130 ;
		        	// si c'est l'automobiliste ou le piéton qui respecte les vitesses max, on prend ce successeur
		        	if(succValidePieton){
			        	// si c'est un chemin non routier, on ne le traite pas
			        	if(succ.getDescripteur().getType() != 'z'){
			        		Noeud noeudSuccCourant = succ.getNoeudDestination();
			        		Label labelNoeudSuccCourant = mapCorrespondanceNoeudLabel.get(noeudSuccCourant.getId_noeud());
			        		// si ce noeud successeur n'est pas encore marqué par algo
			        		if(!labelNoeudSuccCourant.isMarque()){
			        			double coutSuccCourantNouveau = 0 ;
			        			coutSuccCourantNouveau = labelCourant.getCoutCourant() + succ.getTempsAretePieton();
			        			
				    			// si le coût courant nouveau obtenu est inférieur à son coût courant avant
				    			if (coutSuccCourantNouveau < labelNoeudSuccCourant.getCoutCourant()) {
				    				// on remplace l'ancien coût avec ce nouveau coût, et change son père
				    				labelNoeudSuccCourant.setCoutCourant(coutSuccCourantNouveau);
				    				labelNoeudSuccCourant.setId_sommetPere(noeudCourant.getId_noeud());
				    			}
			        			// si ce noeud successeur n'est pas encore parcouru
			        			if(!tasLabel.isInHeap(labelNoeudSuccCourant)){
			        				tasLabel.insert(labelNoeudSuccCourant);
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
	    	else
	    	// s'il n'existe pas de pcc entre ces deux noeuds, on sort de fonction
		    return;
		} while(!mapCorrespondanceNoeudLabel.get(destination).isMarque());
		// si la destination est marquée, le plus court chemin existe
    }
    
    /**
     * fonction qui dessine un segment entre deux noeuds
     */
    public void dessinerSegment(Noeud n1, Noeud n2){
		graphe.getDessin().setColor(Color.blue);
		graphe.getDessin().setWidth(2);
		graphe.getDessin().drawLine(n1.getLongitude(), n1.getLatitude(), n2.getLongitude(), n2.getLatitude());
    }
    
    /**
     * fonction qui construit le plus court chemin avec une recherche dans le sens inverse
     */
    public void construirePlusCourtChemin(){
    	Noeud noeudDepart = graphe.getListeNoeuds().get(origine);
    	Noeud noeudDestination = graphe.getListeNoeuds().get(destination);
    	// on place le noeud de destination dans le chemin
    	plusCourtChemin.addNoeud(noeudDestination);	
    	Label labelCourant = mapCorrespondanceNoeudLabel.get(destination);
    	// on cherche parmi tous les noeuds qui ont été marqués
    	int i = 0;
    	for(;i < nbMarque ;i++){
    		// si le noeud courant n'est pas le noeud départ
    		while(labelCourant.getId_sommetPere() != -1){
    			// on cherche son père, puis ajoute le père dans le chemin
    			Noeud noeudCourant = graphe.getListeNoeuds().get(labelCourant.getId_sommetPere());
    			plusCourtChemin.addNoeud(noeudCourant);
    			labelCourant = mapCorrespondanceNoeudLabel.get(labelCourant.getId_sommetPere());
    		}
    	}
		plusCourtChemin.renverserChemin();
    	plusCourtChemin.setNoeudDepart(noeudDepart);
    	plusCourtChemin.setNoeudDestination(noeudDestination);
    	plusCourtChemin.setNbNoeud(plusCourtChemin.getListeNoeudChemin().size());
    }
	
    /**
     * fonction qui dessine le plus court chemin
     */
	public void afficherPlusCourtChemin(){
		plusCourtChemin.dessinerChemin(graphe.getDessin());
	}
    
    /**
     * fonction principale qui lance l'algorithme
     */
    public void run(){
				algoPCC();
	    }
}