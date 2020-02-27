public class Movie {

    private String title;
    private String tagline;
    private int year;

    public Movie(String title, String tagline, int year) {
        this.title = title;
        this.tagline = tagline;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getTagline() {
        return tagline;
    }

    public int getYear() {
        return year;
    }
}
