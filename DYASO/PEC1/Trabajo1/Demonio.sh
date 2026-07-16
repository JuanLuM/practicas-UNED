#!/bin/bash

#Demonio Dummie, tenéis que completarlo para que haga algo

#Bucle mientras que no llegue el apocalipsis
#   -Espera un segundo
#   -Lee las listas y revive los procesos cuando sea necaario dejando entradas en la biblia
#   -Puede usar todos los ficheros temporales que quiera pero luego en el Apocalipsis hay que borrarlos
#   -Hay que usar un lock para no acceder a las listas a la vez que Fausto
#   -Ojo al cerrar los proceos, hay que terminar el arbol completo no sólo uno de ellos
#Fin bucle

while [ ! -f Apocalipsis ]
do
    sleep 1

    i=1
    # Bucle para recorrer la lista PROCESOS
    while read -r pid proceso
    do

        if [ -f "Infierno/$pid" ]; then
        # El proceso esta en el infierno

            # Eliminar los procesos en el árbol
            pids_arbol=$(pstree -p $pid | egrep --only-matching '[0-9]+')
            kill -9 $pids_arbol

            # Eliminar el archivo del Infierno
            rm "Infierno/$pid"

            (
                flock 666

                # Eliminar la entrada de la lista
                sed -i "$((i--))d" procesos

                # Añadir entrada a la Biblia
                echo "$(date +%T) El proceso $pid $proceso ha terminado" >> Biblia.txt

            ) 666> SanPedro

        else
        # El proceso no está en el infierno

            ps -e | grep $pid > /dev/null
            if [ $? -ne 0 ]; then
            # El proceso no está en ejecución

            (
                flock 666
                sed -i "$((i--))d" procesos
                # Ejemplo: 13:57:26: El proceso 10001 ‘sleep 10; echo hola’ ha terminado.
                echo "$(date +%T) El proceso $pid $proceso ha terminado" >> Biblia.txt

            ) 666> SanPedro

            fi

        fi

        ((i++))

    done < procesos
#---------------------------------------------------------------------------------------------------
    i=1
    # Bucle para recorrer la lista procesos_servicio
    while read -r pid proceso
    do
        if [ -f "Infierno/$pid" ]; then
        # El proceso esta en el infierno

            # Eliminar los procesos en el árbol
            pids_arbol=$(pstree -p $pid | egrep --only-matching '[0-9]+')
            kill -9 $pids_arbol

            # Eliminar el archivo del Infierno
            rm "Infierno/$pid"

            (
                flock 666
                # Eliminar la entrada de la lista
                sed -i "$((i--))d" procesos_servicio

                # Añadir entrada a la Biblia
                echo "$(date +%T) El proceso $pid $proceso ha terminado" >> Biblia.txt
            ) 666> SanPedro

        else
        # El proceso no está en el infierno

            ps -e | grep $pid > /dev/null
            if [ $? -ne 0 ]; then
            # El proceso no está en ejecución

                # Elimino las comillas simples para poder usarlo con bash
                proceso_sin_comillas=${proceso//\'/}
                bash -c "$proceso_sin_comillas" &
                pid_nuevo=$!

                # Sustituyo los caracteres / por \/ escapados para su uso en sed
                proceso_escapado=${proceso//\//\\\/}

                (
                    flock 666
                    sed -i "${i}s/.*/$pid_nuevo $proceso_escapado/" procesos_servicio
                    # 13:57:30 El proceso 10002 “yes > /dev/null" resucita con pid 10004
                    echo "$(date +%T) El proceso $pid $proceso resucita con pid $pid_nuevo" >> Biblia.txt
                ) 666> SanPedro

            fi

        fi

        ((i++))

    done < procesos_servicio
#---------------------------------------------------------------------------------------------------
    i=1
    # Bucle para recorrer la lista procesos_periodicos
    while read -r timer time pid proceso
    do
        if [ -f "Infierno/$pid" ]; then
        # El proceso esta en el infierno

            # Eliminar los procesos en el árbol
            pids_arbol=$(pstree -p $pid | egrep --only-matching '[0-9]+') #TODO: probar el funcionamiento de egrep en la MV
            kill -9 $pids_arbol

            # Eliminar el archivo del Infierno
            rm "Infierno/$pid"

            (
                flock 666
                # Eliminar la entrada de la lista
                sed -i "$((i--))d" procesos_periodicos
                # Añadir entrada a la Biblia
                echo "$(date +%T) El proceso $pid $proceso ha terminado" >> Biblia.txt
            ) 666> SanPedro
        else
        # El proceso no está en el infierno

            ((timer++))
            entrada_lista="$timer $time $pid $proceso"
            ps -e | grep $pid > /dev/null
            if [ $? -ne 0 -a $timer -ge $time ]; then
            # El proceso no está en ejecución

                proceso_sin_comillas=${proceso//\'/}
                bash -c "$proceso_sin_comillas" &
                pid_nuevo=$!
                # 13:58:35 El proceso 10003 “echo hola” se ha reencarnado en el pid 10005.
                (flock 666; echo "$(date +%T) El proceso $pid $proceso se ha reencarnado en el pid $pid_nuevo." >> Biblia.txt ) 666> SanPedro
                entrada_lista="0 $time $pid_nuevo $proceso"
            fi

            entrada_lista_escapada=${entrada_lista//\//\\\/}
            (flock 666; sed -i "${i}s/.*/$entrada_lista_escapada/" procesos_periodicos ) 666> SanPedro

        fi

        ((i++))
    done < procesos_periodicos
#---------------------------------------------------------------------------------------------------
done

#Apocalipsis: termino todos los procesos y limpio todo dejando sólo Fausto, el Demonio y la Biblia
# EL APOCALIPSIS HA LLEGADO

    # 11:29:36 ---------------Apocalipsis---------------
    # 11:29:36 El proceso X ha terminado
    (flock 666; echo "$(date +%T) ---------------Apocalipsis---------------" >> Biblia.txt ) 666> SanPedro

    # Bucle para recorrer la lista PROCESOS
    while read -r pid proceso
    do

        # Eliminar los procesos en el árbol
        pids_arbol=$(pstree -p $pid | egrep --only-matching '[0-9]+')
        kill -9 $pids_arbol
        (flock 666; echo "$(date +%T) El proceso $pid ha terminado" >> Biblia.txt ) 666> SanPedro

    done < procesos
    rm procesos

    # Bucle para recorrer la lista procesos_servicio
    while read -r pid proceso
    do
        # Eliminar los procesos en el árbol
        pids_arbol=$(pstree -p $pid | egrep --only-matching '[0-9]+')
        kill -9 $pids_arbol
        (flock 666; echo "$(date +%T) El proceso $pid ha terminado" >> Biblia.txt ) 666> SanPedro

    done < procesos_servicio
    rm procesos_servicio

    # Bucle para recorrer la lista procesos_periodicos
    while read -r timer time pid proceso
    do

        # Eliminar los procesos en el árbol
        pids_arbol=$(pstree -p $pid | egrep --only-matching '[0-9]+')
        kill -9 $pids_arbol
        (flock 666; echo "$(date +%T) El proceso $pid ha terminado" >> Biblia.txt ) 666> SanPedro

    done < procesos_periodicos
    rm procesos_periodicos
    rm -r Infierno/
    rm Apocalipsis
    # 11:29:36 Se acabo el mundo.
    (flock 666; echo "$(date +%T) Se acabó el mundo." >> Biblia.txt ) 666> SanPedro
    rm SanPedro
