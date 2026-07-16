package es.uned.lsi.eped.DataStructures;

/* Representa un arbol general de elementos, en el que un nodo  *
 * puede tener cualquier número de hijos.                       */
public interface GTreeIF<E> extends TreeIF<E> {
	/* Valor enumerado que indica los tipos de recorridos       *
	 * ofrecidos por los árboles generales.                     */
	public enum IteratorModes {
		PREORDER, POSTORDER, BREADTH
	}
    
    /* Modifica la raíz del árbol.                              *
     * @param el elemento que se quiere poner como raíz del     *
     * árbol.                                                   */ 	
     public void setRoot (E e);
     
    /* Obtiene los hijos del árbol llamante.                    *
     * @return la lista de hijos del árbol (en el orden en que  *
     * estén almacenados en el mismo.                           */
     public ListIF <GTreeIF<E>> getChildren ();

    /* Obtiene el hijo que ocupa la posición dada por parámetro.*
     * @param pos la posición del hijo que se desea obtener,    *
     *  comenzando en 1.                                        *
     * @Pre 1 <= pos <= getChildren ().size ();                 *
     * @return el árbol hijo que ocupa la posición pos.         */
     public GTreeIF<E> getChild (int pos);
		
    /* Inserta un árbol como hijo en la posición pos.           *
     * @Pre: !isEmpty()                                         *
     * @param pos la posición que ocupará el árbol entre sus    *
     * hermanos, comenzando en 1.                               *
     * Si pos == getChildren ().size () + 1, se añade como      *
     * último hijo.                                             *
     * @param e el hijo que se desea insertar.                  *
     * @Pre 1<= pos <= getChildren ().size () + 1               */
     public void addChild (int pos, GTreeIF<E> e);
    
    /* Elimina el hijo que ocupa la posición parámetro.         *
     * @param pos la posición del hijo con base 1.              *
     * @Pre 1 <= pos <= getChildren ().size ();                 */
     public void removeChild (int pos);
       
}
