package es.juanlumg;

import java.util.PriorityQueue;

public class AsignadorTareas {

    private FabricaEntorno fabricaEntorno;

    public AsignadorTareas(FabricaEntorno entorno) {
        fabricaEntorno = entorno;
    }

    public void empezar() {
        // Comprobamos si hay que mostrar la ayuda
        if (fabricaEntorno.esMuestraAyuda()) {
            System.out.println("SINTAXIS: tareas [-t][-h] [fichero entrada]");
            System.out.println("-t\t\t\tTraza el algoritmo");
            System.out.println("-h\t\t\tMuestra esta ayuda");
            System.out.println("[fichero entrada]\tNombre del fichero de entrada");
            System.out.println("[fichero salida]\tNombre del fichero de salida");
            return;
        }

        // TRAZA **************************************************************
        if (fabricaEntorno.esTraza()) {

            System.out.println("Tenemos " + fabricaEntorno.obtenerNumeroDeTareas() + " tareas, y "
                    + fabricaEntorno.obtenerNumeroDeAgentes() + " agentes para hacerlas.");
            System.out.println("Los costes de hacer las tareas son los siguientes:");

            fabricaEntorno.imprimeCostes();
        }
        // ************************************************************** TRAZA

        asignaAgentes(fabricaEntorno.obtenerCostes());
        fabricaEntorno.escribirDatos();
    }

    /*
     * En esta función recae todo el peso del algoritmo
     */
    public void asignaAgentes(int[][] costes) {

        int nodoID = 0; // Utilizado para mostrarlo en la traza;
        float estimacionPesimista;
        PriorityQueue<Nodo> monticulo = new PriorityQueue<>();
        Nodo nodo = new Nodo(fabricaEntorno),
                hijo = new Nodo(fabricaEntorno);

        // TRAZA **************************************************************
        if (fabricaEntorno.esTraza()) {
            System.out.println("Inicia el algoritmo!!");
        }
        // ************************************************************** TRAZA

        nodo.ponerID(nodoID++);
        nodo.ponerPadre(-1);
        nodo.ponerUltimaTareaAsignada(-1); // inicializado a -1 para ir acorde con los indices de un array en java
        nodo.ponerCosteParcial(0);
        nodo.ponerEstimacionOptimista(
                this.estimacionOptimista(costes, nodo.obtenerUltimaTareaAsignada(), nodo.obtenerCosteParcial()));
        monticulo.add(nodo);

        fabricaEntorno
                .ponerCota(estimacionPesimista(costes, nodo.obtenerUltimaTareaAsignada(), nodo.obtenerCosteParcial()));

        // Simplemente añado la información para la traza.
        nodo.ponerInfoExpansion(true, "");

        while (!monticulo.isEmpty() && monticulo.peek().obtenerEstimacionOptimista() <= fabricaEntorno.obtenerCota()) {
            nodo = monticulo.poll(); // Los nodos sacados solo se usan para consultar sus datos y no se vuelven a
                                     // usar
                                     // por eso puedo pasarle a los hijos los punteros del padre, ya que, aunque los
                                     // modifiquemos el padre no los va a volver a usar

            // TRAZA **************************************************************
            if (fabricaEntorno.esTraza()) {
                System.out.println("\n************************");
                System.out.println("* EXPANDIENDO NODO " + nodo.obtenerID() + " *");
                System.out.println("************************\n");
                nodo.traza();

                System.out.println("\n - HIJOS");
                System.out.println("   *****");
            }
            // ************************************************************** TRAZA

            hijo.ponerPadre(nodo.obtenerID());
            hijo.ponerUltimaTareaAsignada(nodo.obtenerUltimaTareaAsignada() + 1);
            hijo.ponerAgentesAsignadosATareas(nodo.obtenerAgentesAsignadosATarea());
            hijo.ponerAgentesOcupados(nodo.obtenerAgentesOcupados());

            for (int i = 0; i < fabricaEntorno.obtenerNumeroDeAgentes(); i++) {
                if (!hijo.estaAgenteOcupado(i)) { // busca un agente ocioso

                    hijo.ponerID(nodoID++);
                    hijo.asignarAgenteATarea(i, hijo.obtenerUltimaTareaAsignada()); // le asigna la tarea en la que
                                                                                    // estamos
                    hijo.ponerCosteParcial(nodo.obtenerCosteParcial() + costes[i][hijo.obtenerUltimaTareaAsignada()]); // calcula
                                                                                                                       // el
                                                                                                                       // acumulado
                    if (hijo.obtenerUltimaTareaAsignada() == (fabricaEntorno.obtenerNumeroDeTareas() - 1)) {
                        // Hemos asignado todas las tareas a algún agente

                        // Al tener a todos los agentes asignados, utilizamos el coste acumulado y no la
                        // estimación optimista,
                        // no obstante, para mostrarla bien en la traza, la actualizo aquí.
                        hijo.ponerEstimacionOptimista(hijo.obtenerCosteParcial());

                        if (fabricaEntorno.obtenerCota() >= hijo.obtenerCosteParcial()) {
                            // Es una solución

                            // TRAZA **************************************************************
                            if (fabricaEntorno.esTraza()) {
                                hijo.ponerInfoExpansion(false, "Nodo hoja, todas las tareas asignadas.");
                                hijo.traza();
                                if (fabricaEntorno.obtenerSolucionActual() == null) { // primera encontrada
                                    System.out.println("Primera solución encontrada, con un coste de: "
                                            + hijo.obtenerCosteParcial());
                                    System.out.println();
                                } else {
                                    System.out.println("Nueva solución encontrada, con un coste de: "
                                            + hijo.obtenerCosteParcial());
                                    System.out.println();
                                }
                            }
                            // ************************************************************** TRAZA

                            // Hago clone por si cambio algo en los punteros sin darme cuenta
                            fabricaEntorno.ponerNodoSolucionActual(hijo.clone());

                            // TRAZA **************************************************************
                        } else if (fabricaEntorno.esTraza()) {
                            hijo.ponerInfoExpansion(false, "Coste total superior a la cota establecida.");
                            hijo.traza();
                        }
                        // ************************************************************** TRAZA

                    } else { // La solución no está completa

                        hijo.ponerEstimacionOptimista(estimacionOptimista(costes, hijo.obtenerUltimaTareaAsignada(),
                                hijo.obtenerCosteParcial()));

                        // TRAZA **************************************************************
                        if (fabricaEntorno.esTraza()) {

                            // Aunque la poda se va a decidir cuando se saque del montículo, como muestro la
                            // traza desde aquí,
                            // compruebo si ya se sabe si se va a podar para mostrarlo
                            if (hijo.obtenerEstimacionOptimista() > fabricaEntorno.obtenerCota()) {
                                hijo.ponerInfoExpansion(false,
                                        "Próxima poda por coste mínimo estimado superior a la cota establecida.");
                            }
                            hijo.traza();
                        }
                        // ************************************************************** TRAZA
                        monticulo.add(hijo.clone());

                        //TODO: Descomentar para el análisis
                        //fabricaEntorno.ponerTamañoMaximoMonticulo(monticulo.size());

                        estimacionPesimista = estimacionPesimista(costes, hijo.obtenerUltimaTareaAsignada(),
                                hijo.obtenerCosteParcial());
                        if (fabricaEntorno.obtenerCota() > estimacionPesimista) {
                            fabricaEntorno.ponerCota(estimacionPesimista);
                        }

                    }
                    // Para volver a usarlo con otros hijos
                    hijo.ponerAgenteOcupado(i, false);
                    hijo.ponerInfoExpansion(true, "");
                }
            }

        }

        // TRAZA **************************************************************
        if (fabricaEntorno.esTraza()) {
            // Si el monticulo no está vacio se ha podado por ser la cota mayor que la
            // estimacion optimista
            if (!monticulo.isEmpty()) {
                System.out.println("\nRAMA PODADA");
                System.out.println("***********");
                Nodo n = monticulo.poll();
                n.ponerInfoExpansion(false, "Poda por coste mínimo estimado mayor que la cota establecida");
                n.traza();
            }

            System.out.println("Coste total: " + fabricaEntorno.obtenerCosteSolucionActual());
        }
        // ************************************************************** TRAZA

        // TODO: Descomentar para el análisis
        // fabricaEntorno.ponerNumeroDeNodos(nodoID);
        // System.out.println("Tamaño máximo del montículo: " + fabricaEntorno.obtenerTamañoMaximoMonticulo());
        
    }

    // Calcula el coste menor a partir una cierta elección inicial,
    // el cálculo no tiene en cuenta si un agente ya está ocupado en otra tarea
    public int estimacionOptimista(int[][] costes, int ultimaTareaAsignada, int costeParcial) {

        int estimacion, menorCoste;
        estimacion = costeParcial;
        // Recorremos las tareas que hay que hacer desde la primera no hecha, hasta la
        // última
        for (int i = ultimaTareaAsignada + 1; i < fabricaEntorno.obtenerNumeroDeTareas(); i++) {
            // metemos el coste de que el primer agente haga la tarea i
            menorCoste = costes[0][i];
            // buscamos el menor coste de la tarea i para almacenarlo
            for (int j = 1; j < fabricaEntorno.obtenerNumeroDeAgentes(); j++) {
                if (menorCoste > costes[j][i]) {
                    menorCoste = costes[j][i];
                }
            }
            // sumamos al coste que llevamos, los menores costes de las tareas que quedan
            estimacion += menorCoste;
        }

        return estimacion;
    }

    // Calcula el coste mayor a partir una cierta elección inicial,
    // el cálculo no tiene en cuenta si un agente ya está ocupado en otra tarea
    public int estimacionPesimista(int[][] costes, int ultimaTareaAsignada, int costeParcial) {
        int estimacion, mayorCoste;
        estimacion = costeParcial;
        // Recorremos las tareas que hay que hacer desde la primera no hecha, hasta la
        // última
        for (int i = ultimaTareaAsignada + 1; i < fabricaEntorno.obtenerNumeroDeTareas(); i++) {
            // metemos el coste de que el primer agente haga la tarea i
            mayorCoste = costes[0][i];
            // buscamos el mayor coste de la tarea i para almacenarlo
            for (int j = 1; j < fabricaEntorno.obtenerNumeroDeAgentes(); j++) {
                if (mayorCoste < costes[j][i]) {
                    mayorCoste = costes[j][i];
                }
            }
            // sumamos al coste que llevamos, los mayores costes de las tareas que quedan
            estimacion += mayorCoste;
        }

        return estimacion;
    }
}
