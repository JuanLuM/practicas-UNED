package es.uned.lsi.eped.pract2023_2024;

/* Representación de una canción                                             */
public interface TuneIF {

  /* Dado un objeto QueryIF conteniendo unos criterios de búsqueda, devuelve */
  /* un valor de verdad indicando si la canción los cumple o no los cumple   */
  /* @param   -un objeto QueryIF con unos criterios de búsqueda              */
  /* @return  -si la canción cumple TODOS los criterios, devolverá verdadero */
  /*          -si la canción incumple algún criterio, devolverá falso        */
  public boolean match(QueryIF q);
}

