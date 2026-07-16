package es.uned.lsi.eped.pract2023_2024;

// TERMINADA CREO, VOLVERÉ
public class Tune implements TuneIF{

    private final String titleString;
    private final String authorString;
    private final String genreString;
    private final String albumString;
    private final int year;
    private final int duration;



    public Tune(String titleString, String authorString, String genreString, String albumString, int year, int duration) {
        this.titleString = titleString;
        this.authorString = authorString;
        this.genreString = genreString;
        this.albumString = albumString;
        this.year = year;
        this.duration = duration;
    }

    @Override
    public boolean match(QueryIF q) {
        
        return  (q.getTitle().equals("") || q.getTitle().toLowerCase().equals(titleString.toLowerCase())) &&
                (q.getAuthor().equals("") || q.getAuthor().toLowerCase().equals(authorString.toLowerCase())) &&
                (q.getGenre().equals("") || q.getGenre().toLowerCase().equals(genreString.toLowerCase())) &&
                (q.getAlbum().equals("") || q.getAlbum().toLowerCase().equals(albumString.toLowerCase())) &&
                (q.getMin_year() == -1 || q.getMin_year() <= year) &&
                (q.getMax_year() == -1 || q.getMax_year() >= year) &&
                (q.getMin_duration() == -1 || q.getMin_duration() <= duration) &&
                (q.getMax_duration() == -1 || q.getMax_duration() >= duration);
        
    }

}
