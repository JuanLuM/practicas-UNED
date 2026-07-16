package es.uned.lsi.eped.pract2023_2024;

import java.util.StringTokenizer;

import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.ListIF;

public class Operation {

	//ID de la operación
	private String opName;
	//Lista de argumentos
	private ListIF<String> args;
	
	Operation(){
		opName = "";
		args = new List<String>();
	}
	//lee operación a partir de una línea del fichero de operaciones
	Operation(String line){
		this();
		//separar los tokens de la línea por tabuladores (formato TSV)
		StringTokenizer st = new StringTokenizer(line,"\t");
		//el primer token es el nombre de la operación
		opName = st.nextToken();
		//los siguientes tokens son los argumentos de la operación en orden
		while(st.hasMoreTokens()){
			String arg = st.nextToken();
			args.insert(args.size()+1,arg);
		}
	}

	
	private String getString(String arg){
		if(arg.startsWith("\"")  && arg.endsWith("\"") ) {
			return arg.substring(1, arg.length()-1);
		}
		return arg;
	}
	
	private void printList(ListIF<?> L){
		IteratorIF<?> it = L.iterator();
		int size = L.size();
		int cont = 1;
		while(it.hasNext()){
			System.out.print(it.getNext());
			if(cont<size){
				System.out.print(",");
			}
			cont++;
		}
	}
	
	//ejecuta la operación correspondiente tras tratar los argumentos
	//se muestra por pantalla el resultado de la operación
	public void execute(Player player){


		if(opName.equalsIgnoreCase("addListOfTunesToPlayBackQueue")){
			ListIF<Integer> L = new List<Integer>();
			IteratorIF<String> I = args.iterator();
			while(I.hasNext()){
				int i = Integer.valueOf(I.getNext());
				L.insert(L.size()+1,i);
			}
			// Imprime la llamada al método
			System.out.print(opName+"([");
			printList(L);
			System.out.println("]);");
			// Llama al método
			player.addListOfTunesToPlayBackQueue(L);
			return;
		}
		if(opName.equalsIgnoreCase("addListOfTunesToPlayList")){
			ListIF<Integer> L = new List<Integer>();
			IteratorIF<String> I = args.iterator();
			String playListID = getString(I.getNext());
			while(I.hasNext()){
				int i = Integer.valueOf(I.getNext());
				L.insert(L.size()+1,i);
			}
			// Imprime la llamada al método
			System.out.print(opName+"(\""+playListID+"\"");
			System.out.print(",[");
			printList(L);
			System.out.println("]);");
			// Llama al método
			player.addListOfTunesToPlayList(playListID, L);
			return;

		}
		if(opName.equalsIgnoreCase("addPlayListToPlayBackQueue")){
			String playListID = getString(args.get(1));
			// Imprime la llamada al método
			System.out.print(opName+"(");
			printList(args);
			System.out.println(");");
			// Llama al método
			player.addPlayListToPlayBackQueue(playListID);
			return;
		}
		if(opName.equalsIgnoreCase("addSearchToPlayBackQueue")){
			String title = getString(args.get(1));
			String author = getString(args.get(2));
			String genre = getString(args.get(3));
			String album = getString(args.get(4));
			int min_year = Integer.valueOf(args.get(5));
			int max_year = Integer.valueOf(args.get(6));
			int min_duration = Integer.valueOf(args.get(7));
			int max_duration = Integer.valueOf(args.get(8));
			// Imprime la llamada al método
			System.out.print(opName+"(");
			printList(args);
			System.out.println(");");
			// Llama al método
			player.addSearchToPlayBackQueue(title, author, genre, album, min_year, max_year, min_duration, max_duration);
			return;
		}
		if(opName.equalsIgnoreCase("addSearchToPlayList")){
			String playListID = getString(args.get(1));
			String title = getString(args.get(2));
			String author = getString(args.get(3));
			String genre = getString(args.get(4));
			String album = getString(args.get(5));
			int min_year = Integer.valueOf(args.get(6));
			int max_year = Integer.valueOf(args.get(7));
			int min_duration = Integer.valueOf(args.get(8));
			int max_duration = Integer.valueOf(args.get(9));
			// Imprime la llamada al método
			System.out.print(opName+"(");
			printList(args);
			System.out.println(");");
			// Llama al método
			player.addSearchToPlayList(playListID, title, author, genre, album, min_year, max_year, min_duration, max_duration);
			return;
		}

		if(opName.equalsIgnoreCase("clearPlayBackQueue")){
			// Imprime la llamada al método
			System.out.println(opName+"();");
			// Llama al método
			player.clearPlayBackQueue();
			return;
		}
		if(opName.equalsIgnoreCase("createPlayList")){
			String playListID = getString(args.get(1));
			// Imprime la llamada al método
			System.out.print(opName+"(");
			printList(args);
			System.out.println(");");
			// Llama al método
			player.createPlayList(playListID);
			return;
		}
		if(opName.equalsIgnoreCase("play")){
			// Imprime la llamada al método
			System.out.println(opName+"();");
			// Llama al método
			player.play();
			return;
		}	
		if(opName.equalsIgnoreCase("removePlayList")){
			String playListID = getString(args.get(1));
			// Imprime la llamada al método
			System.out.print(opName+"(");
			printList(args);
			System.out.println(");");
			// Llama al método
			player.removePlayList(playListID);
			return;
		}
		if(opName.equalsIgnoreCase("removeTuneFromPlayList")){
			String playListID = getString(args.get(1));
			int tuneID = Integer.valueOf(args.get(2));
			// Imprime la llamada al método
			System.out.print(opName+"(");
			printList(args);
			System.out.println(");");
			// Llama al método
			player.removeTuneFromPlayList(playListID, tuneID);
			return;
		}
		if(opName.equalsIgnoreCase("getPlayBackQueue")){
			// Imprime la llamada al método
			System.out.println(opName+"();");
			// Llama al método
			ListIF<Integer> L = player.getPlayBackQueue();
			// Imprime la salida
			printList(L);
			System.out.println();
			return;
		}
		if(opName.equalsIgnoreCase("getPlayListContent")){
			String playListID = getString(args.get(1));
			// Imprime la llamada al método
			System.out.print(opName+"(");
			printList(args);
			System.out.println(");");
			// Llama al método
			ListIF<Integer> L = player.getPlayListContent(playListID);
			// Imprime la salida
			printList(L);
			System.out.println();
			return;
		}
		if(opName.equalsIgnoreCase("getPlayListIDs")){
			// Imprime la llamada al método
			System.out.println(opName+"();");
			// Llama al método
			ListIF<String> L = player.getPlayListIDs();
			// Imprime la salida
			printList(L);
			System.out.println();
			return;
		}
		if(opName.equalsIgnoreCase("getRecentlyPlayed")){
			// Imprime la llamada al método
			System.out.println(opName+"();");
			// Llama al método
			ListIF<Integer> L = player.getRecentlyPlayed();
			// Imprime la salida
			printList(L);
			System.out.println();
		}
	
	}
	
	
	
}
