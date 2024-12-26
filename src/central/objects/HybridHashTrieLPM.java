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
    private String defaultGateway = "0.0.0.0/0";
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
        HybridTrieNode node = root;
    
        for (char bit : binaryPrefix.toCharArray()) {
            String bitStr = String.valueOf(bit);
            node.children.putIfAbsent(bitStr, new HybridTrieNode());
            node = node.children.get(bitStr);
        }
    
        node.isEndOfPrefix = true;
        node.destination = destination;
    }
    

    public String longestPrefixMatch(String ip) {
        if (prefixCache.containsKey(ip)) {
            return prefixCache.get(ip);
        }
    
        String binaryIp = convertToBinary(ip);
        String bestMatch = null;
        int longestPrefixLength = -1;
    
        // Check hash table for short prefixes
        for (Map.Entry<String, String> entry : hashTable.entrySet()) {
            String prefix = entry.getKey();
            if (binaryIp.startsWith(prefix)) {
                int prefixLength = prefix.length();
                if (prefixLength > longestPrefixLength) {
                    longestPrefixLength = prefixLength;
                    bestMatch = entry.getValue();
                }
            }
        }
    
        // Search trie for long prefixes
        HybridTrieNode node = root;
        StringBuilder matchedPrefix = new StringBuilder();
    
        for (char bit : binaryIp.toCharArray()) {
            String bitStr = String.valueOf(bit);
            if (node.children.containsKey(bitStr)) {
                matchedPrefix.append(bitStr);
                node = node.children.get(bitStr);
    
                if (node.isEndOfPrefix) {
                    longestPrefixLength = matchedPrefix.length();
                    bestMatch = node.destination;
                }
            } else {
                break;
            }
        }
    
        // Cache the result
        String result = (bestMatch != null) ? bestMatch : defaultGateway; // Fallback to default gateway
        if (prefixCache.size() >= CACHE_SIZE) {
            prefixCache.keySet().iterator().remove();
        }
        prefixCache.put(ip, result);
    
        return result;
    }
    



    private String convertToBinary(String ip) {
        String[] parts = ip.split("/");
        String ipPart = parts[0];
        int maskLength = parts.length > 1 ? Integer.parseInt(parts[1]) : 32; // Default to /32 if no mask
    
        String[] octets = ipPart.split("\\.");
        StringBuilder binary = new StringBuilder();
    
        for (String octet : octets) {
            binary.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(octet))).replace(' ', '0'));
        }
    
        // Respect subnet mask length
        return binary.substring(0, maskLength);
    }    
}
