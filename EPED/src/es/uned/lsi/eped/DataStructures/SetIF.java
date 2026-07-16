package es.uned.lsi.eped.DataStructures;

/* Representa un conjunto, que es un contenedor que permite     *
 * almacenar elementos que serán únicos dentro del conjunto.    */
public interface SetIF<E> extends ContainerIF<E> {

	/* Realiza la unión del conjunto llamante con el parámetro  */
	public void union (SetIF<E> s);
	
	/* Realiza la intersección del conjunto llamante con el     *
	 * parámetro                                                */
	public void intersection (SetIF<E> s);
	
	/* Realiza la diferencia del conjunto llamante con el       *
	 * parámetro (los elementos que están en el llamante pero   *
	 * no en el parámetro)                                      */
	public void difference (SetIF<E> s);
	
	/* Devuelve true sii el conjunto parámetro es subconjunto   *
	 * del llamante                                             */
	public boolean isSubset (SetIF<E> s);
}
