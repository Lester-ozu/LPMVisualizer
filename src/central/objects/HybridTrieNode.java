package central.objects;

import java.util.HashMap;
import java.util.Map;

public class HybridTrieNode {
    Map<String, HybridTrieNode> children;
    boolean isEndOfPrefix;
    String destination;

    public HybridTrieNode() {
        this.children = new HashMap<>();
        this.isEndOfPrefix = false;
        this.destination = null;
    }
}
