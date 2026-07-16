package es.juanlumg.hanoi;


public class Main {
    
    public static void main(String[] args) {

        // Inicia el escenario con los argumentos
        HanoiAmbiente ambiente = new HanoiAmbiente(args);

        // Crea un conductor a partir del ambiente creado e inicia el algoritmo.
        HanoiConductor conductor = new HanoiConductor(ambiente);
        // long startTime = System.currentTimeMillis();
        conductor.empezar();
        // long endTime = System.currentTimeMillis();
        // System.out.println(endTime - startTime);

    }
}
