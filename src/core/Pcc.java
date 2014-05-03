package core ;

import java.awt.Color;
import java.io.* ;
import java.util.HashMap;
import base.Readarg ;

	/**
	 * La classe Pcc est pour implémenter l'algorithme de Dijkstra
	 */

public class Pcc extends Algo {

    protected int zoneOrigine ;
    protected int zoneDestination ;
    protected int origine ;
    protected int destination ;
    
    protected long tempsExecution;
    
    // existence d'un plus court chemin
	protected boolean existencePCC = false;
	
    // booléan qui permet utilisateurs à choisir coût en temps ou en distance
    protected int isEnTemps;
    
    // HashMap qui met en correspondance le numéro de noeud et son label
    private HashMap<Integer, Label> mapCorrespondanceNoeudLabel = new HashMap<Integer, Label>();
    
	// on a le tas pour y mettre tous les labels (pour ordonner le coût courant)
	BinaryHeap<Label> tasLabel= new BinaryHeap<Label>();
	
	// nombre de sommets dans le graphe
	private int numNoeudGraphe = this.graphe.getListeNoeuds().size();
	
	// nombre maximal des elements présents dans le tas
	private int nbMaxTas;
	
	// le plus court chemin obtenu par algorithme de Dijkstra
	private Chemin plusCourtChemin = new Chemin();
	
	// le nombre de sommets marqués
	private int nbMarque;
	
	// le nombre de sommets parcourus (id. placé dans le tas)
	private int nbParcouru;
	
	
	/**
	 * fonction qui lit tous les paramètres d'entrée
	 */
	public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg) ;
		
		// demander la zone et le sommet départ.
		this.zoneOrigine = gr.getZone () ;
		this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;
	
		// demander la zone et le sommet destination.
		this.zoneDestination = gr.getZone () ;
		this.destination = readarg.lireInt ("Numero du sommet destination ? ");
		
		// demander l'algo Dijkstra s'applique en distance ou en temps
		this.isEnTemps = readarg.lireInt ("En distance(0) ou en temps(1) ?");
    }

	/**
	 * les deux fonctions qui vérifient la bonne saisie du numéro de noeud départ/destination
	 */
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
	
	/**
	 * initialise le tas, et met les labels des sommets dans le hashmap
	 */
	public void initialisationAlgoDijkstra(){
 	    int i = 0;
    	// on met les labels de tous les noeuds dans la map de correspondance "numéro de noeud, label"
    	for (; i<numNoeudGraphe ; i++){
    		int numNoeudCourant = this.graphe.getListeNoeuds().get(i).getId_noeud();
    		if(numNoeudCourant == origine)
    		{
    			Label labelOrigine = new Label(origine, -1, 0, false) ;
    			mapCorrespondanceNoeudLabel.put(numNoeudCourant, labelOrigine);
    			tasLabel.insert(labelOrigine);
    			nbParcouru++;
    		}
    		else {
    			mapCorrespondanceNoeudLabel.put(numNoeudCourant, new Label(i));
    		}
    	}
	}
    	
    /**
     * algorithme de Dijkstra avec le tas
	/* la complexité : o(nlogn) 
     */
    public void algoDijkstra(){
    	
    	// commencer le calcul temps d'exécution d'algo
    	long debut = System.nanoTime();
    	// initialiser l'algo
    	this.initialisationAlgoDijkstra();
    	// tourner l'algo jusqu'à le noeud destination soit marqué
		do{
	    	// si le tas n'est pas vide
	    	if(!(tasLabel.isEmpty())) {
	        	// on choisie le label ayant le plus petit cout dans le tas
	        	Label labelCourant = tasLabel.deleteMin();
	        	// on le marque
	        	labelCourant.setMarquage(true);
	        	// on décrémente le nombre d'élément dans le tas, et incrémente le nombre d'élément marqué
	        	nbMaxTas--;
	        	nbMarque++;
	        	
	        	// pour ce label, on obtient le noeud correspondant
	        	Noeud noeudCourant = this.graphe.getListeNoeuds().get(labelCourant.getId_sommetCourant());
		        	// pour tous les successeurs (tous les arcs sortants) de ce noeud
		        	for (Successeur succ : noeudCourant.getListeSuccesseur()){
		        		// on choisie un noeud successeur courant
		        		Noeud noeudSuccCourant = succ.getNoeudDestination();
		        		// on obtient son label à partir de son numéro
		        		Label labelNoeudSuccCourant = mapCorrespondanceNoeudLabel.get(noeudSuccCourant.getId_noeud());
		        		// si ce noeud successeur n'est pas encore marqué par algo
			    		if(!labelNoeudSuccCourant.isMarque()){
			    			double cout = 0;
			    			// on met à jour son coût courant (en distance ou en temps)
			    			if(isEnTemps == 0)
			    				cout = labelCourant.getCoutCourant() + succ.getLongueurArrete();
			    			if(isEnTemps == 1)
			    				cout = labelCourant.getCoutCourant() + succ.getTempsArrete();
			    			
			    			// si cette fois, le coût total obtenu est inférieur à son coût total d'avant
			    			if (cout < labelNoeudSuccCourant.getCoutCourant()) {
			    				// on remplace l'ancien coût avec ce nouveau coût total
			    				labelNoeudSuccCourant.setCoutCourant(cout);
			    				// on change son père à ce noeud courant
			    				labelNoeudSuccCourant.setId_sommetPere(noeudCourant.getId_noeud());
			    			}
			    			
			    			// si ce noeud est déjà présent dans le tas
			    			if(tasLabel.isInHeap(labelNoeudSuccCourant)){
		    				// on met à jour le tas pour l'éventuel changement de coût de ce noeud courant
					    		tasLabel.update(labelNoeudSuccCourant);
				    		}
			    			// sinon
			    			else{
			    				// on le met dans le tas, et garde le nombre maximal d'élément dans le tas
			    				tasLabel.insert(labelNoeudSuccCourant);
			    				nbParcouru++;
			    				nbMaxTas++;
			    				updateNbElementMaxTas();
			    				// on dessine un segment entre noeudCourant et son succCourant
								this.graphe.getDessin().setColor(Color.BLUE);
								this.graphe.getDessin().setWidth(2);
								this.graphe.getDessin().drawLine(noeudCourant.getLongitude(), noeudCourant.getLatitude(), noeudSuccCourant.getLongitude(), noeudSuccCourant.getLatitude());
			    			}
			    		}
		        	} 
	    	}
	    	else
	    		// s'il n'existe pas de plus court chemin entre ces deux noeuds, on sort de cette fonction avec "return"
		    	return;
    	} while(!mapCorrespondanceNoeudLabel.get(destination).isMarque());
		// on termine le calcul de temps d'exécution de l'algo
		tempsExecution = System.nanoTime() - debut;
		// si l'algo est arrivé jusqu'ici, le Pcc existe
		existencePCC = true;
    }
    
    /**
     * fonction qui met à jour le nombre maximal des elements dans le tas
     */
    public void updateNbElementMaxTas(){
    	if(tasLabel.size() > nbMaxTas)
    		nbMaxTas = tasLabel.size();
    }
    
    /**
     * fonction qui construit le plus court chemin avec une recherche dans le sens inverse
     */
    public void construirePlusCourtChemin(){
    	Noeud noeudDepart = this.graphe.getListeNoeuds().get(origine);
    	Noeud noeudDestination = this.graphe.getListeNoeuds().get(destination);
    	// on place le noeud de destination dans le chemin
    	plusCourtChemin.addNoeud(noeudDestination);	
    	Label labelCourant = mapCorrespondanceNoeudLabel.get(destination);
    	// on cherche parmi tous les noeuds qui ont été marqués
    	int i = 0;
    	for(;i < nbMarque;i++){
    		// si le noeud courant n'est pas le noeud départ
    		while(labelCourant.getId_sommetPere() != -1){
    			// on cherche son père, puis ajoute le père dans le chemin
    			Noeud noeudCourant = this.graphe.getListeNoeuds().get(labelCourant.getId_sommetPere());
    			plusCourtChemin.addNoeud(noeudCourant);
    			labelCourant = mapCorrespondanceNoeudLabel.get(labelCourant.getId_sommetPere());
    		}
    	}
    	// on met le chemin dans la bonne ordre
		plusCourtChemin.renverserChemin();
    	// compléter les information de PCC
    	plusCourtChemin.setNoeudDepart(noeudDepart);
    	plusCourtChemin.setNoeudDestination(noeudDestination);
    	plusCourtChemin.setNbNoeud(plusCourtChemin.getListeNoeudChemin().size());
    }
    
    /**
     * afficher des informations de plus court chemin et le dessiner
     */
    public void afficherPCC(){    	
    	
    	// on construit le chemin
    	construirePlusCourtChemin();
    	
    	// affichage le nombre noeuds dans Pcc
		System.out.println("Le nombre de noeud dans le plus court chemin est: " + plusCourtChemin.getListeNoeudChemin().size());
		
    	// affichage de tous les noeuds dans le Pcc
		for (int i=0;i < plusCourtChemin.getNbNoeud();i++)
		{
			System.out.println("Le sommet " + (i+1) + " est: " + plusCourtChemin.getListeNoeudChemin().get(i).getId_noeud());
		}
		
    	// affichage nombre de noeuds parcourus
		System.out.println("Le nombre de sommets parcourus est : " + nbParcouru);
		
    	// affichage nombre max de noeuds dans le tas
		System.out.println("Le nombre maximal des sommets dans le tas est : " + nbMaxTas);
		
    	// affichage nombre de noeuds marqué
		System.out.println("Le nombre des sommets marqués : " + nbMarque);
		
    	// affichage en distance de PCC
		if(isEnTemps == 0)
			plusCourtChemin.affichageCoutEnDistance();
    	// affichage en temps de PCC
		if(isEnTemps == 1)
			plusCourtChemin.affichageCoutEnTemps();
		
		// dessiner le chemin sur la carte
		plusCourtChemin.dessinerChemin(this.graphe.getDessin());
    }
    
    /**
     * fonction principale qui lance l'algo, affiche les résultats et donne le temps d'exécution de l'algo
     */
    public void run() {

		System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;
		
		// si la saisie est erronée ou c'est le même noeud, inutile de lancer Dijkstra
		if (!verifierSaisirNoeudDepart())
			System.out.println("Le sommet d'origine " + origine + " n'existe pas dans cette carte.");
		else if (!verifierSaisirNoeudDest())
			System.out.println("Le sommet destination " + destination + " n'existe pas dans cette carte.");
		else if (origine == destination)
			System.out.println("Le plus court chemin est 0.");
		// si la saisie est bonne, on lance Dijkstra
		else {
			algoDijkstra();
			// s'il existe le Pcc, on affiche des informations et le temps exécution
			if(existencePCC == true){
				afficherPCC();
				System.out.println("Le temps d'exécution de algo Dijkstra est "+ tempsExecution + " ns.");
			}
			// sinon
			else {
	    		System.out.println("Il n'existe pas de chemin entre noueud "+ origine + " et noeud " +destination);
	    		System.out.println("Algo Dijkstra est arrêté.");
			}
		}
    }
}