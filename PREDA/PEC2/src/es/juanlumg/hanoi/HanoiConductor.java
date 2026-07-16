package es.juanlumg.hanoi;

public class HanoiConductor {

    private HanoiAmbiente hanoiAmbiente;

    public HanoiConductor(HanoiAmbiente ambiente) {
        hanoiAmbiente = ambiente;
    }

    // Inicia el algoritmo o muestra la ayuda en su caso
    public void empezar() {

        if(hanoiAmbiente.muestraAyuda()) {
            System.out.println("SINTAXIS: hanoi [-t][-h] [fichero entrada]");
            System.out.println("-t\t\t\tTraza el algoritmo");
            System.out.println("-h\t\t\tMuestra esta ayuda");
            System.out.println("[fichero entrada]\tNombre del fichero de entrada");
            System.out.println("[fichero salida]\tNombre del fichero de salida");
            return;
        }

        if(hanoiAmbiente.muestraTraza()) {
            System.out.println("Poste de origen:\t\t" + hanoiAmbiente.getPosteOrigen());
            System.out.println("Poste de destino:\t\t" + hanoiAmbiente.getPosteDestino());
            System.out.println("Número de discos:\t\t" + hanoiAmbiente.getDiscos());
        }

        resolverHanoi(hanoiAmbiente.getDiscos(), hanoiAmbiente.getPosteOrigen(), hanoiAmbiente.getPosteDestino(),hanoiAmbiente.getPosteAuxiliar());
        hanoiAmbiente.escribirDatos();   
    }
    
    public void resolverHanoi(int discos, int poste_origen, int poste_destino, int poste_auxiliar) {

        // Si se introduce el mismo poste de origen y destino no hay que realizar ningun movimiento
        if(poste_origen == poste_destino) { 
            if(hanoiAmbiente.muestraTraza())
                System.out.println("¡NADA QUE MOVER!: Poste de destino igual al de origen.");
            return;
        }

        // Si no introducimos discos, no hay nada que mover
        if( discos == 0 ) {
            if(hanoiAmbiente.muestraTraza())
                System.out.println("¡NADA QUE MOVER!: Cero discos en el ambiente");
            return;
        }

        // Caso base, solución trivial
        if ( discos == 1 ) {
            // Movemos el único disco que se pide del origen al destino
            if(hanoiAmbiente.muestraTraza())
                System.out.println("Moviendo disco de " + poste_origen + " a " + poste_destino);

            hanoiAmbiente.añadirMovientoASalida(poste_origen + " " + poste_destino + "\n");

        } else {
            // Movemos todos los discos menos 1 al poste auxiliar para acceder al de más abajo
            resolverHanoi(discos - 1, poste_origen, poste_auxiliar, poste_destino);
            
            // Movemos el disco mayor al destino
            if(hanoiAmbiente.muestraTraza())
                System.out.println("Moviendo disco de " + poste_origen + " a " + poste_destino);
            hanoiAmbiente.añadirMovientoASalida(poste_origen + " " + poste_destino + "\n");

            // Movemos los discos que teniamos en el poste auxiliar al poste destino, usando de auxiliar el poste origen
            resolverHanoi(discos - 1, poste_auxiliar, poste_destino, poste_origen);
        }

    }
    
}
