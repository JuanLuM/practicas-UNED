package es.uned.lsi.eped.DataStructures;

/* Representa un árbol, que es una colección cuyos elementos se *
 * organizan jerárquicamente.                                   */

public interface TreeIF<E> extends CollectionIF<E> {

	/* Obtiene el elemento situado en la raíz del árbol         *
     * @Pre: !isEmpty ();                                       *
     * @return el elemento que ocupa la raíz del árbol.         */
	public E getRoot ();

	/* Decide si el árbol es una hoja (no tiene hijos)          *
	 * @return true sii el árbol es no vacío y no tiene hijos   */
	public boolean isLeaf();	
	
	/* Devuelve el número de hijos del árbol                    */
	public int getNumChildren ();
	
	/* Devuelve el fan-out del árbol: el número máximo de hijos *
	 * que tiene cualquier nodo del árbol                       */
	public int getFanOut ();
	
	/* Devuelve la altura del árbol: la distancia máxima desde  *
	 * la raíz a cualquiera de sus hojas                        */
    public int getHeight ();
	
    /* Obtiene un iterador para el árbol.                       *
     * @param mode el tipo de recorrido indicado por los        * 
     * valores enumerados definidos en cada TAD concreto.       */
    public IteratorIF<E> iterator (Object mode);    

}
