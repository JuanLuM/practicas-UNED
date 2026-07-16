package es.juanlumg;

/*
 * Clase principal desde las que hago las llamadas
 */
public class Fabrica {
    public static void main(String[] args) throws Exception {

        FabricaEntorno entorno = new FabricaEntorno(args);
        AsignadorTareas asignadorTareas = new AsignadorTareas(entorno);
        // TODO: Descomentar para el análisis
        // long tiempoInicio = System.currentTimeMillis();
        asignadorTareas.empezar();
         // TODO: Descomentar para el análisis
        // long tiempoFin = System.currentTimeMillis();
        // System.out.println((tiempoFin - tiempoInicio) + ";" + entorno.obtenerNumeroDeNodos());
    }
}
