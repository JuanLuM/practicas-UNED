package es.uned.lsi.eped.pract2023_2024;

import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.ListIF;

public class RecentlyPlayed extends List<Integer> implements RecentlyPlayedIF {

    private int maxSize;

    

    public RecentlyPlayed(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public ListIF<Integer> getContent() {
        return new List<Integer>(this);
    }

    @Override
    public void addTune(int tuneID) {

        this.insert(1, tuneID);
        if(this.size() > maxSize) {
            this.remove(this.size());
        }
    
    }

}
