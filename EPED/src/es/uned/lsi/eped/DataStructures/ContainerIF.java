package es.uned.lsi.eped.DataStructures;

/* Representa un contenedor, que es una colección de elementos  *
 * que no guardan ningún orden entre sí.                        */
public interface ContainerIF<E> extends CollectionIF<E> {

	/* Añade un elemento al contenedor                          */
	public void add (E e);
	
	/* Elimina un elemento e del contenedor                     *
	 * @pre:  this.contains(e)                                  *
	 * @post: !this.contains(e)                                 */
	public void remove (E e);

	/* Devuelve un iterador para el contenedor                  */
	public IteratorIF<E> iterator (); }
