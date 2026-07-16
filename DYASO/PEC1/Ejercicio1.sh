#!/bin/bash
#este archivo hace varias pruebas de funciomaniento de Fausto/Demonio para ello lanza varios procesos con Fausto y comprueba que su ejecución es adecuada

cd Trabajo1

pkill yes
pkill Fausto
pkill Demonio #para eliminar procesos residuales de ejecuciones anteriores

#Ejecuta el programa del estudiante con varias pruebas de ejemplo

./Fausto.sh run 'sleep 10; echo hola >> test1.txt' 

#comprobamos que efectivamente se ha lanzado el proceso demonio y que ha sido adoptado por systemd
echo
echo "************************************************************"
echo "1) Debería de haberse creado el proceso Demonio" 
echo "la salida esperada es systemd───systemd───Demonio.sh───sleep"
echo "************************************************************"
echo 
# si no sale nada o el arbol entero de procesos no se ha creado bien
pstree -s $(ps l | grep [D]emonio | cut -d " " -f5)

#Ejecutamos algunos comandos más
./Fausto.sh run-service 'yes > /dev/null' 

./Fausto.sh run-periodic 5 'echo hola_periodico >> test2.txt'

./Fausto.sh run-periodic 5 'echo hola_periodico_lento >> test3.txt; sleep 20'


#veo que los procesos se han creado bien
echo
echo "************************************************************"
echo "2) Lanzo algunos comandos y compruebo que se han creado"
echo "Debería de haber un proceso normal" 
echo "'sleep 10; echo hola > test1.txt'"
echo "Un proceso servicio 'yes > /dev/null'"
echo "y dos periódicos, el normal y el lento"
echo "Comparamos los procesos teóricamente lanzados y los que"
echo "realmente existen"
echo "************************************************************"
echo

echo "Procesos lanzados según Fausto:"
echo "./Fausto.sh list"
./Fausto.sh list
echo
echo "Procesos existentes:"
echo "ps -l"
ps -l

echo
echo "************************************************************"
echo "3) Elimino manualmente el proceso yes sin avisar a Fausto."
echo "El Demonio debería detectarlo y reiniciar el proceso:"
echo "************************************************************"
echo

#mato un proceso manualmente
echo "pkill yes" 
pkill yes
echo

#Esperamos un poco, el Demonio debería reiniciar el proceso
sleep 3
#vemos la lista de nuevo
echo "./Fausto.sh list"
./Fausto.sh list

echo
echo "************************************************************"
echo "4) Elimino el proceso usando Fausto."
echo "El Demonio NO debe reiniciar el proceso:"
echo "************************************************************"
echo

#Ahora lo matamos bien, el proceso no debe reniciarse
pid_yes=$(./Fausto.sh list | grep [y]es | cut -d " " -f1)
echo "./Fausto.sh stop $pid_yes"
./Fausto.sh stop $pid_yes
sleep 3
echo "./Fausto.sh list"
./Fausto.sh list


echo
echo "************************************************************"
echo "5) Error sintaxis provocado para que Fausto nos avise:"
echo "************************************************************"
echo

echo "./Fausto.sh asdf"
#esto debería de dar error
./Fausto.sh asdf


echo
echo "************************************************************"
echo "6) Siguiendo la sugerencia anterior veríamos la ayuda:"
echo "************************************************************"
echo

echo "./Fausto.sh help"
./Fausto.sh help


echo
echo "************************************************************"
echo "7) Terminamos la ejecución y vemos los mensajes enviados"
echo "por los procesos lanzados en los ficeheros test 1, 2 y 3"
echo "************************************************************"
echo

#Espero algo más para tener más mensajes y finalmente apagamos todo
sleep 4
./Fausto.sh end
sleep 3 #doy tiempo al Demonio para hacer el Apocalipsis
#y muestro los mensajes, luego lo borro para no dejar basura
cat test1.txt test2.txt test3.txt
rm test1.txt test2.txt test3.txt

echo
echo "************************************************************"
echo "8) Comprobamos que no hay procesos sin terminar."
echo "Esperamos que sólo salgan bash, Ejercicio1.sh y ps" 
echo "************************************************************"
echo

ps
#y tampoco nos hemos dejado procesos colgados (no debe de salir nada aquí)
ps gl | grep [D]emonio


echo
echo "************************************************************"
echo "9) Comprobamos que no hay ficerhos basura."
echo "Solo deben quedar Fausto.sh, Demonio.sh y la Biblia.txt"
echo "************************************************************"
echo

ls 

echo
echo "************************************************************"
echo "10) Comprobamos que no hay bloqueos pendientes"
echo "no debería de salir nada:"
echo "************************************************************"
echo

#ni tampoco debemos de dejar el lock cerrado
lslocks | grep flock


echo
echo "************************************************************"
echo "11) Finalmente mostramos la Biblia"
echo "************************************************************"
echo

cat Biblia.txt


#limpieza, esto debería de ser innecesario pero si por algún motivo no se han cerrado los procesos es bueno hacerlo ahora
pkill yes
pkill Fausto
pkill Demonio
