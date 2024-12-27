package central.objects;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    public Map<String, TrieNode> children; // Map for compressed trie
    public boolean isEndOfPrefix;
    public String destination;

    public TrieNode() {
        this.children = new HashMap<>();
        this.isEndOfPrefix = false;
        this.destination = null;
    }
}
