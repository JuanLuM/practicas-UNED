#!/bin/bash

#Recibe órdenes creando los procesos y listas adecuadas
#Si el Demonio no está vivo lo crea
#Al leer/escribir en las listas hay que usar bloqueo para no coincidir con el Demonio


# $? devuelve 1 si no lo encuentra y 0 si lo encuentra
pgrep [D]emonio > /dev/null
if [[ $? -ne 0 ]]; then
# No hay demonio en ejecución

    # Comprobamos la existencia de los archivos y los eliminamos para volverlos a crear.
    # Archivos que debo eliminar
    # procesos, procesos_servicio, procesos_periodicos, Biblia.txt, Apocalipsis y SanPedro. Y la carpeta Infierno

    if [ -f procesos ]; then
        rm procesos
    fi
    if [ -f procesos_servicio ]; then
        rm procesos_servicio
    fi
    if [ -f procesos_periodicos ]; then
        rm procesos_periodicos
    fi
    if [ -f Biblia.txt ]; then
        rm Biblia.txt
    fi
    if [ -f Apocalipsis ]; then
        rm Apocalipsis
    fi
    if [ -f SanPedro ]; then
        rm SanPedro
    fi
    if [ -d Infierno ]; then
        rm -r Infierno
    fi

    # Volvemos a crear la estructura
    touch procesos procesos_servicio procesos_periodicos Biblia.txt SanPedro
    mkdir Infierno

    # Lanzamos el proceso demonio.
    nohup ./Demonio.sh > /dev/null &

    # "11:29:24 ---------------Génesis---------------"
    # "11:29:24 El demonio ha sido creado."
     (
        flock 666
        echo -e "$(date +%T) ---------------Génesis---------------" >> Biblia.txt
        echo -e "$(date +%T) El demonio ha sido creado." >> Biblia.txt
     ) 666> SanPedro

fi

# Procesar los argumentos
# ./Fausto.sh run comando
# ./Fausto.sh run-service comando
# ./Fausto.sh run-periodic T comando
# ./Fausto.sh list
# ./Fausto.sh help
# ./Fausto.sh stop PID
# ./Fausto.sh end

case $1 in
    run)
        bash -c "$2" &
        PID_bash_padre=$!
        (
        flock 666
        echo "$PID_bash_padre '$2'" >> procesos
        echo "$(date +%T) El proceso $PID_bash_padre '$2' ha nacido." >> Biblia.txt
        ) 666> SanPedro
    ;;
    run-service)
        #echo "run-service"
        bash -c "$2" &
        PID_bash_padre=$!
        (
        flock 666
        echo "$PID_bash_padre '$2'" >> procesos_servicio
        echo "$(date +%T) El proceso $PID_bash_padre '$2' ha nacido." >> Biblia.txt
        ) 666> SanPedro
    ;;
    run-periodic)
        #echo "run-periodic"
        bash -c "$3" &
        PID_bash_padre=$!
        (
        flock 666
        echo "0 $2 $PID_bash_padre '$3'" >> procesos_periodicos
        echo "$(date +%T) El proceso $PID_bash_padre '$3' ha nacido." >> Biblia.txt
        ) 666> SanPedro
    ;;
    list)
        (
            flock 666;
            echo "***** Procesos normales *****"
            cat procesos
            echo "***** Procesos servicio *****"
            cat procesos_servicio
            echo "***** Procesos periódicos *****"
            cat procesos_periodicos
        ) 666> SanPedro
    ;;
   help)
        echo ".--------------."
        echo "| Ayuda Fausto |"
        echo ".--------------."
        echo "Uso:"
        echo "1) ./Fausto.sh run 'comando':"
        echo -e "\tSe encarga de ejecutar un comando una sola vez y crear una entrada en la lista de procesos con el PID del proceso bash que ejecuta el comando*"
        echo -e "\tCada vez que se cree un proceso se apuntará el nacimiento en la Biblia.txt"
        echo -e "\tEjemplo:"
        echo -e "\t$ ./Fausto.sh run 'sleep 10; echo hola'\n"

        echo "2) ./Fausto.sh run-service 'comando'"
        echo -e "\tEjecuta un comando como servicio"
        echo -e "\tCrea una entrada en la lista de procesos_servicio con el PID del proceso bash que ejecuta el comando*"
        echo -e "\tCada vez que se cree un proceso se apuntará el nacimiento en la Biblia.txt"
        echo -e "\tEjemplo:"
        echo -e "\t$ ./Fausto.sh run-service 'yes > /dev/null'\n"

        echo "3) ./Fausto.sh run-periodic T 'comando'"
        echo -e "\tEjecuta una orden con reinicio periódico."
        echo -e "\tAñade el proceso creado al fichero procesos_periodicos"
        echo -e "\tCada vez que se cree un proceso se apuntará el nacimiento en la Biblia.txt"
        echo -e "\tEjemplo:"
        echo -e "\t$ ./Fausto.sh run-periodic 10 'echo hola'\n"

        echo "4) ./Fausto.sh list"
        echo -e "\tMuestra por salida estándar el contenido de las listas:"
        echo -e "\tprocesos, procesos_servicio y procesos_periodicos.\n"

        echo "5) ./Fausto.sh help"
        echo -e "\tMuestra la lista de comandos disponibles y su sintaxis.\n"

        echo "6) ./Fausto.sh stop PID"
        echo -e "\tSi el proceso con pid=PID fue creado por Fausto o Demonio:"
        echo -e "\t\tAñade el proceso para ser destruido."
        echo -e "\tSi no:"
        echo -e "\t\tMuestra un error"
        echo -e "\tEjemplo:"
        echo -e "\t./Fausto.sh stop 10003\n"

        echo "7) ./Fausto.sh end"
        echo -e "\tInicia el Apocalipsis"
        echo -e "\tTermina la ejecución de todos los procesos que hayan sido lanzados, incluido el Demonio\n"
   ;;
    stop)
        cat procesos procesos_servicio procesos_periodicos | grep "$2" > /dev/null
        if [ $? -eq 0 ]; then
            touch "./Infierno/$2"
        else
            echo "Proceso con pid=$2 no encontrado, pruebe hacer './Fausto list'"
        fi

    ;;
    end)
        touch Apocalipsis
    ;;
    *)
        echo "Error, orden '$1' no reconocida, consulte las órdenes disponibles con './Fausto.sh help'."
    ;;
esac


