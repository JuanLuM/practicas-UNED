#!/bin/bash

# TRABAJO II: Combate de procesos
# Este ejercicio consiste en un combate entre varios procesos hijos que será arbitrada por el
# proceso padre.

# Para ello deberán implementare dos archivos de código en C: padre.c e hijo.c. Además
# deberá implementarse un fichero de script Ejercicio2.sh que compile dichos archivos
# fuente y genere los ejecutables PADRE e HIJO, cree un fichero FIFO resultado y limpie
# todos los ficheros creados al finalizar el combate.

#este archivo es un scrip que:

clear

# Accede al directorio donde estan los programas
cd Trabajo2


echo 'Matando procesos HIJO que se hubieran quedado corriendo ...'
killall HIJO
echo 'Matando procesos PADRE que se hubieran quedado corriendo ...'
killall PADRE

#1 compila los fuentes padre.c e hijo.c con gcc
echo 'Compilando padre.c ...'
gcc padre.c -o PADRE
echo 'Compilando hijo.c ...'
gcc hijo.c -o HIJO
#2 crea el fihero fifo "resultado"
echo 'Creando tubería resultado ...'
mkfifo resultado
#lanza un cat en segundo plano para leer "resultado"  
cat resultado &
#lanza el proceso padre
sleep 1
./PADRE 10
#al acabar limpia todos los ficheros que ha creado
echo 'Limpiando los ficheros creados ...'
rm PADRE
rm HIJO
rm resultado
echo 'Terminado. OK!'
