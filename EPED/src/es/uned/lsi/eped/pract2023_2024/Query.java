package es.uned.lsi.eped.pract2023_2024;

// TERMINADA, CREO VOLVERÉ
public class Query implements QueryIF{

    private String titleString = "";
    private String authorString = "";
    private String genreString = "";
    private String albumString = "";
    private int min_year = -1;
    private int max_year = -1;
    private int min_duration = -1;
    private int max_duration = -1;

    

    public Query(String titleString, String authorString, String genreString, String albumString, int min_year,
            int max_year, int min_duration, int max_duration) {
        this.titleString = titleString;
        this.authorString = authorString;
        this.genreString = genreString;
        this.albumString = albumString;
        this.min_year = min_year;
        this.max_year = max_year;
        this.min_duration = min_duration;
        this.max_duration = max_duration;
    }

    @Override
    public String getTitle() {
        return titleString;
    }

    @Override
    public String getAuthor() {
        return authorString;
    }

    @Override
    public String getGenre() {
        return genreString;
    }

    @Override
    public String getAlbum() {
        return albumString;
    }

    @Override
    public int getMin_year() {
        return min_year;
    }

    @Override
    public int getMax_year() {
        return max_year;
    }

    @Override
    public int getMin_duration() {
        return min_duration;
    }

    @Override
    public int getMax_duration() {
        return max_duration;
    }

}
