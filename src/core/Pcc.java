package core ;

import base.* ;
import java.awt.Color;
import java.util.HashMap;
import java.io.PrintStream;

/**
 * La classe Pcc est pour calculer le plus court chemin à la fois en algorithme Dijkstra Standard et en Dijkstra A Star.
 * Dans le but de minimiser la duplication de code, aucune autre classe n'a été créée.
 */

public class Pcc extends Algo {

    protected int origine ;
    protected int destination ;
    protected int zoneOrigine ;
    protected int zoneDestination ;

	// nombre de sommets dans le graphe
	private int numNoeudGraphe = graphe.getListeNoeuds().size();
	
    // HashMap qui met en correspondance le numéro de noeud et son label
    private HashMap<Integer, Label> mapCorrespondanceNoeudLabel = new HashMap<Integer, Label>();
    
	// un tas binaire pour y mettre tous les labels, afin d'ordonner le coût courant de label
	BinaryHeap<Label> tasLabel= new BinaryHeap<Label>();
	
	// le plus court chemin obtenu par l'algorithme
	private Chemin plusCourtChemin = new Chemin();
	
	// le nombre de sommets parcourus (= ont été placé dans le tas)
	private int nbParcouru;
	
	// nombre maximal des éléments présents dans le tas
	private int nbMaxTas;
	
	// le nombre de sommets marqués
	private int nbMarque;
	
    // booléen qui permet à l'utilisateur de choisir le calcul en distance(0) ou en temps(1)
	private int isEnTemps;
    
    // booléen qui permet à l'utilisateur de choisir entre algorithme Dijkstra Standard(0) ou A Star(1)
    private int choixAlgo;
    
    // le temps d'exécution de l'algorithme
    private long tempsExecution;
    
    // booléen qui indique l'existence d'un plus court chemin, initialisé comme faux
	private boolean existencePCC = false;
	
	// booléen qui indique si l'on est en mode "test"
	private boolean enModeTest = false ;
	
	// booléen qui indique si l'on est en algorithme pour covoiturage
	private int isCovoiturage = 0 ;
	
	// booléen qui indique si l'affichage du parcours de recherche
	private int isDisplay = 0 ;
	
	/**
	 * constructeurs
	 */
	public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg) ;
		
		// demander la zone et le sommet de départ.
		this.zoneOrigine = gr.getZone () ;
		this.origine = readarg.lireInt ("Numéro du sommet d'origine ? ") ;
		// demander la zone et le sommet destination.
		this.zoneDestination = gr.getZone () ;
		this.destination = readarg.lireInt ("Numéro du sommet destination ? ");
		// demander l'utilisateur de choisir entre Dijsktra Standard et A Star
		this.choixAlgo = readarg.lireInt ("Algorithme Dijkstra Standard(0) ou Dijkstra A Star(1) ?");
		// demander l'utilisateur de choisir entre temps et distance
		this.isEnTemps = readarg.lireInt ("Le plus court chemin en : distance(0) ou temps(1) ?");
		// demander l'utilisateur si l'affichage de parcour
		this.isDisplay = readarg.lireInt ("Voulez-vous afficher le parcours de recherche Pcc : (0) Non (1) Oui ?");
    }
	
	// constructeur spécifique pour lancer le test
    public Pcc(Graphe gr, int origine, int destination, int choixAlgo, int isEnTemps) {
		super(gr);
		this.origine = origine;
		this.destination = destination;
		this.choixAlgo = choixAlgo ;
		this.isEnTemps = isEnTemps;
		// on passe en mode test
		this.enModeTest = true ;
    }
    
	// constructeur spécifique pour les problèmes sous contraintes
    public Pcc(Graphe gr, int origine, int destination){
		super(gr);
		this.origine = origine;
		this.destination = destination;
		this.enModeTest = true ;
		this.isEnTemps = 1 ;
		this.isCovoiturage = 1 ;
    }
    
    // constructeur par défault
    public Pcc(){
		super(Algo.graphe);
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
		        	// si c'est un chemin non routier, on ne le traite pas
		        	if(succ.getDescripteur().getType() != 'z'){
		        		// on choisit un noeud successeur courant
		        		Noeud noeudSuccCourant = succ.getNoeudDestination();
		        		Label labelNoeudSuccCourant = mapCorrespondanceNoeudLabel.get(noeudSuccCourant.getId_noeud());
		        		// si ce noeud successeur n'est pas encore marqué par algo
		        		if(!labelNoeudSuccCourant.isMarque()){
		        			double coutSuccCourantNouveau = 0 ;
		        			// le choix de l'utilisateur entre distance et temps
		        			if(isEnTemps == 0) 
		        				coutSuccCourantNouveau = labelCourant.getCoutCourant() + succ.getLongueurArete();
		        			else
		        				coutSuccCourantNouveau = labelCourant.getCoutCourant() + succ.getTempsArete();
		        			
			    			// si le coût nouveau est inférieur à son coût avant, on remplace le coût et change son père
			    			if (coutSuccCourantNouveau < labelNoeudSuccCourant.getCoutCourant()) {
			    				labelNoeudSuccCourant.setCoutCourant(coutSuccCourantNouveau);
			    				labelNoeudSuccCourant.setId_sommetPere(noeudCourant.getId_noeud());
				    			// si l'utilisateur lance A*
				    			if (choixAlgo == 1){
				    				Noeud noeudDestination = graphe.getListeNoeuds().get(destination);
				    				// on calcule estimation en distance ou en temps, selon le choix de l'utilisateur, et le stocke dans label
				    				if(isEnTemps == 0){
						    			double estimationDistance = Graphe.distanceEntreDeuxNoeuds(noeudSuccCourant, noeudDestination);
						    			labelNoeudSuccCourant.setCoutEstimation(estimationDistance);
				    				}
				    				else{
						    			double estimationTemps = Graphe.tempsEntreDeuxNoeud(noeudSuccCourant, noeudDestination);
						    			labelNoeudSuccCourant.setCoutEstimation(estimationTemps);
				    				}
				    			}
			    			}
		        			// si ce noeud successeur n'est pas encore parcouru
		        			if(!tasLabel.isInHeap(labelNoeudSuccCourant)){
		        				tasLabel.insert(labelNoeudSuccCourant);
		        				nbParcouru++;
		        				if(isDisplay == 1)
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
	    	else
	    	// s'il n'existe pas de pcc entre ces deux noeuds
		    return;
		} while(!mapCorrespondanceNoeudLabel.get(destination).isMarque());
		// si la destination est marquée, le plus court chemin existe
		existencePCC = true;
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
    	// on met le chemin dans le bon ordre
		plusCourtChemin.renverserChemin();
    	// compléter les information de PCC
    	plusCourtChemin.setNoeudDepart(noeudDepart);
    	plusCourtChemin.setNoeudDestination(noeudDestination);
    	plusCourtChemin.setNbNoeud(plusCourtChemin.getListeNoeudChemin().size());
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
     * fonction qui dessine le plus court chemin
     */
	public void afficherPlusCourtChemin(){
		plusCourtChemin.dessinerChemin(graphe.getDessin());
	}
	
    /**
     * fonction qui affiche des informations concernant le plus court chemin obtenu, et puis le dessiner
     */
    public void afficherPCC(){
    	
    	// on construit le plus court chemin
    	construirePlusCourtChemin();
    	System.out.println();
    	System.out.println("****** INFORMATION DU PLUS COURT CHEMIN ******");
    	System.out.println();
		System.out.println(plusCourtChemin.getListeNoeudChemin().size() + " sommets sont présents dans le plus court chemin.");
		System.out.println(nbParcouru + " sommets ont été parcourus par l'algorithme.");
		System.out.println(nbMaxTas + " est le nombre maximal des sommets dans le tas.");
		System.out.println(nbMarque + " sommets sont marqués.");
    	// affichage de la distance de PCC
		if(isEnTemps == 0)
			plusCourtChemin.affichageCoutEnDistance();
    	// affichage du temps de PCC
		if(isEnTemps == 1)
			plusCourtChemin.affichageCoutEnTemps();
		// dessiner le chemin sur la carte
		afficherPlusCourtChemin();
    }
    
    /**
     * fonction qui affiche des informations en mode test (ligne par ligne)
     * format : sommet origine / sommet destination / coût en distance (en mètre) ou en temps (en seconde) selon le choix / 
     * nombre de sommets parcourus / nombre de sommets marqués / nbMaxTas / temps d'exécution.
     */
    public void afficherPCCModeTest(){
    	
    	construirePlusCourtChemin();
		if(isEnTemps == 0){
			System.out.println(
					plusCourtChemin.getNoeudDepart().getId_noeud() + " " + 
		plusCourtChemin.getNoeudDestination().getId_noeud() + " "
							+ plusCourtChemin.getCoutEnDistanceCheminModeTest() + " "
							+ nbParcouru + " "
							+ nbMarque + " "
							+ nbMaxTas + " "
							+ tempsExecution );
		}
		// dans la méthode getCoutEnTempsCheminModeTest(), on prend au maximal 2 chiffres après la virgules
		if(isEnTemps == 1)
			System.out.println(
					plusCourtChemin.getNoeudDepart().getId_noeud() + " " + 
		plusCourtChemin.getNoeudDestination().getId_noeud() + " "
							+ plusCourtChemin.getCoutEnTempsCheminModeTest() + " "
							+ nbParcouru + " "
							+ nbMarque + " "
							+ nbMaxTas + " "
							+ tempsExecution );
    }
    
    /**
     * fonction principale qui lance l'algorithme, affiche les résultats et le temps d'exécution
     */
    public void run(){
    	
    	// si on n'est pas en mode "test"
	    if(!enModeTest){
			System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;
			// si la saisie est erronée, ou c'est du même noeud, inutile de lancer l'algorithme
			if (!verifierSaisirNoeudDepart())
				System.out.println("Le sommet d'origine " + origine + " n'existe pas dans cette carte.");
			else if (!verifierSaisirNoeudDest())
				System.out.println("Le sommet destination " + destination + " n'existe pas dans cette carte.");
			else if (origine == destination)
				System.out.println("Le plus court chemin est 0.");
			// si la saisie est bonne, on lance l'algorithme
			else {
		    	// on commence à calculer le temps d'execution
		    	long debut = System.nanoTime();
				algoPCC();
				// si l'algorithme a terminé, on arrête le temps d'exécution de l'algo
				tempsExecution = System.nanoTime() - debut;
				// s'il existe le Pcc, on affiche des informations et le temps exécution
				if (existencePCC == true){
					afficherPCC();
					// on affiche selon le choix de l'algo
					if(choixAlgo == 0)
						System.out.println("Le temps d'exécution de l'algorithme Dijkstra est : ");
					else
						System.out.println("Le temps d'exécution de l'algorithme A Star est : ");
				System.out.println(tempsExecution + " ns.");
				}
				// s'il n'existe pas de Pcc
				else {
					System.out.println("**********");
		    		System.out.println("Il n'existe pas de chemin entre noueud " + origine + " et noeud " + destination);
		    		System.out.println("Algorithme est arrêté.");
				}
			}
	    }
	    // si on est en mode "test", on simplifie l'affichage
        else {
        	long debut = System.nanoTime();
        	if (origine != destination)
        		algoPCC();
    		// si l'algorithme a terminé, on arrête le temps d'exécution de l'algo
    		tempsExecution = System.nanoTime() - debut;
    		// s'il existe le Pcc, on affiche des informations et le temps exécution
    		if(existencePCC == true && isCovoiturage == 0){
    			afficherPCCModeTest();
    		}
        }
    }
}