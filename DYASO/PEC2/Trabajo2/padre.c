//este archivo es el fichero fuente que al compilarse produce el ejecutable PADRE
#include <fcntl.h>
#include <sys/stat.h>
#include "combate.h"

/*
 * Función: poner_a_0_en_lista(int lista[], int pid, int numero_hijos)
 *      - Systituye en lista el valor pid por un 0
 * @params
 *      - int pid:          Pid del proceso a eliminar de la lista
 *      - int lista[]:      Array de enteros con los pids vivos y cero donde no queda el pids
 *      - int numero_hijos: Cantidad de procesos vivos
 * @return
 *      - int:              0 si lo encuentra, -1 en caso contrario
 */


int poner_a_0_en_lista(int pid, int lista[], int numero_hijos) {
    for(int i = 0; i < numero_hijos; i++) {
        if(lista[i] == 0) {
            numero_hijos++;
        } else if(lista[i] == pid) {
            lista[i] = 0;
            return 0;
        }
    }
    return -1;
}

int main(int argc, char *argv[]) {

    setbuf(stdout, NULL);

    // Otras variables
    //------------------

    int numero_hijos = atoi(argv[1]);
    int procesos_vivos[numero_hijos]; // Para imprimir resultados parciales y sacar el ganador
    char k_bytes[numero_hijos];
    struct mensajes_struct mensaje;
    int tamanio_mensaje = sizeof(mensaje) - sizeof(mensaje.tipo);

    /*
     * El proceso padre P que se obtiene al ejecutar el ejecutable PADRE creará una clave asociada al
     * propio fichero ejecutable (nombre que obtendrá del primer argumento de entrada) y a una letra
     * (por ejemplo ‘X’).
     */

    key_t llave;
    llave = ftok(argv[0], PROYECTO);

    /*
     * A partir de esta clave generará varios mecanismos IPC, en particular creará una cola de
     * mensajes mensajes, una región de memoria compartida lista (que enlazará con una array
     * con capacidad para N PIDs), un semáforo sem que usará para proteger el acceso a dicha
     * variable compartida y una tubería sin nombre barrera.
    */

    // Cola de mensajes

    int mensajes = msgget( llave, IPC_CREAT | 0600 );
    if(mensajes == -1) {
        perror("msgget");
        exit(ERROR_MSG);
    }

    // Memoria compartida

    int tamanio_lista = numero_hijos * sizeof(int);
    int listaid = shmget(llave, tamanio_lista, IPC_CREAT | 0600);
    if(listaid == -1) {                                  // TODO: ver pag 346 del pdf del libro de la asignatura
        perror("shmget_p");
        exit(ERROR_SHM);
    }
    // Enlazando con array con capacidad para N PIDs
    int *lista = shmat(listaid, 0, 0);
    if(lista == (void *)-1) {
        perror("shmat");
        exit(ERROR_SHMAT);
    }

    // Semáforo

    int sem = semget(llave, 1, IPC_CREAT | 0600);
    if(sem == -1) {
        perror("semid");
        exit(ERROR_SEM);
    }
    init_sem(sem, 1);

    // Tubería sin nombre
     // - extremo de lectura    -> barrera[0]
     // - extremo de escritura  -> barrera[1]
    int barrera[2];
    int resultado_pipe = pipe(barrera);
    if(resultado_pipe == -1) {
        perror("pipe");
        exit(ERROR_PIPE);
    }

    /*
     * A continuación creará N procesos hijos (H1…HN), donde N es un parámetro que se pasada como
     * entrada al invocarlo desde Ejecicio2.sh. Cada hijo realizará un exec() del ejecutable HIJO.
     * Cada uno de estos procesos hijo Hi re-establecerá los mecanismos IPC para comunicarse con su
     * padre (es necesario reabrirlos* ya que al hacer exec() el código del padre se sobrescribe con
     * el código del hijo).
     */

    // Aunque el enunciado habla de cerrar la entrada estandar antes de llamar a dup2
    // tengo entendido que dup2 cierra automaticamente el fichero que tiene como descriptor
    // el especificado como destino y por ello no lo hago explicitamente.
    dup2(barrera[0], STDIN_FILENO);
    // cierro en padre el fichero de lectura ya que no lo voy a usar y a los hijos les llega un duplicado
    close(barrera[0]);

    printf("\033[44m\033[37m**************************\033[0m\n");
    printf("\033[44m\033[37m* PROCESOS EN EL COMBATE *\033[0m\n");
    printf("\033[44m\033[37m**************************\033[0m\n");
    for(int i = 0; i < numero_hijos; i++) {    // ver sección 4.3 y 4.8.2

        int hijo = fork();
        if(hijo == -1) {            // error

            perror("fork");
            exit(ERROR_FORK);

        } else if ( hijo == 0){     // hijo

            // char llave_char_array[10];
            // sprintf(llave_char_array, "%d", llave);
            char *datos[] = {"HIJO", argv[0], argv[1], NULL};
            execv("HIJO", datos); // ver execv, execl y execlp

            // si se ejecuta esto ha habido un error con exec
            perror("execv");
            exit(ERROR_EXEC);

        } else {      // padre

            printf("\033[34m - %d\t\t*\033[0m\n", hijo);

            /*
             * El proceso padre, justo antes de iniciar cada ronda mantendrá una lista con los PIDs de los
             * procesos “vivos” en el array lista ubicado en la región de memoria compartida. El acceso a
             * dicha región por el padre y los hijos deberá protegerse mediante un semáforo sem que será
             * usado en cada acceso a la misma.
             */

            k_bytes[i] = i;

            if(wait_sem(sem) == -1) {
                perror("wait_sem");
                exit(ERROR_SEM);
            }

            lista[i] = hijo;

            if(signal_sem(sem) == -1) {
                perror("signal_sem");
                exit(ERROR_SEM);
            }


        }

    }
    printf("\033[44m\033[37m**************************\033[0m\n");

    // PADRE (Los hijos se van por exec)
    // ---------------------------------

    // ESPERANDO PROCESOS

    for(int i = 0; i < numero_hijos; i++) {
        if(msgrcv(mensajes, &mensaje, tamanio_mensaje, MSG_READY, 0) == -1) {
            perror("msgrcv");
            exit(ERROR_MSG);
        }
        printf("\033[32m⚙ Proceso %d ready ✓\033[0m\n", mensaje.pid);
    }

    // CUENTA ATRAS

    for(int i = CUENTA_ATRAS; i > 0; i--) {

        
        printf("\033[1mEmpezando el combate en %d\033[0m\n",i);

        sleep(1);
        printf("\033[A\33[2K\r");
        
    }
    printf("\033[1mEmpezando el combate en 0\033[0m\n");

    do { /* RONDAS */

        /*
         * Para sincronizar el inicio de cada ronda se usará una “barrera” en la que el padre imprimirá
         * “Iniciando ronda de ataques” por salida estándar y avisará a los contendientes para
         * que se preparen enviando K bytes a la tubería barrera (uno por cada hijo que quede vivo).
         */

        printf("\t\t\033[44m\033[37m+----------------------------+\033[0m\n");
        printf("\t\t\033[44m\033[37m| Iniciando ronda de ataques |\033[0m\n");
        printf("\t\t\033[44m\033[37m+----------------------------+\033[0m\n");

        write(barrera[1], k_bytes, numero_hijos);

        /*
         * Por su parte, el padre leerá los K mensajes recibidos y comprobará el resultado de la contienda.
         * A cada proceso que esté “KO” le enviará una señal SIGTERM, esperará a que finalice (wait),
         * pondrá a cero su PID en el array lista y actualizará la variable K que controla el número de
         * procesos vivos.
         */

        int muertos = 0;
        int vivos = 0;
        int procesos_muertos[numero_hijos]; // Para imprimir resultados
        for(int i = 0; i < numero_hijos; i++) {

            int resultado=msgrcv(mensajes, &mensaje, tamanio_mensaje, MSG_RESULTADO, 0);

            if(strcmp(mensaje.estado, "KO") == 0) {
                kill(mensaje.pid, SIGTERM);
                int status;
                waitpid(mensaje.pid, &status, 0);

                if(wait_sem(sem) == -1) {
                    perror("wait_sem");
                    exit(ERROR_SEM);
                }

                poner_a_0_en_lista(mensaje.pid, lista, numero_hijos);

                if(signal_sem(sem) == -1) {
                    perror("signal_sem");
                    exit(ERROR_SEM);
                }

                procesos_muertos[muertos++] = mensaje.pid;

            } else {
                procesos_vivos[vivos++] = mensaje.pid;
            }

        }
        // Podría pasarle directamente el conteo de vivos
        numero_hijos = numero_hijos - muertos;

        // IMPRIMO RESULTADOS PARCIALES (RONDA)
        printf("\n * \033[44m\033[37mResultado de la ronda:\033[0m\n");
        printf("   \033[44m\033[37m----------------------\033[0m\n");
        printf("\033[1;42m\033[37mSobreviven %d procesos ❤️:\033[1;0m\n", vivos);
        for(int i = 0; i < vivos; i++ ) {
            printf(" \033[1;32m- %d\033[1;0m\n", procesos_vivos[i]);
        }
        printf("\033[1;41m\033[30mMueren %d procesos ☠:\033[1;0m\n", muertos);
        for(int i = 0; i < muertos; i++ ) {
            printf(" \033[1;31m- %d\033[1;0m\n", procesos_muertos[i]);
        }


    /*
     * Si después de la ronda quedan dos o más contendientes, el padre iniciará una nueva ronda de
     * ataques, en caso contrario terminarán las rondas.
     */
    } while(numero_hijos > 1);

    printf("\n\033[44m\033[37m*************************************************************\033[0m\n");
    printf("\t\t+-----------------+\n");
    printf("\t\t| FIN DEL COMBATE |\n");
    printf("\t\t+-----------------+\n");

    /*
     * Al terminar las rondas, si queda un solo hijo con vida, el padre lo finaliza con SIGTERM
     * esperando a que termine su ejecución, a continuación escribirá “El hijo Hi ha ganado”
     * en el fichero FIFO resultado y finalmente liberaría todos los recursos IPC.
     */

    /*
     * Si no quedan hijos vivos (se han matado todos mutuamente) el padre escribirá “Empate” en el
     * fichero FIFO resultado y liberará los recursos.
     */

    printf("\nEscribiendo el resultado en la tuberia \"resultado\"...\n");
    printf("RESULTADO: \n\t");


    int fifo = open("resultado", O_WRONLY);
    if(fifo == -1) {
        perror("fifo");
    }
    char mensaje_salida[50];

    if(numero_hijos == 0){ // No quedan hijos vivos

        dprintf(fifo, "\t\t ⛓️ - EMPATE - ⛓️\n\n");

    } else {                // Queda un hijo

        kill(procesos_vivos[0], SIGTERM);
        waitpid(procesos_vivos[0], NULL,0);
        dprintf(fifo, "\t ⛓️ - El hijo %d ha ganado - ⛓️\n\n", procesos_vivos[0]);
    }

    // Liberando recursos
    close(barrera[1]);
    close(fifo);
    msgctl(mensajes, IPC_RMID, 0);
    semctl(sem, IPC_RMID, 0);
    shmctl(listaid, IPC_RMID, 0);

    /*
    * Antes de terminar, el padre demostrará que los recursos IPC se han liberado utilizando la
    * llamada al sistema system para invocar la utilidad ipcs con las opciones apropiadas para
    * mostrar solamente las colas de mensajes y los semáforos (En general puede haber regiones de
    * memoria compartida en uso, es normal y se cierran solas cuando el proceso termina pero no
    * deben quedar colas de mensajes ni semáforos abiertos).
    */

    system("ipcs -q");
    system("ipcs -s");

}
