package Zpieton ;

import core.*;
import covoiturage.*;
import java.awt.Color;
import java.util.HashMap;

/**
 * La classe PccZpieton
 * 
 * Algorithme :
 * 
 * (1) On prend comme paramètre en entrée un hashmap de tous les sommets parcourus par le piéton.
 * (2) On lance l'algorithme A* du sommet origine de l'automobiliste vers le sommet d'origine du piéton (guidage A*).
 * (3) A* va s'arrêter une fois qu'il a trouvé un sommet parcouru par le piéton, le 1er sommet qu'il a rencontré de Zpieton
 * (4) A* se transforme en mode Dijkstra "un vers tous", pour couvrir toute la zone Zpiéton
 * (5) Dijkstra va s'arrêter si la distance entre prochain sommet successeur choisi et le sommet d'origine du piéton > rayon maximal de Zpieton 
 * (6) Tous les sommets marqués par l'automobiliste sont les noeuds en commun.  
 */

public class PccZpieton extends Algo {

	// numéro de sommet origine et de sommet destinataire
    protected int origine ;
    protected int destination ;
    
	// le sommet origine du piéton
	private Noeud noeudPieton ;
	
	// nombre de sommets dans le graphe
	private int numNoeudGraphe = Algo.graphe.getListeNoeuds().size();
	
    // HashMap qui met en correspondance le numéro de noeud et son label
    private HashMap<Integer, Label> mapCorrespondanceNoeudLabel = new HashMap<Integer, Label>();
    
	// un tas binaire pour y mettre tous les labels, afin d'ordonner le coût courant de label
	BinaryHeap<Label> tasLabel = new BinaryHeap<Label>();
	
	// le nombre de sommets parcourus (= ont été placé dans le tas)
	private int nbParcouru;
	
	// nombre maximal des éléments présents dans le tas
	private int nbMaxTas;
	
	// le nombre de sommets marqués
	private int nbMarque;
	
    // le temps d'exécution de l'algorithme
    private long tempsExecution;
	
    // identifiant du type de l'algorithme, Dijkstra Standard(0) ou A*(1)
	private int choixAlgo ;
	
	// parmi tous les sommets parcourus par le piéton, le premier sommet rencontré par l'automobiliste (frontière de Zpieton)
	private int pointZoneRecherche ;
	
	// le rayon maximal de zone Zpiéton
	private double maxRayonZpieton ;
	
	// le booléen qui indique si on a déjà fini la recherche par A*, et commence à couvrir le zone Zpiéton
	private boolean modeRechercheZpieton = false ;
	
	// le hashmap qui stocke les sommets parcourus par le piéton
	private HashMap<Integer, LabelCovoiturage> mapListeNoeudMarquePieton = new HashMap<Integer, LabelCovoiturage>();
	
	// le hashmap qui stocke tous les sommets à la fois parcourus par le piéton et marqués par l'automobiliste
	private HashMap<Integer, Label> mapListeNoeudEnCommun = new HashMap<Integer, Label>();
	
	// le booléen qui indique qu'on a fini la recherche de zone Zpieton par A*
	private boolean finRechercheNoeudZpieton = false ;
	
	// le booléen qui indique qu'on a fini la couverture de zone Zpiéton
	private boolean finRechercheZpieton = false ;

	/**
	 * constructeurs
	 */
	public PccZpieton(Graphe gr, int numAutomobiliste, int numPieton, HashMap<Integer, LabelCovoiturage> mapListeNoeudCommun, int limitePieton) {
		super(gr);
		this.origine = numAutomobiliste;
		this.destination = numPieton ;
		this.mapListeNoeudMarquePieton = mapListeNoeudCommun ;
		// on choisit par défault A*
		this.choixAlgo = 1 ;
		this.maxRayonZpieton = (limitePieton/60)*4*3600 ;
		this.noeudPieton = graphe.getListeNoeuds().get(destination);
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
    		if (numNoeudCourant == origine)
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
	
    public void algoPCC(){
    	
    	// on initialise l'algorithme
    	this.initialisationAlgo();
		do {
	    	if(!(tasLabel.isEmpty())) {
	    		Label labelCourant = tasLabel.deleteMin();
	    		
	        	labelCourant.setMarquage(true);
	        	nbMarque++;
	        	
	        	Noeud noeudCourant = Algo.graphe.getListeNoeuds().get(labelCourant.getId_sommetCourant());
	        	
	        	// le point de retour pour labelled break
	        	search :
		        for (Successeur succ : noeudCourant.getListeSuccesseur()){
		        	
		        	Noeud noeudSuccCourant = succ.getNoeudDestination();
		        	Label labelNoeudSuccCourant = mapCorrespondanceNoeudLabel.get(noeudSuccCourant.getId_noeud());
		        	
		        	// si on est en recherche de zone Zpiéton, ou on a déjà trouvé Zpiéton et toujours dans ce zone
		        	if(!modeRechercheZpieton || (modeRechercheZpieton && 
		        			Graphe.distanceEntreDeuxNoeuds(noeudSuccCourant, noeudPieton) < maxRayonZpieton)){
			        	if(succ.getDescripteur().getType() != 'z'){
			        		// une fois A* a trouvé un sommet de Zpiéton, il se transforme en mode Dijkstra "un vers tous"
				        	if( !modeRechercheZpieton && mapListeNoeudMarquePieton.containsKey(succ.getNoeudDestination().getId_noeud())){
				        		pointZoneRecherche = succ.getNoeudDestination().getId_noeud() ;
				        		// on se transforme en mode Dijkstra un vers tous pour parourir Zpieton
				        		modeRechercheZpieton = true ;
				        		// on passe à Dijkstra
				        		choixAlgo = 0 ;
				        		// on a fini la recherche du Zpiéton
				        		finRechercheNoeudZpieton = true ;
				        		// on met ce premier dans le hashmap
				        		mapListeNoeudEnCommun.put(succ.getNoeudDestination().getId_noeud(), labelNoeudSuccCourant);
				        		// on retourne à ce successeur, et par la suite c'est du Dijkstra "un vers tous"
				        		break search;
				        	}
				        	else {
				        		if(!labelNoeudSuccCourant.isMarque()){
				        			double coutSuccCourantNouveau = 0 ;
				        			coutSuccCourantNouveau = labelCourant.getCoutCourant() + succ.getTempsArete();
				        			
					    			if (coutSuccCourantNouveau < labelNoeudSuccCourant.getCoutCourant()) {
					    				labelNoeudSuccCourant.setCoutCourant(coutSuccCourantNouveau);
					    				labelNoeudSuccCourant.setId_sommetPere(noeudCourant.getId_noeud());
					    				
						    			// la recherche de zone Zpiéton est en A*
						    			if (choixAlgo == 1){
						    				Noeud noeudDestination = graphe.getListeNoeuds().get(destination);
						    				// on calcule l'estimation en temps
								    		double estimationTemps = Graphe.tempsEntreDeuxNoeud(noeudSuccCourant, noeudDestination);
								    		labelNoeudSuccCourant.setCoutEstimation(estimationTemps);
						    			}
						    		}
				        			// si ce noeud successeur n'est pas encore parcouru
				        			if(!tasLabel.isInHeap(labelNoeudSuccCourant)){
				        				tasLabel.insert(labelNoeudSuccCourant);
				        				nbParcouru++;
				        				// si ce noeud successeur a été parcouru par le piéton, on le met dans le hashmap
				        				if(modeRechercheZpieton && mapListeNoeudMarquePieton.containsKey(noeudSuccCourant.getId_noeud())){
				    		        		mapListeNoeudEnCommun.put(succ.getNoeudDestination().getId_noeud(), labelNoeudSuccCourant);	
				        				}
				        				// le parcours de A* est en gris
				        				if(choixAlgo == 1)
				        					dessinerSegment(noeudCourant,noeudSuccCourant, Color.LIGHT_GRAY);
				        				// le parcours de Dijkstra est en rose
				        				if(choixAlgo == 0)
				        					dessinerSegment(noeudCourant,noeudSuccCourant, Color.PINK);
				        			}
							    	tasLabel.update(labelNoeudSuccCourant);
						    	}
					        }
					        updateNbElementMaxTas();
			        	}
		        	}
		        	else
		        		break search ;
		        }
		}
	    else
	    	return;
	    // on sort de la boucle si les deux recherches sont toutes finies
		} while(!finRechercheNoeudZpieton || !finRechercheZpieton);
    }
    
    /**
     * fonction qui met à jour le nombre maximal des éléments dans le tas
     */
    public void updateNbElementMaxTas(){
    	if(tasLabel.getCurrentSize() > nbMaxTas)
    		nbMaxTas = tasLabel.getCurrentSize();
    }
    
    /**
     * fonction qui dessine un segment entre deux noeuds
     */
    public void dessinerSegment(Noeud n1, Noeud n2, Color color){
		graphe.getDessin().setColor(color);
		graphe.getDessin().setWidth(2);
		graphe.getDessin().drawLine(n1.getLongitude(), n1.getLatitude(), n2.getLongitude(), n2.getLatitude());
    }
    
	/**
	 * les deux fonctions qui vérifient la bonne saisie du numéro de noeud départ/destination
	 */
	public boolean verifierSaisirNoeudDepart(){
    	boolean trouveOrigine = false;
    	int i=0;
		for (; i<numNoeudGraphe ; i++){
    		if(graphe.getListeNoeuds().get(i).getId_noeud() == origine)
    			trouveOrigine = true;
		}
		return trouveOrigine;
	}
	
	public boolean verifierSaisirNoeudDest(){
    	boolean trouveDest = false;
    	int i=0;
		for (; i<numNoeudGraphe ; i++){
    		if(graphe.getListeNoeuds().get(i).getId_noeud() == destination)
    			trouveDest = true;
		}
		return trouveDest;
	}

    /**
     * fonction principale qui lance l'algorithme, affiche les résultats et le temps d'exécution
     */
    public void run(){
		    	long debut = System.nanoTime();
				algoPCC();
				tempsExecution = System.nanoTime() - debut;
    }

    // getters & setters
    
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

	public int getPointZoneRecherche() {
		return pointZoneRecherche;
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

	public void setTempsExecution(long tempsExecution) {
		this.tempsExecution = tempsExecution;
	}

	public void setPointZoneRecherche(int pointZoneRecherche) {
		this.pointZoneRecherche = pointZoneRecherche;
	}

	public HashMap<Integer, Label> getMapListeNoeudEnCommun() {
		return mapListeNoeudEnCommun;
	}

	public HashMap<Integer, Label> getMapCorrespondanceNoeudLabel() {
		return mapCorrespondanceNoeudLabel;
	}

	public void setMapCorrespondanceNoeudLabel(
			HashMap<Integer, Label> mapCorrespondanceNoeudLabel) {
		this.mapCorrespondanceNoeudLabel = mapCorrespondanceNoeudLabel;
	}
}