import org.neo4j.driver.v1.*;

public class Main implements AutoCloseable {

    private final Driver driver;
    private static Session session;

    public Main(String uri, String user, String password) {
        this.driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        session = driver.session();
    }

    public static Session getSession(){
        return session;
    }

    @Override
    public void close() throws Exception {
        session.close();
        driver.close();
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main("bolt://localhost:7687", "neo4j", "password");
//        Main main = new Main("bolt://localhost:7687", "neo4j", "12345");  //dla spanning tree

        main.zad3();
//        main.zad4();
//        main.zad5();
//        main.zad6();
//        main.zad7();
//        main.zad7b();
//        main.zad9();
//        main.zad10();
//        main.zad12();

//        SpanningTreeMaker stMaker = new SpanningTreeMaker();
//        stMaker.makeSpanningTree();

        main.close();
    }

    public void zad3() {
        StatementResult result = session.run("MATCH (p: Person)-[:ACTED_IN]->(m: Movie) " +
                "WHERE p.name = 'Tom Cruise' RETURN m.title AS title");
        while (result.hasNext()) {
            Record record = result.next();
            System.out.println(record.get("title").asString());
        }
    }

    public void zad4() {
        Factory factory = new Factory();
        Movie movie1 = factory.createNewMovie("Simple Movie", "Random tagline", 2010);
        Movie movie2 = factory.createNewMovie("Test Movie", "Test tagline", 2015);

        Person person1 = factory.createNewPerson("Jan Kowalski",1980);
        Person person2 = factory.createNewPerson("Kazimierz Piotrowski",1990);
        Person person3 = factory.createNewPerson("Tomasz Dabrowski",1975);

        person1.actedIn(movie1);
        person2.actedIn(movie1);
        person2.actedIn(movie2);
        person3.actedIn(movie2);
    }

    public void zad5(){
        Person jan = new Person("Jan Kowalski",1980);

        jan.setBirthplace("Krakow");
        jan.setBirthdate("05.01.1980");
    }

    public void zad6(){
        String name = "Jan Kowalski";
        String newBirthplace = "Warszawa";

        getSession().run(String.format("MATCH (p: Person {name : '%s'})" +
                " SET p.birthplace = '%s'", name, newBirthplace));
    }

    public void zad7(){
        int minMoviesAmount = 2;
        StatementResult result = getSession().run(String.format("MATCH (p: Person)-[:ACTED_IN]->(m: Movie) " +
                "WITH p, collect(m.title) as movies " +
                "WHERE length(movies) >= %d " +
                "RETURN p.name AS name, length(movies) AS m_length", minMoviesAmount));
        while(result.hasNext()){
            Record record = result.next();
            System.out.println(record.get("name").asString() + " : " + record.get("m_length"));
        }
    }

    public void zad7b(){
        StatementResult result = getSession().run("MATCH (p: Person)-[:ACTED_IN]->(m: Movie) " +
                "WITH p, collect(m.title) as movies " +
                "WHERE length(movies) >= 3 " +
                "RETURN avg(length(movies)) AS average" );
        while(result.hasNext()){
            Record record = result.next();
            System.out.println(record.get("average"));
        }
    }

    public void zad9(){
        long id1 = 183; //Jan Kowalski
        long id2 = 40; //Keanu Reeves

        getSession().run(String.format("MATCH r=((f)-[*]->(s)) WHERE ID(f)=%d AND ID(s)=%d " +
                "FOREACH (n IN nodes(r)[1..-1] | SET n.additionalValue = 'TestValue')", id1, id2));
    }

    public void zad10(){
        long id1 = 183; //Jan Kowalski
        long id2 = 40; //Keanu Reeves

        StatementResult result = getSession().run(String.format("MATCH (f)--(n)-[*2]->(s) WHERE ID(f)=%d AND ID(s)=%d " +
                "RETURN n", id1, id2));
        while(result.hasNext()){
            Record record = result.next();
            System.out.println(record);
        }
    }

    public void zad12(){
        Factory factory = new Factory();

        Movie movie1 = factory.createNewMovie("Movie1", "Tagline1", 2010);
        Movie movie2 = factory.createNewMovie("Movie2", "Tagline2", 2015);

        Person person1 = factory.createNewPerson("Person1",1980);
        Person person2 = factory.createNewPerson("Person2",1990);
        Person person3 = factory.createNewPerson("Person3",1975);

        person1.actedIn(movie1);
        person2.actedIn(movie1);
        person2.actedIn(movie2);
        person3.actedIn(movie2);
    }
}
