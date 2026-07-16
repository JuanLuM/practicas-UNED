package es.uned.lsi.eped.DataStructures;

/* Representa un multiconjunto, que es un contenedor que        *
 * permite almacenar elementos de los que puede haber múltiples *
 * instancias dentro del multiconjunto.                         */
public interface MultiSetIF<E> extends ContainerIF<E> {

	/* Añade varias instancias de un elemento al multiconjunto  *
	 * @pre:  n > 0 && premult = multiplicity(e)                *
	 * @post: multiplicity(e) = premult + n                     */
	public void addMultiple (E e, int n);
	
	/* Elimina varias instancias de un elemento del             *
	 * multiconjunto                                            *
	 * @pre: 0<n<= multiplicity(e) && premult = multiplicity(e) *
	 * @post: multiplicity(e) = premult - n                     */
	public void removeMultiple (E e, int n);

	/* Devuelve la multiplicidad de un elemento dentro del      *
	 * multiconjunto.                                           *
	 * @return: multiplicidad de e (0 si no está contenido)     */
	public int multiplicity (E e);
	
	/* Realiza la unión del multiconjunto llamante con el       *
	 * parámetro                                                */
	public void union (MultiSetIF<E> s);
	
	/* Realiza la intersección del multiconjunto llamante con   *
	 * el parámetro                                             */
	public void intersection (MultiSetIF<E> s);
	
	/* Realiza la diferencia del multiconjunto llamante con el  *
	 * parámetro (los elementos que están en el llamante pero   *
	 * no en el parámetro                                       */
	public void difference (MultiSetIF<E> s);
	
	/* Devuelve cierto sii el parámetro es un submulticonjunto  *
	 * del llamante                                             */
	public boolean isSubMultiSet (MultiSetIF<E> s);
}
