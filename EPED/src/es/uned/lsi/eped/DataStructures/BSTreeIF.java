package es.uned.lsi.eped.DataStructures;

/* Representa un árbol de búsqueda binaria, en el que los       *
 * elementos se organizan automáticamente según su orden.       */
public interface BSTreeIF<E extends Comparable<E>> extends TreeIF<E> {
	/* Valor enumerado que indica los tipos de recorrido        *
	 * ofrecidos por los árboles de búsqueda binaria.           */
	public enum IteratorModes {
		DIRECTORDER, REVERSEORDER
	}
	
	/* Valor enumerado que indica cuál es la ordenación de los  *
	 * elementos dentro del árbol (ascendente o descendente).   */
	public enum Order {
		ASCENDING, DESCENDING
	}

	/* Añade un elemento no contenido previamente en el árbol   *
	 * @Pre: !contains(e)                                       *
	 * @Post: contains(e)                                       */
	public void add(E e);
	
	/* Elimina un elemento previamente contenido en el árbol    *
	 * @Pre: contains(e)                                        *
	 * @Post: !contains(e)                                      */
	public void remove(E e);

	public Order getOrder();
	
}
