package central.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class HybridTrieNode {
    Map<String, HybridTrieNode> children;
    boolean isEndOfPrefix;
    String destination;

    public HybridTrieNode() {
        this.children = new HashMap<>();
        this.isEndOfPrefix = false;
        this.destination = null;
    }
}

public class HybridHashTrieLPM {
    private HybridTrieNode root;
    private Map<String, String> prefixCache; // Cache for Longest Prefix Match results
    private Map<String, String> hashTable;   // Hash table for quick lookup
    private static final int CACHE_SIZE = 100; // Limit cache size

    public HybridHashTrieLPM(List<IPRoute> routes) {
        root = new HybridTrieNode();
        prefixCache = new HashMap<>(CACHE_SIZE);
        hashTable = new HashMap<>();

        for (IPRoute route : routes) {
            insert(route.getPrefix(), route.getDestination());
        }
    }

    private void insert(String prefix, String destination) {
        String binaryPrefix = convertToBinary(prefix);

        if (binaryPrefix.length() <= 16) {
            // For short prefixes, use a hash table for quick lookup
            hashTable.put(binaryPrefix, destination);
        } else {
            // For longer prefixes, insert into the trie
            HybridTrieNode node = root;
            for (int i = 0; i < binaryPrefix.length();) {
                StringBuilder currentPrefix = new StringBuilder();
                while (i < binaryPrefix.length() && (node.children.isEmpty() || node.children.size() == 1)) {
                    currentPrefix.append(binaryPrefix.charAt(i));
                    i++;
                }

                node.children.putIfAbsent(currentPrefix.toString(), new HybridTrieNode());
                node = node.children.get(currentPrefix.toString());
            }
            node.isEndOfPrefix = true;
            node.destination = destination;
        }
    }

    public String longestPrefixMatch(String ip) {
        if (prefixCache.containsKey(ip)) {
            return prefixCache.get(ip);
        }

        String binaryIp = convertToBinary(ip);
        String bestMatch = null;

        // First, try using the hash table for quick lookups for short prefixes
        if (binaryIp.length() <= 16 && hashTable.containsKey(binaryIp)) {
            bestMatch = hashTable.get(binaryIp);
        } else {
            HybridTrieNode node = root;
            for (int i = 0; i < binaryIp.length();) {
                boolean matched = false;

                for (String prefix : node.children.keySet()) {
                    if (binaryIp.startsWith(prefix, i)) {
                        node = node.children.get(prefix);
                        i += prefix.length();
                        if (node.destination != null) {
                            bestMatch = node.destination;
                        }
                        matched = true;
                        break;
                    }
                }

                if (!matched) {
                    break;
                }
            }
        }

        // Cache the result
        String result = bestMatch != null ? bestMatch : "0.0.0.0/0";
        if (prefixCache.size() >= CACHE_SIZE) {
            String oldestKey = prefixCache.keySet().iterator().next();
            prefixCache.remove(oldestKey);
        }
        prefixCache.put(ip, result);

        return result;
    }

    private String convertToBinary(String ip) {
        if (ip.contains("/")) {
            ip = ip.split("/")[0];  // Remove subnet mask part
        }

        String[] parts = ip.split("\\.");
        StringBuilder binary = new StringBuilder();

        for (String part : parts) {
            binary.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(part))).replace(' ', '0'));
        }
        return binary.toString();
    }
}
