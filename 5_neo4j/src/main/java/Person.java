public class Person {

    private String name;
    private int year;
    private String birthdate;
    private String birthplace;

    public Person(String name, int year) {
        this.name = name;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public void actedIn(Movie movie) {
        Main.getSession().run(String.format("MATCH (p: Person {name: '%s', born: %d}), " +
                        "(m: Movie {title: '%s', tagline: '%s', released: %d})" +
                        "CREATE (p)-[:ACTED_IN]->(m)",
                getName(), getYear(), movie.getTitle(), movie.getTagline(), movie.getYear()));
    }

    public void setBirthdate(String birthdate) {
        Main.getSession().run(String.format("MATCH (p: Person {name: '%s', born: %d}) " +
                        "SET p.birthdate = '%s' RETURN p",
                getName(), getYear(), birthdate));
        this.birthdate = birthdate;
    }

    public void setBirthplace(String birthplace) {
        Main.getSession().run(String.format("MATCH (p: Person {name: '%s', born: %d}) " +
                        "SET p.birthplace = '%s'",
                getName(), getYear(), birthplace));
        this.birthplace = birthplace;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getBirthplace() {
        return birthplace;
    }
}
