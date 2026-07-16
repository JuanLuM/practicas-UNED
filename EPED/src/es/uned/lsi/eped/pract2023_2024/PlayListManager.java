package es.uned.lsi.eped.pract2023_2024;

import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.ListIF;

// PINTA A TERMINADO, VOLVERÉ
public class PlayListManager implements PlayListManagerIF{

    
    private List<PlayList> playLists = new List<>();
    private List<String> playListIDs = new List<>();

    @Override
    public boolean contains(String playListID) {
        return getPosition(playListID) != -1;
    }

    @Override
    public PlayListIF getPlayList(String playListID) {

        int p;
        if((p = getPosition(playListID)) != -1) {
            return playLists.get(p);
        }

        return null;
    }

    @Override
    public ListIF<String> getIDs() {
        return new List<String>(playListIDs); 
    }

    @Override
    public void createPlayList(String playListID) {
        
        if(!contains(playListID)) {
            playLists.insert(playLists.size() + 1, new PlayList() );
            playListIDs.insert(playListIDs.size() + 1, playListID);
        }

    }

    @Override
    public void removePlayList(String playListID) {
            int position = getPosition(playListID);
            if(position != -1) {
                playListIDs.remove(position);
                playLists.remove(position);
            }
    }

    /* Mis métodos auxiliares */
    private int getPosition(String playListID) {

        IteratorIF<String> it = playListIDs.iterator();
        for(int i = 1; it.hasNext(); i++) {
            if(it.getNext().equals(playListID)){
                return i;
            }
        }
        return -1;
    }

}
