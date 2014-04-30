// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// Comparable deleteMin( )--> Return and remove smallest item
// Comparable findMin( )  --> Return smallest item
// boolean isEmpty( )     --> Return true if empty; else false
// ******************ERRORS********************************
// Throws RuntimeException for findMin and deleteMin when empty


package core ;
import java.util.* ;

/**
 * Implements a binary heap.
 * Note that all "matching" is based on the compareTo method.
 * @author Mark Allen Weiss
 * @author DLB
 */


/* BinaryHeap : 
 * 
 * 1. is a heap in which each node can only have two child-node : binary
 * 2. when adding a node, first add to parent-node's left, then to its right	
 * 3. for a "min-heap", a parent node is always smaller or equal to its child-node(s)
 */

// paramètre d'entrée : element E extends Comparable<E>

public class BinaryHeap<E extends Comparable<E>> {

    private int currentSize; // Number of elements in heap

    // Java genericity does not work with arrays.
    // We have to use an ArrayList
    private ArrayList<E> array; // The heap array
    
    // HashMap is used to find the index of element E
    private HashMap<E, Integer> hashMap = new HashMap<E, Integer>(); 

    /**
     * Construct the binary heap.
     */
    public BinaryHeap() {
        this.currentSize = 0;
        this.array = new ArrayList<E>() ;
    }

    // Constructor used for debug.
    private BinaryHeap(BinaryHeap<E> heap) {
		this.currentSize = heap.currentSize ;
		this.array = new ArrayList<E>(heap.array) ;
    }

    // Sets an element in the array
    private void arraySet(int index, E value) {
    	//for the first element
		if (index == this.array.size()) {
		    this.array.add(value) ;
	}
	else {
	    this.array.set(index, value) ;
	}
    }

    /**
     * Test if the heap is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() { return this.currentSize == 0; }
    
    /**
     * Returns size.
     * @return current size.
     */
    public int size() { return this.currentSize; }
 
    /**
     * Returns index of parent.
     */
    private int index_parent(int index) {
		return (index - 1) / 2 ;
    }

    /**
     * Returns index of left child.
     */
    private int index_left(int index) {
		return index * 2 + 1 ;
    }

    /**
     * Insert into the heap.
     * @param x the item to insert.
     */
    public void insert(E x) {
    	// if currentSize=0-> index = 0 -> currentSize = 1
		int index = this.currentSize++ ;
		this.arraySet(index, x) ;
		// we put x in hashmap
		this.hashMap.put(x, index);
		this.percolateUp(index) ;
    }

    /**
     * Internal method to percolate up in the heap.
     * @param index the index at which the percolate begins.
     */
    private void percolateUp(int index) {
    	// get element x by its index
		E x = this.array.get(index) ;
	
		//if x (which is on "index" place) is smaller than its parent node ->swap with its parent (percolateUp)
	        for( ; index > 0 && x.compareTo(this.array.get(index_parent(index)) ) < 0; index = index_parent(index) ) {
		    E moving_val = this.array.get(index_parent(index)) ;
	            this.arraySet(index, moving_val) ;
	            this.hashMap.put(moving_val, index);
	}

        this.arraySet(index, x) ;
        this.hashMap.put(x, index);
    }

    /**
     * Internal method to percolate down in the heap.
     * @param index the index at which the percolate begins.
     */
	    private void percolateDown(int index) {
		int ileft = index_left(index) ;
		int iright = ileft + 1 ;
	
		if (ileft < this.currentSize) {
		    E current = this.array.get(index) ;
		    E left = this.array.get(ileft) ;
		    boolean hasRight = iright < this.currentSize ;
		    // if right element exists, return its index, if not, return null
		    E right = (hasRight)?this.array.get(iright):null ;
		    
		    if (!hasRight || left.compareTo(right) < 0) {
			// Left is smaller
			if (left.compareTo(current) < 0) {
			    this.arraySet(index, left) ;
			    this.hashMap.put(left, index);
			    this.arraySet(ileft, current) ;
			    this.hashMap.put(current,ileft);
			    this.percolateDown( ileft ) ;
			}
		    }
		    else {
			// Right is smaller
			if (right.compareTo(current) < 0) {
			    this.arraySet(index, right) ;
			    this.hashMap.put(right,index);
			    this.arraySet(iright, current) ;
			    this.hashMap.put(current,iright);
			    this.percolateDown( iright ) ;
		}		
	    }
	}
    }

    /**
     * Find the smallest item in the heap.
     * @return the smallest item.
     * @throws Exception if empty.
     */
    public E findMin( ) {
        if( isEmpty() )
            throw new RuntimeException( "Empty binary heap" );
        return this.array.get(0);
    }
    
    /**
     * Remove the smallest item from the heap.
     * @return the smallest item.
     * @throws Exception if empty.
     */
    public E deleteMin( ) {
        E minItem = findMin( );
		E lastItem = this.array.get(--this.currentSize) ;
        this.arraySet(0, lastItem) ;
        this.percolateDown( 0 );
        return minItem;
    }
    
    
    // we update the heap both up and down
    public void update(E element){
    	// we get indice of element by its value
    	int indice = this.hashMap.get(element);
    	this.percolateUp(indice);
    	this.percolateDown(indice);
    }
    
    /**
     * Prints the heap
     */
    public void print() {
		System.out.println() ;
		System.out.println("========  HEAP  (size = " + this.currentSize + ")  ========") ;
		System.out.println() ;
	
		for (E el : this.array) {
		    System.out.println(el.toString()) ;
		}
	
		System.out.println() ;
		System.out.println("--------  End of heap  --------") ;
		System.out.println() ;
    }

    /**
     * Prints the elements of the heap according to their respective order.
     */
    public void printSorted() {
	
		BinaryHeap<E> copy = new BinaryHeap<E>(this) ;
	
		System.out.println() ;
		System.out.println("========  Sorted HEAP  (size = " + this.currentSize + ")  ========") ;
		System.out.println() ;
	
		while (!copy.isEmpty()) {
		    System.out.println(copy.deleteMin()) ;
		}
	
		System.out.println() ;
		System.out.println("--------  End of heap  --------") ;
		System.out.println() ;
	}


    
    // Test program : compare with the reference implementation PriorityQueue.
    // PriorityQueue : a priority heap implemented in Java
    public static void main(String [] args) {
        BinaryHeap<Integer> heap = new BinaryHeap<Integer>() ;
        PriorityQueue<Integer> queue = new PriorityQueue<Integer>() ;
	
		int count = 0 ;
		int blocksize = 10000 ;
	
		System.out.println("Interrupt to stop the test.") ;
		
		while (true) {
	
		    // Insert up to blocksize elements
		    int nb_insert = (int)(Math.random() * (blocksize + 1)) ;
		    
		    for (int i = 0 ; i < nb_insert ; i++) {
				Integer obj = new Integer(i) ;
				heap.insert(obj) ;
				queue.add(obj) ;
		    }
	
		    // Remove up to blocksize elements
		    int nb_remove = (int)(Math.random() * blocksize * 1.1) ;
		    
		    if (nb_remove > queue.size()) {
				nb_remove = queue.size() ;
		    }
	
		    for (int i = 0 ; i < nb_remove ; i++) {
	
				int removed1 = queue.poll().intValue() ;
				int removed2 = heap.deleteMin().intValue() ;
				
				if (removed1 != removed2) {
				    System.out.println("Ouch : expected " + removed1 + "  .. but got " + removed2) ;
				    System.exit(1) ;
				}
		    }
	
		    if (heap.size() != queue.size()) {
			    System.out.println("Ouch : heap size = " + heap.size() + "  queue size = " + queue.size() ) ;
			    System.exit(1) ;
			}
	
		    count += nb_remove ;
		    
		    if (count > 1000000) {
				System.out.println("" + count + " items successfully compared. Heap size : " + heap.size()) ;
				count = 0 ;
		    }
		}
    }
}
