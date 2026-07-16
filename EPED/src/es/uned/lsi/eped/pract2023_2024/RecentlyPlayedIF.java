package es.uned.lsi.eped.pract2023_2024;

import es.uned.lsi.eped.DataStructures.ListIF;

/* Representación del almacén de las últimas canciones reproducidas          */
public interface RecentlyPlayedIF {

  /* Devuelve los identificadores de las últimas canciones reproducidas en   */
  /* el orden inverso al que fueron reproducidas                             */
  /* @return  una lista con los identificadores de las últimas canciones     */
  /*          reproducidas (en el orden inverso al que se reprodujeron)      */
  public ListIF<Integer> getContent();

  /* Añade la última canción reproducida                                     */
  /* @param   -un entero con el identificador de la última canción           */
  /*          reproducida                                                    */
  /* @pos     -se añade el identificador a la estructura que almacena las    */
  /*          últimas canciones reproducidas, garantizándose que no se       */
  /*          almacenan más canciones que las marcadas por el valor máximo   */
  /*          permitido indicado en el constructor                           */
  public void addTune(int tuneID);
}

