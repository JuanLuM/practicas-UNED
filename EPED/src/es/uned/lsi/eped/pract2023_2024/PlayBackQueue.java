package es.uned.lsi.eped.pract2023_2024;

import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.ListIF;
import es.uned.lsi.eped.DataStructures.Queue;

// TERMINADO CREO, VOLVERÉ
public class PlayBackQueue extends Queue<Integer> implements PlayBackQueueIF{

    public PlayBackQueue() {
        super();
    }

    @Override
    public ListIF<Integer> getContent() {
        ListIF<Integer> list = new List<Integer>();
        IteratorIF<Integer> it = this.iterator();
        while (it.hasNext()) {
            list.insert(list.size()+1, it.getNext());
        }
        return list;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public int getFirstTune() {
        return super.getFirst();
    }

    @Override
    public void extractFirstTune() {
        super.dequeue();
    }

    @Override
    public void addTunes(ListIF<Integer> lT) {

        IteratorIF<Integer> it = lT.iterator();
        while (it.hasNext()) {
            super.enqueue(it.getNext());            
        }

    }

    @Override
    public void clear() {
        super.clear();
    }

}
