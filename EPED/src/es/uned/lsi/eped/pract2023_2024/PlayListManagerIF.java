package es.uned.lsi.eped.pract2023_2024;

import es.uned.lsi.eped.DataStructures.ListIF;

/* Representación del gestor de listas de reproducción                       */
public interface PlayListManagerIF {

  /* Comprueba si existe una lista de reproducción dado su identificador     */
  /* @param   -una cadena de caracteres no vacía con el identificador de la  */
  /*          lista de reproducción que queremos saber si existe o no        */
  /* @return  -un valor booleano indicando si existe o no una lista de       */
  /*          reproducción asociada al identificador recibido como parámetro */
  public boolean contains(String playListID);

  /* Devuelve la lista de reproducción asociada a un identificador           */
  /* @param   -una cadena de caracteres no vacía con el identificador de la  */
  /*          lista de reproducción que queremos recuperar                   */
  /* @pre     -existe una lista de reproducción asociada al identificador    */
  /*          que se recibe como parámetro                                   */
  /* @return  -la lista de reproducción asociada al identificador recibido   */
  /*          como parámetro                                                 */
  public PlayListIF getPlayList(String playListID);

  /* Devuelve una lista con todos los identificadores de las listas de       */
  /* reproducción existentes                                                 */
  /* @return  -una lista de cadenas de caracteres (todas no vacías) que son  */
  /*          los identificadores de todas las listas de reproducción        */
  /*          existentes                                                     */
  public ListIF<String> getIDs();

  /* Crea una nueva lista de reproducción vacía y la asocia a un nuevo       */
  /* identificador                                                           */
  /* @param   -una cadena de caracteres no vacía con el identificador de la  */
  /*          lista de reproducción que queremos crear                       */
  /* @pre     -no existe ninguna lista de reproducción asociada al           */
  /*          identificador recibido como parámetro                          */
  public void createPlayList(String playListID);

  /* Elimina una lista de reproducción asociada a un identificador           */
  /* @param   -una cadena de caracteres no vacía con el identificador de la  */
  /*          lista de reproducción que queremos eliminar                    */
  /* @pre     -existe una lista de reproducción asociada al identificador    */
  /*          recibido como parámetro                                        */
  public void removePlayList(String playListID);
}

