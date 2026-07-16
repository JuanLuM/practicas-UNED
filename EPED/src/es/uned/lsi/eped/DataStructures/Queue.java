package es.uned.lsi.eped.DataStructures;

public class Queue<E> extends Sequence<E> implements QueueIF<E> {

	private NodeSequence lastNode;

	/* Constructor por defecto: crea una cola vacía */
	public Queue(){
		super();              /* Construimos la secuencia vacía */
		this.lastNode = null; /* No hay último nodo */
	}
	
	/* Constructor por copia: delega en el constructor por copia *
	 * de la secuencia                                           */
    public Queue(Queue<E> s) {
		super(s);  /* Copiamos la secuencia de la cola original */
		/* Recorremos la secuencia de la cola copia para encontrar su último nodo */
		if ( this.isEmpty() ) {
			this.lastNode = null;
		} else {
			NodeSequence node = this.getNode(this.size);
			this.lastNode = node;
		}
    }
    
	/* Devuelve el primer elemento de la cola */
	public E getFirst() {
		return this.firstNode.getValue();
	}

	/* Añade un nuevo elemento al final de la cola */
	public void enqueue(E elem) {
		NodeSequence newNode = new NodeSequence(elem);
		if(isEmpty()){
			this.firstNode = newNode;
		} else{
			this.lastNode.setNext(newNode);
		}
		this.lastNode = newNode;
		this.size++;
	}

	/* Elimina el primer elemento de la cola */
	public void dequeue() {
		this.firstNode = this.firstNode.getNext();
		this.size--;
		if(this.size == 0){
			this.lastNode = null;
		}
	}
	
	/* Vacía la cola */
	public void clear() {
		super.clear();   /* Vaciamos la secuencia */
		this.lastNode = null; /* No hay último nodo */
	}
	
}
