package es.juanlumg.hanoi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class HanoiAmbiente {

private boolean muestraAyuda = false;
private boolean muestraTraza = false;
private String ficheroEntrada = null;
private String ficheroSalida = null;
private String salida = "";
private int poste_origen;
private int poste_destino;
private int poste_auxiliar;
private int discos;

    /*
     * Posibles argumentos
     * hanoi
     * hanoi -h [lo que sea]
     * hanoi [-t] 
     * hanoi [-t] entrada.txt
     * hanoi [-t] entrada.txt salida.txt
     */

    public HanoiAmbiente(String[] args) {

        if(args.length > 0 && args[0].equals("-h") || args.length > 1 && args[1].equals("-h")) {
        // Manejo si esta pidiendo la ayuda
            muestraAyuda = true; 
            return;
        }

        switch (args.length) {
            
            case 0: 
            // tengo que meter los datos a mano y la salida por pantalla
                break;
                
            case 1:
            
                if(args[0].equals("-t")) {
                // hay que mostrar el trazado
                    muestraTraza = true;
                } else {
                // el argumento introducido debe ser el fichero de entrada
                    ficheroEntrada = args[0];
                }
                break;

            case 2:
                if(args[0].equals("-t")){
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
                if(args[0].equals("-t")){
                // Mostrar el trazado y aporta tanto entrada como salida
                    muestraTraza = true;
                    ficheroEntrada = args[1];
                    ficheroSalida = args[2];
               
                } else {
                // Si no lleva la opción -t, lleva demasiados argumentos
                    System.out.println("Formato de llamada incorrecto, usa hanoi -h para ver las opciones");
                    return;

                }
                break;
            default:
            // Mas de 3 argumentos sin contar -h no son aceptables
                    System.out.println("Demasiados argumentos, pruebe 'hanoi -h'");
                    return;
        }

        // Recabamos los datos necesarios para ejecutar el algoritmo
        try {
            if(ficheroEntrada == null) {
                // No se ha aportado el fichero de entrada
                leeDatosEntradaEstandar();
                return;
            } 
            leeDatosArchivo();
        } catch (IOException E) {

            System.out.println(E.toString());

        } catch (PosteFueraDeRangoException pfre) {
            
            System.err.println(pfre.toString());
            System.out.println("El valor " + pfre.getPoste() + " para el poste " + pfre.getTipo() + " está fuera de rango." );

            muestraAyuda = true;
        } catch (DatosEntradaErroneosException dee) {
            System.err.toString();
            System.out.println("Error en el formato de datos, revise el archivo de entrada:\n" + ficheroEntrada );

            muestraAyuda = true;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void leeDatosEntradaEstandar() {

        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Introduce el poste de origen (1,2 o 3):");
        while ((poste_origen = scanner.nextInt()) < 1 || poste_origen > 3) {
            System.out.println("¡¡Fuera de rango!! Introduce un número de 1 a 3 para el ORIGEN:");
        }


        System.out.println("Introduce el poste de destino (1,2 o 3):");
        while ((poste_destino = scanner.nextInt()) < 1 || poste_destino > 3 ) {
            System.out.println("¡¡Fuera de rango!! Introduce un número de 1 a 3 para el DESTINO:");
        } 

        // 01 xor 10 = 11; 01 xor 11 = 10; 10 xor 11 = 01
        poste_auxiliar = poste_origen ^ poste_destino;

        System.out.println("Introduce el número de discos: ");
        while ((discos = scanner.nextInt()) < 0 ) {
            System.out.println("¡¡Negativo!! Introduce un número de discos positivo: ");
        } 
        scanner.close();


    }
    
    public void leeDatosArchivo() throws 
                                    IOException, 
                                    FileNotFoundException, 
                                    NumberFormatException, 
                                    PosteFueraDeRangoException, 
                                    DatosEntradaErroneosException {
    
        File entrada = new File (ficheroEntrada);
        FileReader reader = new FileReader(entrada);
        BufferedReader bufferReader = new BufferedReader(reader);
        String entradaString = bufferReader.readLine();
        bufferReader.close();
        
        String[] entradas;
        if(entradaString != null)
            entradas = entradaString.split(" ");
        else {
            return;
        }

        if(entradas.length != 3){

            throw new DatosEntradaErroneosException();

        }
        
        poste_origen = Integer.valueOf(entradas[0]);
        if (poste_origen < 1 || poste_origen > 3) {
            
            throw new PosteFueraDeRangoException(poste_origen, PosteFueraDeRangoException.TIPO_ORIGEN);
        }
        poste_destino = Integer.valueOf(entradas[1]);
        if (poste_destino < 1 || poste_destino > 3) {
            
            throw new PosteFueraDeRangoException(poste_destino, PosteFueraDeRangoException.TIPO_DESTINO);
        }
        // 01 xor 10 = 11; 01 xor 11 = 10; 10 xor 11 = 01
        poste_auxiliar = poste_origen ^ poste_destino;

        discos = Integer.valueOf(entradas[2]);
        if(discos < 0) {
            
            throw new DatosEntradaErroneosException();

        }

        
    }

    public void añadirMovientoASalida(String movimiento) {
        salida+=movimiento;
    }

    public void escribirDatos() {

        if(ficheroSalida != null) {

            try {
                FileWriter fileWriter = new FileWriter(ficheroSalida);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.write(salida);
                printWriter.close();
            } catch (IOException ioException) {
                System.out.println("Error leyendo " + ficheroSalida);
            }

        } else {

            System.out.println(salida);

        }
    }

    // Devuelve si hay que mostrar la ayuda
    public boolean muestraAyuda() {
        return muestraAyuda;
    }
    
    // Devuelve si hay que mostrar la traza
    public boolean muestraTraza() {
        return muestraTraza;
    }

    public int getPosteOrigen() {
        return poste_origen;
    }
   
    public int getPosteDestino() {
        return poste_destino;
    }
    
    public int getPosteAuxiliar() {
        return poste_auxiliar;
    }
    
    public int getDiscos() {
        return discos;
    }

    // Excepción que lanzo cuando se leen algun dato incorrecto
    public class DatosEntradaErroneosException extends Exception {

    }
   
    // Lanzada cuando se lee un poste menor de 1 o mayor de 3, extiende a DatosEntradaErroneosException
    public class PosteFueraDeRangoException extends DatosEntradaErroneosException {

        public static final int TIPO_ORIGEN = 0, TIPO_DESTINO = 1, TIPO_AUXILIAR = 3;

        private int nPoste;
        private int nTipo;
        public PosteFueraDeRangoException(int poste, int tipo) {

            nPoste = poste;
            nTipo = tipo;

        }

        @Override
        public String toString() {
            return "Se ha producido un error debido a la lectura de un poste fuera de rango.\nValores aceptables 1,2 y 3.";
        }

        public int getPoste() {
            return nPoste;
        }

        public String getTipo() {

            switch (nTipo) {
                case TIPO_ORIGEN:
                    return "ORIGEN";
                case TIPO_DESTINO:
                    return "DESTINO";
                case TIPO_AUXILIAR:
                    return "AUXILIAR";
                default:
                    return "TIPO DESCONOCIDO";
            }

        }

    }
}

