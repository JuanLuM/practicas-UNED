package es.juanlumg;

public class Nodo implements Comparable<Nodo>, Cloneable {

    // Solo usado para imprimir la traza
    private FabricaEntorno fabricaEntorno;
    private int ID;
    private int nodoPadre;
    private boolean expandir = true;
    private String motivoPodaString = "";

    // Array que almacena en la posición i,
    // el número de agente asignado a la tarea i
    private int[] agentesAsignadosATarea;
    // Array que almacena en la posición i,
    // si el agente i está ocupado (true) o no (false)
    private boolean[] agentesOcupados;
    // ultimaTareaAsignada representa la ultima tarea que ya ha sido asignada cuando
    // estas en este nodo
    // Empieza en 0 como los indices de arrays, -1 representa que ninguna tarea ha
    // sido asignada
    private int ultimaTareaAsignada;
    private int costeParcial;
    private int estimacionOptimista;

    /*****************
     * CONSTRUCTORES *
     *****************/

    private Nodo() {
    }

    public Nodo(FabricaEntorno fabricaEntorno) {
        this(fabricaEntorno.obtenerNumeroDeAgentes(), fabricaEntorno.obtenerNumeroDeTareas());
        this.fabricaEntorno = fabricaEntorno;
    }

    private Nodo(int nAgentes, int nTareas) {
        agentesAsignadosATarea = new int[nTareas];
        agentesOcupados = new boolean[nAgentes];
        for (int i = 0; i < nAgentes; i++)
            agentesOcupados[i] = false;
    }

    /***********
     * MÉTODOS *
     ***********/

    public boolean estaAgenteOcupado(int agente) {
        return agentesOcupados[agente];
    }

    public void asignarAgenteATarea(int agente, int tarea) {
        agentesAsignadosATarea[tarea] = agente;
        this.ponerAgenteOcupado(agente, true);
    }

    // Nodo 92
    // ---------
    // - Profundidad: 3
    // - Tarea --> Agente: [1-->1, 2-->3, 3-->5]
    // - Coste minimo estimado: 31
    // - Nodo padre: 66
    // - Expansión: no
    // - Motivo de la poda: Coste mínimo estimado superior a la cota establecida.
    public void traza() {

        System.out.println("Nodo " + ID);
        System.out.println("---------");
        if (nodoPadre != -1)
            System.out.println(" - Nodo padre: " + nodoPadre);
        System.out.println(" - Profundidad: " + (ultimaTareaAsignada + 1));
        System.out.print(" - Tareas Asignadas (Tarea --> Agente): [");
        for (int i = 0; i < ultimaTareaAsignada; i++) {
            System.out.print((i + 1) + "-->" + (agentesAsignadosATarea[i] + 1) + ", ");
        }
        if (ultimaTareaAsignada >= 0) // Ver si arreglar esto para el nodo 0
            System.out.print((ultimaTareaAsignada + 1) + "-->" + (agentesAsignadosATarea[ultimaTareaAsignada] + 1));
        System.out.println("]");
        System.out.println(" - Coste mínimo estimado: " + estimacionOptimista);
        System.out.println(" - Cota actual: " + fabricaEntorno.obtenerCota());

        System.out.println(" - Expansión: " + (expandir ? "si" : "no"));
        if (!expandir) {
            System.out.println(" └─ Motivo: " + motivoPodaString);
        }
        System.out.println();
    }

    /***********
     * GETTERS *
     ***********/

    public int obtenerID() {
        return ID;
    }

    public int[] obtenerAgentesAsignadosATarea() {
        return agentesAsignadosATarea;
    }

    public boolean[] obtenerAgentesOcupados() {
        return agentesOcupados;
    }

    public int obtenerUltimaTareaAsignada() {
        return ultimaTareaAsignada;
    }

    public int obtenerCosteParcial() {
        return costeParcial;
    }

    public int obtenerEstimacionOptimista() {
        return estimacionOptimista;
    }

    /***********
     * SETTERS *
     ***********/

    public void ponerID(int id) {
        ID = id;
    }

    public void ponerPadre(int idPadre) {
        nodoPadre = idPadre;
    }

    public void ponerInfoExpansion(boolean expandir, String motivo) {
        this.expandir = expandir;
        motivoPodaString = motivo;
    }

    public void ponerAgentesAsignadosATareas(int[] agentesAsignadosATarea) {
        this.agentesAsignadosATarea = agentesAsignadosATarea;
    }

    public void ponerAgentesOcupados(boolean[] agentesOcupados) {
        this.agentesOcupados = agentesOcupados;
    }

    public void ponerAgenteOcupado(int agente, boolean ocupado) {
        agentesOcupados[agente] = ocupado;
    }

    public void ponerUltimaTareaAsignada(int ultimaTareaAsignada) {
        this.ultimaTareaAsignada = ultimaTareaAsignada;
    }

    public void ponerCosteParcial(int costeParcial) {
        this.costeParcial = costeParcial;
    }

    public void ponerEstimacionOptimista(int estimacionOptimista) {
        this.estimacionOptimista = estimacionOptimista;
    }

    /*************************
     * MÉTODOS DE INTERFACES *
     *************************/

    // Comparable
    // ------------

    @Override
    public int compareTo(Nodo nodo) {
        // los que se detectan menores, se colocan primero (Orden ascendente)
        return this.estimacionOptimista - nodo.estimacionOptimista;
    }

    // Cloneable
    // ----------

    @Override
    public Nodo clone() {

        Nodo retorno = new Nodo();
        retorno.fabricaEntorno = this.fabricaEntorno;
        retorno.agentesAsignadosATarea = new int[this.agentesAsignadosATarea.length];
        for (int i = 0; i < agentesAsignadosATarea.length; i++) {
            retorno.agentesAsignadosATarea[i] = this.agentesAsignadosATarea[i];
        }
        retorno.agentesOcupados = new boolean[this.agentesOcupados.length];
        for (int i = 0; i < agentesOcupados.length; i++) {
            retorno.agentesOcupados[i] = this.agentesOcupados[i];
        }
        retorno.ID = this.ID;
        retorno.nodoPadre = this.nodoPadre;
        retorno.ultimaTareaAsignada = this.ultimaTareaAsignada;
        retorno.costeParcial = this.costeParcial;
        retorno.estimacionOptimista = this.estimacionOptimista;
        retorno.expandir = this.expandir;
        retorno.motivoPodaString = new String(this.motivoPodaString);

        return retorno;
    }

}
