package core ;

import java.awt.Color;
import java.io.* ;
import java.util.HashMap;
import base.Readarg ;

public class Pcc extends Algo {

    // numéro des sommets origine et destination
    protected int zoneOrigine ;
    protected int origine ;

    protected int zoneDestination ;
    protected int destination ;
    
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
	
	
	// fonction qui lit les paramètres d'entrée
	public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg) ;
		
		// demander la zone et le sommet départ.
		this.zoneOrigine = gr.getZone () ;
		this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;
	
		// demander la zone et le sommet destination.
		this.zoneDestination = gr.getZone () ;
		this.destination = readarg.lireInt ("Numero du sommet destination ? ");
    }
	
	
	// fonctions qui vérifient la bonne saisie du numéro de noeud départ / destination
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
	
	
	// initialise le tas, et met les labels des sommets dans le hashmap
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
    			nbParcouru++;
    		}
    		else {
    			mapCorrespondanceNoeudLabel.put(numNoeudCourant, new Label(i));
    		}
    	}
	}
    	
    	
    // algorithme de Dijkstra avec le tas, complexité : o(nlogn) 
    public void algoDijkstra(){
    	
    	// initialiser
    	this.initialisationAlgoDijkstra();
  
    	// tourner l'algo jusqu'à le noeud destination soit marqué
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
		        		// on choisie le noeud successeur courant
		        		Noeud noeudSuccCourant = succ.getNoeudDestination();
		        		// on obtient son label à partir de son id
		        		Label labelNoeudSuccCourant = mapCorrespondanceNoeudLabel.get(noeudSuccCourant.getId_noeud());
		        		System.out.println("" + labelNoeudSuccCourant.getId_sommetCourant());
		        		
		        		// si ce noeud successeur n'est pas encore marqué par l'algo
			    		if(!labelNoeudSuccCourant.isMarque()){
			    			double cout = 0;
			    			// on met à jour son coût courant (de sommet d'origine jusqu'ici)
			    			cout = labelCourant.getCoutCourant() + succ.getLongueurArrete();
			    			// si cette fois, le coût total obtenu est inférieur à son coût total avant
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
			    				// on le met dans le tas
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
	    	else {
		    		System.out.println("Il n'existe pas de chemin entre noueud "+ origine + " et noeud " +destination);
		    		break;
	    	}
    	} while(!mapCorrespondanceNoeudLabel.get(destination).isMarque());
    }
    
    
    // fonction qui met à jour le nombre maximal des elements dans le tas
    public void updateNbElementMaxTas(){
    	if(tasLabel.size() > nbMaxTas)
    		nbMaxTas = tasLabel.size();
    }
    
    
    // construire le plus court chemin
    public void construirePlusCourtChemin(){
    	Noeud noeudDepart = this.graphe.getListeNoeuds().get(origine);
    	Noeud noeudDestination = this.graphe.getListeNoeuds().get(destination);
    	// on met d'abords le noeud de distination dans le chemin, on cherche dans le sens inverse
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
    	}
    	// on met le chemin dans la bonne ordre
		plusCourtChemin.renverserChemin();
		
    	// compléter les information de PCC
    	plusCourtChemin.setNoeudDepart(noeudDepart);
    	plusCourtChemin.setNoeudDestination(noeudDestination);
    	plusCourtChemin.setNbNoeud(plusCourtChemin.getListeNoeudChemin().size());
    }
    
    
    // afficher des informations du chemin, et dessiner le PCC obtenu
    public void afficherPCC(){    	
    	// on construit le chemin
    	construirePlusCourtChemin();
    	
    	// affichage le nombre noeuds dans le plus court chemin
		System.out.println("size :" + plusCourtChemin.getListeNoeudChemin().size());
		
    	// affichage tous les noeuds dans le plus court chemin
		for (int i=0;i < plusCourtChemin.getNbNoeud();i++)
		{
			System.out.println("Le sommet " + i + " de PCC est : " + plusCourtChemin.getListeNoeudChemin().get(i).getId_noeud());
		}
		
    	// affichage nombre noeuds parcourus
		System.out.println("Le nombre de sommets parcourus est : " + nbParcouru);
		
    	// affichage nombre max de noeuds dans le tas
		System.out.println("Le nombre maximal des sommets dans le tas est : " + nbMaxTas);
		
    	// affichage nombre noeuds marqué
		System.out.println("Le nombre des sommets marqués : " + nbMarque);
		
    	// affichage distance de PCC
		System.out.println("La distance de PCC : " + plusCourtChemin.obetnircoutEnDistanceChemin());
		
    	// affichage temps de PCC
		System.out.println("Le temps de PCC : " + plusCourtChemin.obetnircoutEnTempsChemin());
		
		plusCourtChemin.affichageInformationChemin();
		
		// dessiner le chemin sur la carte
		plusCourtChemin.dessinerChemin(this.graphe.getDessin());
		
    }
    
    // fonction principale qui lance l'algo et afficher les résultats
    public void run() {

		System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;
		
		// si la saisie est erronée, inutile de lancer Dijkstra
		if (!verifierSaisirNoeudDepart())
			System.out.println("Le sommet d'origine " + origine + " n'existe pas dans cette carte.");
		else if (!verifierSaisirNoeudDest())
			System.out.println("Le sommet destination " + destination + " n'existe pas dans cette carte.");
		else if (origine == destination)
			System.out.println("Le plus court chemin est 0.");
		// si la saisie est bonne, on lance Dijkstra
		else {
			algoDijkstra();
			afficherPCC();
		}
    }
}