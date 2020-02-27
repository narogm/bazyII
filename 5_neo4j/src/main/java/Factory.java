public class Factory {

    public Movie createNewMovie(String title, String tagline, int year){
        Main.getSession().run(String.format("CREATE (m: Movie {title: '%s', tagline: '%s', released: %d})",
                title, tagline, year));
        return new Movie(title,tagline, year);
    }

    public Person createNewPerson(String name, int year){
        Main.getSession().run(String.format("CREATE (p: Person {name: '%s', born: %d})", name, year));
        return new Person(name, year);
    }
}
