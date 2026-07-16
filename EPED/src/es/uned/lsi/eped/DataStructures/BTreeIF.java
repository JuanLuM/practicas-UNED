package es.uned.lsi.eped.DataStructures;

/* Representa un arbol binario de elementos, en el que un nodo	*
 * puede tener, a lo sumo, dos hijos (fan-out <= 2 para todos   *
 * los nodos).                                                  */
public interface BTreeIF<E> extends TreeIF<E>{
	/* Valor enumerado que indica los tipos de recorrido        *
	 * ofrecidos por los árboles de binarios.                   */
	public enum IteratorModes {
		PREORDER, POSTORDER, BREADTH, INORDER, RLBREADTH
	}
    
	/* Obtiene el hijo izquierdo del árbol llamante.            *
     * @return el hijo izquierdo del árbol llamante.            */
    public BTreeIF<E> getLeftChild ();
    
    /* Obtiene el hijo derecho del árbol llamante.              *
     * @return el hijo derecho del árbol llamante               */
    public BTreeIF<E> getRightChild ();

    /* Modifica la raíz del árbol.                              *
     * @param el elemento que se quiere poner como raíz del     *
     * árbol.                                                   */ 	
    public void setRoot (E e);
    
    /* Pone el árbol parámetro como hijo izquierdo del árbol    *
     * llamante. Si ya había hijo izquierdo, el antiguo dejará  *
     * de ser accesible (se pierde).                            *
     * @Pre: !isEmpty()                                         *
     * @param child el árbol que se debe poner como hijo        *
     *        izquierdo.                                        */
    public void setLeftChild (BTreeIF <E> child);
    
    /* Pone el árbol parámetro como hijo derecho del árbol      *
     * llamante. Si ya había hijo izquierdo, el antiguo dejará  *
     * de ser accesible (se pierde).                            *
     * @Pre: !isEmpty()                                         *
     * @param child el árbol que se debe poner como hijo        *
     *        derecho.	                                        */
    public void setRightChild (BTreeIF <E> child);

    /* Elimina el hijo izquierdo del árbol.                     */
    public void removeLeftChild ();
    
    /* Elimina el hijo derecho del árbol.                       */
    public void removeRightChild ();
    
}
