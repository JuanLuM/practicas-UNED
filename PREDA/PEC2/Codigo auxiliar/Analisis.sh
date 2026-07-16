#!/bin/bash

# config -----------------------------------------------------
DIMENSIONES=100
ITERACIONES_POR_DIMENSION=5
JAVACOMAND='java'
JAVAARGS='-Xms1g -Xmx8g -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=heapdump.hprof'
JARFILE="hanoi.jar"

# Entrada
INPUTPREFIX=""
INPUTFILEBASENAME="entrada"
INPUTFILEEXTENSION=".txt"
INPUTFOLDER="ENTRADA"

# Salida
OUTPUTFOLDER="SALIDA"
OUTPUTFILE="Analisis.csv"

# crea el directorio de salida
mkdir $OUTPUTFOLDER
# crea el archivo de salida
OUTPUT_PATH="$OUTPUTFOLDER/$OUTPUTFILE"

#-------------------------------------------------------------

#imprime el nombre de las columnas de la salida en el archivo de salida
nombreColumnas=""
for i in $(seq 1 $ITERACIONES_POR_DIMENSION); do
  nombreColumnas="$nombreColumnas;Ex$i"
done
echo "Entrada${nombreColumnas};Media;" > $OUTPUT_PATH

for i in $(seq 1 $DIMENSIONES); do
  OUTPUT=$(echo "$INPUTPREFIX$INPUTFILEBASENAME$i$INPUTFILEEXTENSION")
  #Ejecuta el programa varias veces con el mismo archivo
  for j in $(seq 1 $ITERACIONES_POR_DIMENSION); do
      OUTPUT="$OUTPUT;$("$JAVACOMAND" $JAVAARGS -jar $JARFILE "$INPUTFOLDER/$INPUTPREFIX$INPUTFILEBASENAME$i$INPUTFILEEXTENSION" "$OUTPUTFOLDER/$INPUTPREFIX${INPUTFILEBASENAME}_OUTUPUT$i.txt")"
  done
  echo $OUTPUT >> $OUTPUT_PATH
done
