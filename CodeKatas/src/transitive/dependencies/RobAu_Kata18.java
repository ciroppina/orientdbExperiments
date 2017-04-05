package transitive.dependencies;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 
 * @author RobAu on StackExchange
 *
 */
public class RobAu_Kata18 {

    private class CyclicDependencyException extends Throwable {

		public String getMessage() {
			return "\nnode already exists, maybe a cycle ";
		}
    }

    //I implement Comparable because I want a nice sorted list when printing.
    class Node implements Comparable<Node> {
        Set<Node> children = new HashSet<Node>();
        String name;

        public Node(String name) {
            this.name = name;
        }

        public boolean addChild(Node n) {
            //if we add a child, all the children of the that node cannot be this node, otherwise we create a cycle.
            if (n.allDescendants().contains(this))
            {
                return false;
            }
            else
            {
                this.children.add(n);
                return true;
            }
        }


        public Set<Node> allDescendants() {
            Set<Node> all = new TreeSet<>();

            for (Node c : children) {
                all.add(c);
                all.addAll(c.allDescendants());
            }
            return all;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return name != null ? name.equals(node.name) : node.name == null;

        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

        @Override
        public String toString() {
            return name + " depends transitively on " + allDescendants().stream().map(x -> x.name).collect(Collectors.joining(","));
        }

        @Override
        public int compareTo(Node o) {
            return name.compareTo(o.name);
        }
    }

    private Map<String, Node> nodesMap = new HashMap<String, Node>();

    private void print() {
        for (String name : nodesMap.keySet()) {
            System.out.println(nodesMap.get(name).toString());
        }
    }

    /**
     * Get a node from the Map if it already exists, else create it and put it in the map.
     */
    public Node getOrCreateNode(String nodeName) {
        Node currentNode;
        if (!nodesMap.containsKey(nodeName)) {
            currentNode = new Node(nodeName);
            nodesMap.put(nodeName, currentNode);
        } else {
            currentNode = nodesMap.get(nodeName);
        }
        return currentNode;
    }

    public boolean tryDirectDependency(String rawDependency) throws CyclicDependencyException {
        String[] elements = rawDependency.split(" ");

        String nodeName = elements[0];

        Node node = getOrCreateNode(nodeName);

        for (int i = 1; i < elements.length; i++) {
            if (!node.addChild(getOrCreateNode(elements[i]))) {
                throw new CyclicDependencyException();
            }
        }
        return true;
    }

    public static void main(String args[]) {

        try {
            RobAu_Kata18 codeKata = new RobAu_Kata18();
            codeKata.tryDirectDependency("A B C");
            codeKata.tryDirectDependency("B C E");
            codeKata.tryDirectDependency("C G");
            codeKata.tryDirectDependency("D A F");
            codeKata.tryDirectDependency("E F");
            codeKata.tryDirectDependency("F H");

            codeKata.print();
        } catch (CyclicDependencyException e) {
            e.printStackTrace();
        }

        try {
            RobAu_Kata18 codeKata2 = new RobAu_Kata18();
            codeKata2.tryDirectDependency("A B");
            codeKata2.tryDirectDependency("B C");
            codeKata2.print();
            codeKata2.tryDirectDependency("C A");
            codeKata2.print();
        } catch (CyclicDependencyException e) {
            e.printStackTrace();
        }
    }
}