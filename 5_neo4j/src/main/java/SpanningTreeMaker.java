import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SpanningTreeMaker {

    private Map<Long, Node> nodes = new HashMap<>();

    public void makeSpanningTree(){
        getAllNodes();
        bfs();
    }

    private void getAllNodes(){
        StatementResult result = Main.getSession().run("MATCH (n)-[r]-(m) " +
                "RETURN ID(n) as fn, ID(m) as sn");
        while (result.hasNext()){
            Record record = result.next();
            long firstNode = record.get("fn").asLong();
            long secondNode = record.get("sn").asLong();
            if (!nodes.containsKey(firstNode))
                nodes.put(firstNode, new Node(firstNode));
            if (!nodes.containsKey(secondNode))
                nodes.put(secondNode, new Node(secondNode));
            nodes.get(firstNode).addNeighbour(nodes.get(secondNode));
            nodes.get(secondNode).addNeighbour(nodes.get(firstNode));
        }
    }

    private void bfs() {
        List<Node> queue = new LinkedList<>();
        nodes.forEach((id, node) -> {
            if (node.getState() == NodeStates.INITIAL){
                node.setState(NodeStates.PROCESSING);
                queue.add(node);

                while (!queue.isEmpty()){
                    Node processingNode = queue.get(0);
                    queue.remove(0);

                    processingNode.getNeighbours().forEach(neighbour -> {
                        if (neighbour.getState() == NodeStates.INITIAL){
                            neighbour.setState(NodeStates.PROCESSING);
                            queue.add(neighbour);
                        }
                        else if (neighbour.getState() == NodeStates.PROCESSING){
                            removeConnection(processingNode.getId(), neighbour.getId());
                        }
                    });
                    processingNode.setState(NodeStates.VISITED);
                }
            }
        });
    }

    private void removeConnection(long id1, long id2){
        Main.getSession().run(String.format("MATCH (n)-[r]-(m) WHERE ID(n)=%d AND ID(m)=%d DELETE r", id1, id2));
    }
}
