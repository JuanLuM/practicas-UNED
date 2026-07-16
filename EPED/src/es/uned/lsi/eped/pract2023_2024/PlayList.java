package es.uned.lsi.eped.pract2023_2024;

import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.ListIF;

// TERMINADA Y DEBERIA FUNCIONAR.
public class PlayList extends List<Integer> implements PlayListIF{
    
    public PlayList() {}


    @Override
    public ListIF<Integer> getPlayList() {
        return this;
    }


    @Override
    public void addListOfTunes(ListIF<Integer> lT) {
        IteratorIF<Integer> iterator = lT.iterator();
        while (iterator.hasNext()) {
            this.insert(this.size() + 1, iterator.getNext());            
        }
    }

    @Override
    public void removeTune(int tuneID) {
        
        for(int i = this.size(); i >= 1; i--) {
            if(this.get(i) == tuneID) {
                remove(i);
            }                   
        }

    }

}
