//este archivo es el fichero fuente que al compilarse produce el ejecutable HIJO
#include <time.h>
#include "combate.h"

// Variables globales

// En principio estan todos OK hasta que les atacan indefensos
char estado[3] = "OK";

// Funciones

/*
 * FUNCIÓN: defensa()
 *      - Imprime por consola "El hijo %d ha repelido un ataque\n"
 *      - Establece el estado a "OK"
 */
void defensa() {
    /*
     * La función defensa, al ser invocada mostrará un mensaje diciendo “El hijo Hi ha
     * repelido un ataque” (donde Hi es su PID) y establecerá la cadena estado al valor
     * “OK”.
     */
    printf("\033[1;42m\033[37m⚒ El hijo %d ha repelido un ataque 🛡️\033[0m\n", getpid());
    strcpy(estado, "OK");
}

/*
 * FUNCIÓN: indefenso()
 *      - Imprime por consola "El hijo %d ha sidoemboscado mientras realizaba un ataque\n"
 *      - Establece el estado a "KO"
 */
void indefenso() {
    /*
     * La función indefenso, por el contrario, al ser invocada escribirá “El hijo Hi ha sido
     * emboscado mientras realizaba un ataque” y establecerá estado a “KO”.
     */
    printf("\033[1;41m\033[30m⚰ El hijo %d ha sido emboscado mientras realizaba un ataque ☠\033[0m\n", getpid());
    strcpy(estado, "KO");
}

// Función principal, main
// argv
//  - argv[0] = "HIJO"
//  - argv[1] = "PADRE"
//  - argv[2] = "numero_hijos"
int main(int argc, char *argv[]) {

    setbuf(stdout, NULL);

    //  _________________
    // | Otras variables |
    //  -----------------
    //  -----------------

    struct mensajes_struct mensaje;

    int tamanio_mensaje = sizeof(mensaje) - sizeof(mensaje.tipo);

    int numero_hijos = atoi(argv[2]);

    int barrera = dup(STDIN_FILENO); // También podría hacer 'int barrera = STDIN_FILENO;'

    close(STDIN_FILENO);

    // Si necesitasemos recuperar la entrada por consola podríamos descomentar las siguientes lineas
    // int terminal_fd = open("/dev/tty", O_RDONLY);
    // dup2(terminal_fd, STDIN_FILENO);
    // También se podría pasar por la tubería el descriptor a todos los hijos

    /*
     * Cada uno de estos procesos hijo Hi re-establecerá los mecanismos IPC para comunicarse con su
     * padre (es necesario reabrirlos* ya que al hacer exec() el código del padre se sobrescribe con
     * el código del hijo)
     */

    // llave

    key_t llave;
    llave = ftok(argv[1], PROYECTO);

    // Cola de mensajes

    int mensajes = msgget( llave, IPC_CREAT | 0600 );
    if(mensajes == -1) {
        perror("msget");
        exit(ERROR_MSG);
    }

    // Memoria compartida

    int listaid = shmget(llave, numero_hijos * sizeof(int), IPC_CREAT | 0600);
    if(listaid == -1) {                                 // TODO: ver pag 346 del pdf del libro de la asignatura
        perror("shmget_h");
        exit(ERROR_SHM);
    }

    // Enlazando con array con capacidad para N PIDs
    // resultado = shmat(shmid, shmdir, shmflags); -1 si da error
    int *lista = shmat(listaid, 0, 0);

    // Semáforo

    int sem = semget(llave, 1, IPC_CREAT | 0600);
    if(sem == -1) {
        perror("semid");
        exit(2);
    }

    srand(getpid()); //srand(time(NULL)); -> todos se ejecutan en el mismo segundo y devuelven el mismo aleatorio


    // Avisamos al padre de que estamos listos!!
    mensaje.tipo = MSG_READY;
    mensaje.pid = getpid();
    if(msgsnd(mensajes, &mensaje, tamanio_mensaje, 0)) {
        perror("msgsnd");
        exit(ERROR_MSG);
    }

    while(TRUE) {

        /*
         * Los hijos esperarán la barrera leyendo de la tubería barrera.
         */
        char byte;
        read(barrera, &byte, sizeof(byte));

        /*
         * A continuación, los distintos hijos se atacaran por rondas, en cada ronda hay
         * dos fases “preparación” y “ataque”.
         */
        /*
         * En la fase de preparación, cada proceso hijo decidirá aleatoriamente si “ataca” o “defiende” e
         * iniciará la variable estado que controla el resultado de la contienda a la cadena “OK”.
         */
        int accion = rand() % 2;
        strcpy(estado, "OK");

        if( accion == DEFENSA ) {   // rand() % 2 == DEFENSA        // defiende
        /*
         * Los procesos que se defiendan instalarán la función defensa en SIGURS1 y dormirán
         * durante 0.2 segundos**,
         */
            signal(SIGUSR1, defensa);
            printf("\033[1;32mEl proceso %d se protege\033[1;0m\n", getpid());
            usleep(200000);

        } else {                    // rand() % 2 == ATAQUE         // ataca

        /*
         * mientras que los atacantes instalarán la función indefenso y
         * esperarán 0.1 segundos antes de iniciar su ataque.
         */

            signal(SIGUSR1, indefenso);
            usleep(100000);

            /*
             * Transcurridos los 0.1 segundos de la fase de preparación, los procesos que estén en modo de
             * ataque elegirán aleatoriamente un PID válido de la lista, esto es, uno que sea distinto de cero (y
             * del suyo propio por razones obvias). Seguidamente imprimirán por salida estándar “Atacando
             * al proceso Hi”, enviarán a dicho proceso una señal SIGURS1 y esperarán otros 0.1
             * segundos a que termine la fase de ataques.
             */
            // Buscando un proceso aleatorio para atacarle
            int procesos_vivos_menos_yo[numero_hijos];
            int k_hijos_vivos_menos_yo = 0;
            for(int i = 0; i < numero_hijos; i++) { // numero_procesos es el numero de hijos

                procesos_vivos_menos_yo[i] = 0; // Aunque no es necesario me ayuda a depurar

                if(wait_sem(sem) == -1) {
                    perror("wait_sem");
                    exit(ERROR_SEM);
                }

                if(lista[i] > 0 && lista[i] != getpid() ) { // Si está vivo y no soy yo
                    procesos_vivos_menos_yo[k_hijos_vivos_menos_yo++] = lista[i];
                }

                if(signal_sem(sem) == -1) {
                    perror("signal_sem");
                    exit(ERROR_SEM);
                }

            }
            int proceso_objetivo = procesos_vivos_menos_yo[rand() % k_hijos_vivos_menos_yo];

            printf("\033[1;31mProceso %d atacando al proceso %d 💣\033[1;0m\n", getpid(), proceso_objetivo);
            kill(proceso_objetivo, SIGUSR1);
            usleep(100000);

        }

        /*
         * Terminada la ronda de ataques y defensas cada proceso hijo enviará un mensaje al padre usando
         * la cola de mensajes llamada mensajes, indicando su PID y el resultado de la contienda
         * (cadena estado) y quedara a la espera de una siguiente ronda (esperando en la barrera).
         */

        // Enviando resultado
        mensaje.tipo = MSG_RESULTADO;
        mensaje.pid = getpid();
        strcpy(mensaje.estado, estado);
        int r_msgsnd = msgsnd(mensajes, &mensaje, tamanio_mensaje, 0);
        if(r_msgsnd == -1) {
            perror("msgsnd");
            exit(ERROR_MSG);
        }
        // ------------------------
    }

}
