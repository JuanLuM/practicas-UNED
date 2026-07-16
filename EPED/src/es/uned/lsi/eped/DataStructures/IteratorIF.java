package es.uned.lsi.eped.DataStructures;

/* Representa un iterador de elementos.                         */
public interface IteratorIF<E> {

	/* Obtiene el siguiente elemento de la iteración.           *
     * @pre:  hasNext ()                                        *
     * @return el siguiente elemento de la iteración,           */
	  public E getNext ();
	    
	 /* Comprueba si aún quedan elementos por iterar.           *
	  * @return true sii el iterador dispone de más elementos.  */
	  public boolean hasNext ();
	    
	 /* Vuelve la posición del iterador al principio. Esto      *
	  * permite reutilizar un iterador sin crear otro nuevo.    */
	  public void reset ();
}
