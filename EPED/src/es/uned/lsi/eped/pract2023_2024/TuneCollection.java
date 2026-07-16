package es.uned.lsi.eped.pract2023_2024;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.ListIF;

public class TuneCollection implements TuneCollectionIF{

	private Tune[] collection;
	
	//recibe el path del fichero de canciones.
	public TuneCollection(String fileTunes) throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(new 
				FileInputStream(fileTunes), "UTF-8"));
		String line;
		//1ª linea tiene el nombre de los campos, se lee y se ignora.
		line = in.readLine();
		ListIF<Tune> L = new List<Tune>();
		while((line = in.readLine())!=null) {
			 StringTokenizer st = new StringTokenizer(line,"\t");
                         Tune T = new Tune(st.nextToken(),
                                           st.nextToken(),
                                           st.nextToken(),
                                           st.nextToken(),
                                           Integer.valueOf(st.nextToken()),
                                           Integer.valueOf(st.nextToken()));
			 L.insert(1, T);
		} 
		in.close();
		collection = new Tune[L.size()];
		int cont = L.size()-1;
		IteratorIF<Tune> iteratorL = L.iterator();
		while(iteratorL.hasNext()){
			Tune T = iteratorL.getNext();
			collection[cont] = T;
			cont--;
		}
	}
	
	//pre-cond: 0<=ID<this.size().
	public Tune getTune(int ID){
		return collection[ID];
	}
	
	public int size(){
		return collection.length;
	}
	
}
