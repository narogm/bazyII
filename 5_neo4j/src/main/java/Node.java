import java.util.HashSet;
import java.util.Set;

public class Node {
    private long id;
    private Set<Node> neighbours = new HashSet<>();
    private NodeStates state;

    public Node(long id) {
        this.id = id;
        this.state = NodeStates.INITIAL;
    }

    public long getId() {
        return id;
    }

    public NodeStates getState() {
        return state;
    }

    public void setState(NodeStates state) {
        this.state = state;
    }

    public void addNeighbour(Node neighbour){
        neighbours.add(neighbour);
    }

    public Set<Node> getNeighbours() {
        return neighbours;
    }
}
