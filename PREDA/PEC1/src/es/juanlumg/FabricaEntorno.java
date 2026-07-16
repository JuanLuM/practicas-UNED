package es.juanlumg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.zip.DataFormatException;

public class FabricaEntorno {

    private Nodo nodoSolucionFinal;
    private boolean muestraAyuda = false;
    private boolean muestraTraza = false;
    private String ficheroEntrada = null;
    private String ficheroSalida = null;
    private String salidaString = "";
    private int numeroDeAgentes;
    private int numeroDeTareas;
    private int[][] costes;
    private int[] solucionAgentesAsignados = null;
    private float cota;
    private int numeroDeNodos = 0;

    /*
     * Posibles argumentos
     * tareas
     * tareas -h [lo que sea]
     * tareas [-t]
     * tareas [-t] entrada.txt
     * tareas [-t] entrada.txt salida.txt
     */

    public FabricaEntorno(String[] args) {

        // Compruebo si se pasa -h, tanto en el primer como en el segundo argumento tal
        // como permite el enunciado
        if (args.length > 0 && args[0].equals("-h") || args.length > 1 && args[1].equals("-h")) {
            // Manejo si esta pidiendo la ayuda
            muestraAyuda = true;
            return;
        }

        // leerDatos* en los case's
        switch (args.length) {

            case 0:
                // tengo que meter los datos a mano y la salida por pantalla
                break;

            case 1:

                if (args[0].equals("-t")) {
                    // hay que mostrar el trazado
                    muestraTraza = true;
                } else {
                    // el argumento introducido debe ser el fichero de entrada
                    ficheroEntrada = args[0];
                }
                break;

            case 2:
                if (args[0].equals("-t")) {
                    // hay que mostrar el trazado y tambien ha introducido el fichero de entrada
                    muestraTraza = true;
                    ficheroEntrada = args[1];
                } else {
                    // Debe haber introducido tanto la entrada como la salida
                    ficheroEntrada = args[0];
                    ficheroSalida = args[1];
                }
                break;

            case 3:
                if (args[0].equals("-t")) {
                    // Mostrar el trazado y aporta tanto entrada como salida
                    muestraTraza = true;
                    ficheroEntrada = args[1];
                    ficheroSalida = args[2];

                } else {
                    // Si no lleva la opción -t, lleva demasiados argumentos
                    System.out.println("Formato de llamada incorrecto, usa tarea -h para ver las opciones");
                    return;

                }
                break;
            default:
                // Mas de 3 argumentos sin contar -h no son aceptables
                System.out.println("Demasiados argumentos, pruebe 'tarea -h'");
                return;
        }

        // Recabamos los datos necesarios para ejecutar el algoritmo
        try {
            if (ficheroEntrada == null) {
                // No se ha aportado el fichero de entrada
                leeDatosEntradaEstandar();
                return;
            }
            leeDatosArchivo();
        } catch (IOException E) {
            System.err.println(E.toString());
            muestraAyuda = true;
        } catch (DataFormatException dfe) {
            // Lanzada desde leeDatosArchivo()

            System.err.println("Error en el formato de datos, revise el archivo de entrada:\n" + ficheroEntrada);
            System.err.println(dfe.toString());
            ;

            muestraAyuda = true;
        } catch (InputMismatchException ime) {

            System.err.println(ime.toString() + ": Error en la entrada. Introduce solo números naturales!!!");
            muestraAyuda = true;

        } catch (Exception e) {
            System.err.println(e.toString());
            muestraAyuda = true;
        }
    }

    public void leeDatosEntradaEstandar() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduce el numero de agentes en la fábrica:");
        while ((numeroDeAgentes = scanner.nextInt()) < 1) {
            System.out.println("¡¡Fuera de rango!! Introduce al menos un AGENTE!");
        }

        // Conforme al último mensaje en el foro del Equipo Docente,
        // la cantidad de agentes puede ser superior al de tareas pero
        // no al reves.
        System.out.println("Introduce el numero de tareas disponible en la fábrica:");
        while ((numeroDeTareas = scanner.nextInt()) < 1 || numeroDeTareas > numeroDeAgentes) {
            System.out.println("¡¡Fuera de rango!! Introduce al menos una TAREA y no mas TAREAS que AGENTES!");
        }

        costes = new int[numeroDeAgentes][numeroDeTareas];

        for (int i = 0; i < numeroDeAgentes; i++) {
            for (int j = 0; j < numeroDeTareas; j++) {
                int coste = -1;
                System.out.println("Introduce el coste de que el agente " + (i + 1) + " realice la tarea " + (j + 1));

                while ((coste = scanner.nextInt()) < 0) {
                    System.out.println("¡¡Fuera de rango!! Introduce un coste positivo para el agente " + (i + 1)
                            + " y la tarea " + (j + 1));
                }
                costes[i][j] = coste;
            }
        }

        scanner.close();

    }

    public void leeDatosArchivo() throws IOException,
            FileNotFoundException,
            NumberFormatException,
            DataFormatException {

        File entrada = new File(ficheroEntrada);
        FileReader reader = new FileReader(entrada);
        BufferedReader bufferReader = new BufferedReader(reader);

        try {
            String[] dimensiones = bufferReader.readLine().split(" ");
            numeroDeAgentes = Integer.valueOf(dimensiones[0]);
            numeroDeTareas = Integer.valueOf(dimensiones[1]);

            // Conforme al último mensaje en el foro del Equipo Docente,
            // la cantidad de agentes puede ser superior al de tareas pero
            // no al reves.
            if (numeroDeTareas > numeroDeAgentes) {
                bufferReader.close();
                throw new DataFormatException(
                        "Entrada de datos incorrecta!! La cantidad de agentes, debe ser igual o superior al de tareas");
            }
            costes = new int[numeroDeAgentes][numeroDeTareas];

            String[] entradaArrayStrings;
            for (int i = 0; i < numeroDeAgentes; i++) {
                entradaArrayStrings = bufferReader.readLine().split(" ");
                for (int j = 0; j < numeroDeTareas; j++) {
                    costes[i][j] = Integer.valueOf(entradaArrayStrings[j]);
                }
            }

            if (bufferReader.readLine() != null) {
                System.out.println(
                        "Warning!! Posible error en la entrada de datos. Se han leido todos los agentes pero hay más lineas disponibles. Revise si el número de agentes es correcto!");
            }
            bufferReader.close();
        } catch (IndexOutOfBoundsException | NullPointerException npe) {
            throw new DataFormatException(
                    "Entrada malformada! debes meter en la matriz: \n - tantas filas como agentes.\n - tantas columnas como tareas.");
        } catch (NumberFormatException nfe) {
            throw new DataFormatException("Entrada malformada! introduce solo números y elimna las lineas vacias!!!");
        }
    }

    // Esta función es para imprimir los agentes con sus tareas en orden.
    // Se podría imprimir directamente desde el el array que guarda el agente en la
    // posición de la tarea asignada, pero estarían ordenados por las tareas no por
    // los agentes.
    public void solucionACadena() {
        // inicializo el array auxiliar con -1 para detectar si el agente tiene tarea
        // asignada
        int[] salidaArrayAux = new int[numeroDeAgentes];
        for (int i = 0; i < salidaArrayAux.length; i++) {
            salidaArrayAux[i] = -1;
        }
        // Paso de un array que guarda los agentes en la posicion de la tarea i (las
        // tareas guardan el agente que las hace)
        // a un array que guarda las tareas en la posción del agente i (los agentes
        // guardan la tarea que hacen)
        for (int i = 0; i < solucionAgentesAsignados.length; i++) {
            salidaArrayAux[solucionAgentesAsignados[i]] = i;
        }
        // Escribo los agentes que tiene asignada una tarea en un string en el formato
        // que se pide (agente tarea)
        for (int i = 0; i < salidaArrayAux.length; i++) {
            salidaString += salidaArrayAux[i] == -1 ? "" : ((i + 1) + " " + (salidaArrayAux[i] + 1) + "\n");
        }
    }

    public void escribirDatos() {

        solucionACadena();

        if (ficheroSalida != null) {

            try {
                FileWriter fileWriter = new FileWriter(ficheroSalida);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.write(salidaString);
                printWriter.close();
            } catch (IOException ioException) {
                System.out.println("Error leyendo " + ficheroSalida);
            }

        } else {
            System.out.println(salidaString);
        }
    }

    public boolean esMuestraAyuda() {
        return muestraAyuda;
    }

    public boolean esTraza() {
        return muestraTraza;
    }

    // imprime una linea con separaciones del tamaño del ancho de la tabla
    private void imprimeLineaHorizontalTablaCostes() {
        System.out.print("+---------------");
        for (int i = 0; i < costes[0].length; i++) {
            System.out.print("+-------");
        }
        System.out.println("+");
    }

    // imprime una linea continua del tamaño del ancho de la tabla
    private void imprimeLineaHorizontalContinuaTablaCostes() {
        System.out.print("_________________");
        for (int i = 0; i < costes[0].length; i++) {
            System.out.print("________");
        }
        System.out.println();
    }

    // imprime la tabla costes formateada en ASCII
    public void imprimeCostes() {

        // margen superior
        imprimeLineaHorizontalContinuaTablaCostes();

        // linea tareas
        System.out.print("█\t\t| TAREAS");
        for (int i = 1; i < costes[0].length; i++) {
            System.out.print("\t");
        }
        System.out.println("|█");

        // +---------------+-------+-------+-------+-------+-------+-------+
        imprimeLineaHorizontalTablaCostes();

        // █ AGENTES | 1 | 3 | 2 | 3 | 6 | 5 |
        System.out.print("█ AGENTES\t");
        for (int i = 0; i < costes[0].length; i++) {
            System.out.print("| " + (i + 1) + "\t");
        }
        System.out.println("|█");

        // _________________________________________________________________
        imprimeLineaHorizontalContinuaTablaCostes();

        // Datos en si de la tabla costes
        for (int i = 0; i < costes.length; i++) {
            System.out.print("█ " + (i + 1) + "\t\t█");
            for (int j = 0; j < costes[i].length; j++) {
                System.out.print(" " + costes[i][j] + "\t|");
            }
            System.out.println("█");
            imprimeLineaHorizontalTablaCostes();

        }
        imprimeLineaHorizontalContinuaTablaCostes();
        System.out.println();
    }

    /*
     * GETTERS
     */

    public int obtenerNumeroDeAgentes() {
        return numeroDeAgentes;
    }

    public int obtenerNumeroDeTareas() {
        return numeroDeTareas;
    }

    public int[][] obtenerCostes() {
        return costes;
    }

    public int[] obtenerSolucionActual() {
        return solucionAgentesAsignados;
    }

    public float obtenerCosteSolucionActual() {
        return nodoSolucionFinal.obtenerCosteParcial();
    }

    public float obtenerCota() {
        return cota;
    }

    public int obtenerNumeroDeNodos() {
        return numeroDeNodos;
    }
    
    /*
     * SETTERS
     */

    public void ponerSolucionActual(int[] solucion) {
        solucionAgentesAsignados = solucion;
    }

    public void ponerNodoSolucionActual(Nodo nodo) {
        nodoSolucionFinal = nodo;
        ponerSolucionActual(nodo.obtenerAgentesAsignadosATarea());
        ponerCota(nodo.obtenerCosteParcial());

    }

    public void ponerCota(float c) {
        cota = c;
    }

    public void ponerNumeroDeNodos(int n) {
        numeroDeNodos = n;
    }

    // TODO: Descomentar para el análisis
    // private int tamaño_maximo_monticulo=0;
    // public void ponerTamañoMaximoMonticulo(int t) {
    //     if (t>tamaño_maximo_monticulo) {
    //         tamaño_maximo_monticulo = t;
    //     }
    // }
    // public int obtenerTamañoMaximoMonticulo() {
    //     return tamaño_maximo_monticulo;
    // }

}
