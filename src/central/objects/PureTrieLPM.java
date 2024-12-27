package central.objects;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PureTrieLPM {
    private TrieNode root;
    private String defaultGateway = "0.0.0.0/0";
    private Map<String, String> prefixCache; // Cache for Longest Prefix Match results
    private static final int CACHE_SIZE = 100; // Limit cache size

    public PureTrieLPM(List<IPRoute> routes) {
        root = new TrieNode();
        prefixCache = new LinkedHashMap<>(CACHE_SIZE, 0.75f, true); // LRU cache

        for (IPRoute route : routes) {
            insert(route.getPrefix(), route.getDestination());
        }
    }

    private void insert(String prefix, String destination) {
        TrieNode node = root;
        String binaryPrefix = convertToBinary(prefix);

        for (int i = 0; i < binaryPrefix.length();) {
            StringBuilder currentPrefix = new StringBuilder();

            // Group consecutive bits into a single prefix
            while (i < binaryPrefix.length() && (node.children.isEmpty() || node.children.size() == 1)) {
                currentPrefix.append(binaryPrefix.charAt(i));
                i++;
            }

            // Store the prefix in the children map
            String prefixStr = currentPrefix.toString();
            node.children.putIfAbsent(prefixStr, new TrieNode());
            node = node.children.get(prefixStr);
        }

        node.isEndOfPrefix = true;
        node.destination = destination;
    }

    public String longestPrefixMatch(String ip, List<String> sequence) {

        sequence.clear();
        sequence.add(ip);

        // Check cache first
        if (prefixCache.containsKey(ip)) {
            return prefixCache.get(ip);
        }

        TrieNode node = root;
        String binaryIp = convertToBinary(ip);
        String bestMatch = null;

        for (int i = 0; i < binaryIp.length(); ) {
            boolean matched = false;

            // Traverse the trie and find the longest match
            for (String prefix : node.children.keySet()) {
                if (binaryIp.startsWith(prefix, i)) {
                    node = node.children.get(prefix);
                    i += prefix.length();
                    if (node.destination != null) {
                        System.out.println("ESYEUSHDJSD");
                        bestMatch = node.destination;  // Update the best match
                        sequence.add(node.destination);
                    }
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                break;
            }
        }

        // Cache the result
        String result = bestMatch != null ? bestMatch : defaultGateway; // Fall back to default gateway
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

    public TrieNode getRoot() {return root;}
}
