package es.uned.lsi.eped.pract2023_2024;

import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.ListIF;

public class Player implements PlayerIF{

    private TuneCollection tuneCollection;
    private PlayListManager playListManager;
    private PlayBackQueue playBackQueue;
    private RecentlyPlayed recentlyPlayed;

    public Player(TuneCollection tuneCollection, int recentlyPlayedMaxSize) {
        this.tuneCollection = tuneCollection;
        this.playListManager = new PlayListManager();
        this.playBackQueue = new PlayBackQueue();
        this.recentlyPlayed = new RecentlyPlayed(recentlyPlayedMaxSize);
    }

    @Override
    public ListIF<String> getPlayListIDs() {
        return playListManager.getIDs();
    }

    @Override
    public ListIF<Integer> getPlayListContent(String playListID) {
        
        if(playListManager.contains(playListID))
            return playListManager.getPlayList(playListID).getPlayList();

        return new List<Integer>();
        
    }

    @Override
    public ListIF<Integer> getPlayBackQueue() {
        return playBackQueue.getContent();
    }

    @Override
    public ListIF<Integer> getRecentlyPlayed() {
        return recentlyPlayed.getContent();
    }

    @Override
    public void createPlayList(String playListID) {
        playListManager.createPlayList(playListID);
    }

    @Override
    public void removePlayList(String playListID) {
        playListManager.removePlayList(playListID);
    }

    @Override
    public void addListOfTunesToPlayList(String playListID, ListIF<Integer> lT) {
        if(playListManager.contains(playListID))
            playListManager.getPlayList(playListID).addListOfTunes(lT);
    }

    @Override
    public void addSearchToPlayList(String playListID, String t, String a, String g, String al, int min_y, int max_y,
            int min_d, int max_d) {

                Query q = new Query(t,a,g,al, min_y, max_y, min_d,max_d);
                
                if(playListManager.contains(playListID)) {
                    
                    PlayList pl = (PlayList)playListManager.getPlayList(playListID);
                    for(int i = 0; i < tuneCollection.size(); i++){
                        if(tuneCollection.getTune(i).match(q)) {
                            pl.insert(pl.size() + 1, i);
                        }
                    }                
                }
    }

    @Override
    public void removeTuneFromPlayList(String playListID, int tuneID) {
        if(playListManager.contains(playListID))
            playListManager.getPlayList(playListID).removeTune(tuneID);
    }

    @Override
    public void addListOfTunesToPlayBackQueue(ListIF<Integer> lT) {
        playBackQueue.addTunes(lT);
    }

    @Override
    public void addSearchToPlayBackQueue(String t, String a, String g, String al, int min_y, int max_y, int min_d,
            int max_d) {
                
                Query q = new Query(t,a,g,al, min_y, max_y, min_d,max_d);
                List<Integer> pl = new List<Integer>();
                for(int i = 0; i < tuneCollection.size(); i++){
                    if(tuneCollection.getTune(i).match(q)) {
                        pl.insert(pl.size() + 1, i);
                    }
                }
                playBackQueue.addTunes(pl);       
    }

    @Override
    public void addPlayListToPlayBackQueue(String playListID) {
        if(playListManager.contains(playListID))
            playBackQueue.addTunes(playListManager.getPlayList(playListID).getPlayList());
    }

    @Override
    public void clearPlayBackQueue() {
        playBackQueue.clear();
    }

    @Override
    public void play() {
        if(!playBackQueue.isEmpty()) {
            int tune = playBackQueue.getFirstTune();
            playBackQueue.extractFirstTune();
            recentlyPlayed.addTune(tune);
        }
    }
    
}
