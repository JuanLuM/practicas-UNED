package es.uned.lsi.eped.DataStructures;

/* Representa una colección de elementos, sin ningún tipo de    *
 *  relación entre ellos más que la pertenencia a la misma      *  
 *  colección.                                                  */
public interface CollectionIF<E> {

	/* Devuelve el número de elementos de la colección.         */
	public int size ();
	
	/* Devuelve true sii la colección no contiene elementos.    */	
	public boolean isEmpty ();
	
	/* Devuelve true sii e está en la colección.                */
	public boolean contains (E e);
	
	/* Elimina todos los elementos de la colección.             */
	public void clear ();
	
}
