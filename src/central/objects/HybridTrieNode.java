package central.objects;

import java.util.HashMap;
import java.util.Map;

public class HybridTrieNode {
    public Map<String, HybridTrieNode> children;
    public boolean isEndOfPrefix;
    public String destination;

    public HybridTrieNode() {
        this.children = new HashMap<>();
        this.isEndOfPrefix = false;
        this.destination = null;
    }
}
