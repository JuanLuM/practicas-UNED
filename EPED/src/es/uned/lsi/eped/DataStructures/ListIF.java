package es.uned.lsi.eped.DataStructures;

/* Representa una lista de elementos. Una lista es una          *
 * secuencia que permite modificar o acceder a cualquiera de    *
 * sus elementos de forma no destructiva.                       */ 
public interface ListIF<E> extends SequenceIF<E> {

	/* Devuelve el elemento de la lista que ocupa la posición   *
	 * indicada por el parámetro.                               *	
	 * @param pos la posición comenzando en 1.                  *	
	 * @Pre: 1 <= pos <= size().                                *
	 * @return el elemento en la posición pos.                  */
	public E get (int pos);
    
	/* Modifica la posición dada por el parámetro pos para que  *
	 * contenga el valor dado por el parámetro e.               *
	 * @param pos la posición cuyo valor se debe modificar,     *
	 *  comenzando en 1.                                        *
	 * @param e el valor que debe adoptar la posición pos.      *
	 * @Pre: 1 <= pos <= size().                                */
	public void set (int pos, E e);
	
   	/* Inserta un elemento en la Lista.                         *
   	 * @param elem El elemento que hay que añadir.              *				
   	 * @param pos  La posición en la que se debe añadir elem,   *
   	 *  comenzando en 1.                                        *
 	 * @Pre: 1 <= pos <= size()+1                               */
    public void insert (int pos, E elem);
    
    /* Elimina el elemento que ocupa la posición del parámetro  *	 
     * @param pos la posición que ocupa el elemento a eliminar, *
     *  comenzando en 1                                         *
	 * @Pre: 1 <= pos <= size()                                 */
    public void remove (int pos);
}
