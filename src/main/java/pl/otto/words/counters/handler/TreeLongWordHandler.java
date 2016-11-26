package pl.otto.words.counters.handler;

import pl.otto.words.counters.DistinctCounter;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by tomek on 26.11.16.
 */
public class TreeLongWordHandler implements LongWordCounterHandler {

    private DistinctCounter counter;
    private Node rootNode;

    public TreeLongWordHandler(DistinctCounter counter) {
        this.counter = counter;
        this.rootNode = new Node();
    }

    @Override
        public void handleWord(String word, long numberFromWord) {

        boolean existsInTree = true;
        Node currentNode = rootNode;
        for (int i = 0; i < word.length(); ++i) {
            char c = word.charAt(i);

            Node childNode = currentNode.getChild(c);
            if (childNode == null) {
                Node newNode = currentNode.addChild(c);
                currentNode = newNode;
                existsInTree = false;
            } else {
                currentNode = childNode;
            }
        }

        if (!existsInTree) {
            counter.updateUniqueCharsMap(word);
        }
    }

    private static class Node implements Comparable<Node> {
        private char c;
        private SortedSet<Node> childNodes;

        public Node() {
            this('X');
        }

        public Node(char c) {
            this.c = c;
            childNodes = new TreeSet<>();
        }

        public Node addChild(char c) {
            Node newNode = new Node(c);
            childNodes.add(newNode);
            return newNode;
        }

        public Node getChild(char c) {
            for (Node child : childNodes) {
                if (child.getChar() == c) {
                    return child;
                }
            }
            return null;
        }

        public char getChar() {
            return c;
        }

        public SortedSet<Node> getChildNodes() {
            return childNodes;
        }

        @Override
        public int compareTo(Node other) {
            return getChar() - other.getChar();
        }
    }
}
