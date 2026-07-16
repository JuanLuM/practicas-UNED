package es.uned.lsi.eped.DataStructures;

public abstract class Collection<E> implements CollectionIF<E> {
	protected int size;

	/* Constructor por defecto de una colección */
	public Collection () {
		size = 0;
	}
	
	/* Devuelve el número de elementos de la colección */
	public int size() {
		return size;
	}
	
	/* Nos dice si la colección está vacía o no */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/* Vacía la colección */
	public void clear() {
		size = 0;
	}
	
	abstract public boolean contains(E e);
}
