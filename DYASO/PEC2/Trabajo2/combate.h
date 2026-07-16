#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <sys/wait.h>
#include <unistd.h>
#include <signal.h>
#include <string.h>
#include <errno.h>

#define PROYECTO        'X'
#define TRUE            1
#define ATAQUE          0
#define DEFENSA         1
#define ERROR_FORK      1
#define ERROR_SIGNAL    2
#define ERROR_MSG       3
#define ERROR_SHM       4
#define ERROR_SEM       5
#define ERROR_PIPE      6
#define ERROR_EXEC      7
#define ERROR_SHMAT     8
#define MSG_RESULTADO   1
#define MSG_READY       2
#define CUENTA_ATRAS    3

typedef unsigned short ushort;

/*
 * Estructura de los mensajes enviados y recibidos entre padre e hijos
 */
struct mensajes_struct {
    long tipo;
    int pid;
    char estado[3];
};

/*
 * FUNCIÓN: init_sem(int semid, ushort valor)
 *
 * @params
 *      - int semid:    id del semaforo
 *      - ushort valor  valor a inicializar el semaforo
 * @return
 *      - 0 si todo va bien, -1 en caso contrario
 *
 */
int init_sem (int semid, ushort valor) {

    ushort sem_array[1];
    sem_array[0]=valor;
    if (semctl(semid,0,SETALL,sem_array)==-1) {     // semctl(semid, semnum, cmd, arg);
        perror("Error semctl");
        return -1;
    }
    return 0;
}

/*
 * FUNCIÓN: wait_sem(int semid)
 *      - Pide acceso al recurso que protege el semaforo con id = semid
 * @params
 *      - int semid:    id del semaforo
 * @return
 *      - 0 si todo va bien, -1 en caso contrario
 *
 */
int wait_sem (int semid) {

    struct sembuf op[1];
    op[0].sem_num=0;
    op[0].sem_op=-1;
    op[0].sem_flg=0;

    while (semop(semid, op, 1) == -1) {
        if(errno != EINTR) {
            perror("semop");
            return -1;
        }
    }
    return 0;
}

/*
 * FUNCIÓN: signal_sem(int semid)
 *      - Deja libre un recurso de los que protege el semaforo con id = semid
 * @params
 *      - int semid:    id del semaforo
 * @return
 *      - 0 si todo va bien, -1 en caso contrario
 */
int signal_sem (int semid) {
    struct sembuf op[1];
    op[0].sem_num=0;
    op[0].sem_op=1;
    op[0].sem_flg=0;
    if (semop(semid, op, 1) == -1) {
        perror("semop:");
        return -1;
    }
    return 0;
}
