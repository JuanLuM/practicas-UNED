package es.uned.lsi.eped.DataStructures;

public abstract class Tree<E> extends Collection<E> implements TreeIF<E> {

	protected E root;

	Tree() {
		super();
		this.root = null;
	}
	
	/* Devuelve el elemento situado en la raíz del árbol */
	public E getRoot() {
		return this.root;
	}

	/* Decide si el árbol es una hoja */
	public boolean isLeaf() {
		return this.root!=null && getNumChildren() == 0;
	}

	/* Reimplementación de algunos métodos de Collection */
	
	/* Decide si el árbol es vacío */
	public boolean isEmpty() {
		return this.root==null;
	}
	
	/* Vacía el árbol */
	public void clear() {
		super.clear();
		this.root = null;
	}
	
	abstract public int getNumChildren();

	abstract public int getFanOut();

	abstract public int getHeight();

	abstract public IteratorIF<E> iterator(Object mode);

	abstract public boolean contains(E e);

}
