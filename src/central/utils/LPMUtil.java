package central.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import central.objects.HybridHashTrieLPM;
import central.objects.IPRoute;
import central.objects.LinearSearchLPM;
import central.objects.PureTrieLPM;

public class LPMUtil {
    
    public static List<IPRoute> generateRoutingTable(int totalDestinations, int hashMapCount, int trieCount) {
        List<IPRoute> routes = new ArrayList<>();
        Random random = new Random();

        // Generate HashMap-based destinations
        for (int i = 0; i < hashMapCount; i++) {
            String prefix = "10." + random.nextInt(256) + ".0.0/16"; // Random /16 prefix
            String destination = "192.168." + random.nextInt(256) + "." + random.nextInt(256); // Random destination
            routes.add(new IPRoute(prefix, destination));
        }

        // Generate Trie-based destinations
        for (int i = 0; i < trieCount; i++) {
            String prefix = "192.168." + random.nextInt(256) + ".0/24"; // Random /24 prefix
            String destination = "10." + random.nextInt(256) + ".0." + random.nextInt(256); // Random destination
            routes.add(new IPRoute(prefix, destination));
        }

        return routes;
    }

    public static List<String> generateRandomIPs(int count, List<IPRoute> routes) {
        List<String> ipAddresses = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {

            String ip;

            if(routes != null && !routes.isEmpty()) {
            
                IPRoute route = routes.get(random.nextInt(routes.size()));
                String [] prefixParts = route.getPrefix().split("\\.|/");

                ip = prefixParts[0] + "." + prefixParts[1] + "." + prefixParts[2] + "." + random.nextInt(256);
            }

            else {

                ip = random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256);
            }

            ipAddresses.add(ip);
        }

        return ipAddresses;
    }

    public static StringBuilder measureLinearPerformance(LinearSearchLPM linearRouter, List<String> ipAddresses, List<String> traversal) {
        StringBuilder results = new StringBuilder();

        Runtime runtime = Runtime.getRuntime();

        // Measure time and space for Linear Search
        runtime.gc();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();
        for (String ip : ipAddresses) {
            linearRouter.longestPrefixMatch(ip, traversal);
        }
        long endTime = System.nanoTime();
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        results.append((endTime - startTime) / 1_000_000.0);
        results.append("\n" + (memoryAfter - memoryBefore) / 1024.0);

        return results;
    }

    public static StringBuilder measureTriePerformance(PureTrieLPM trieRouter, List<String> ipAddresses, List<String> traversal) {
        StringBuilder results = new StringBuilder();

        Runtime runtime = Runtime.getRuntime();

        // Measure time and space for Pure Trie
        runtime.gc();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();
        for (String ip : ipAddresses) {
            trieRouter.longestPrefixMatch(ip, traversal);
        }
        long endTime = System.nanoTime();
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        results.append((endTime - startTime) / 1_000_000.0);
        results.append("\n" + ( memoryAfter - memoryBefore) / 1024.0);

        return results;
    }

    public static StringBuilder measureHashTriePerformance(HybridHashTrieLPM hybridRouter, List<String> ipAddresses, HashMap<String, String> bestMatch) {
        StringBuilder results = new StringBuilder();

        Runtime runtime = Runtime.getRuntime();

        // Measure time and space for Hybrid Hash-Trie
        runtime.gc();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();
        for (String ip : ipAddresses) {
            bestMatch.put(ip, hybridRouter.longestPrefixMatch(ip));
        }
        long endTime = System.nanoTime();
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        results.append((endTime - startTime) / 1_000_000.0);
        results.append("\n" + (memoryAfter - memoryBefore) / 1024.0);

        return results;
    }
}
