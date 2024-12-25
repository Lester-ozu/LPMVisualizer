package central;

import central.objects.HybridHashTrieLPM;
import central.objects.IPRoute;
import central.objects.LinearSearchLPM;
import central.objects.PureTrieLPM;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    // Generate a routing table with fixed destinations
    private static List<IPRoute> generateRoutingTable(int totalDestinations, int hashMapCount, int trieCount) {
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

    // Generate random IP addresses for testing
    private static List<String> generateRandomIPs(int count, List<IPRoute> routes) {
        List<String> ipAddresses = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            int octet1 = random.nextInt(256);
            int octet2 = random.nextInt(256);
            int octet3 = random.nextInt(256);
            int octet4 = random.nextInt(256);

            String ip = octet1 + "." + octet2 + "." + octet3 + "." + octet4;

            // Ensure match if required
            if (routes != null) {
                for (IPRoute route : routes) {
                    if (ip.startsWith(route.getPrefix().split("/")[0])) {
                        ipAddresses.add(ip);
                        break;
                    }
                }
            }

            ipAddresses.add(ip);
        }

        return ipAddresses;
    }
    // Modified testCase method to test all three approaches independently
    private static void testCase(int totalDestinations, int hashMapCount, int trieCount, boolean match) {
        // Generate the routing table
        List<IPRoute> routes = generateRoutingTable(totalDestinations, hashMapCount, trieCount);

        // Initialize the HybridHashTrieLPM with the routes
        HybridHashTrieLPM hybridRouter = new HybridHashTrieLPM(routes);
        LinearSearchLPM linearRouter = new LinearSearchLPM(routes);
        PureTrieLPM trieRouter = new PureTrieLPM(routes);

        // Generate random IP packets based on the match parameter
        List<String> ipAddresses = generateRandomIPs(50, match ? routes : null);

        // Display the first 10 IPs and then the dots
        System.out.println("Generated IP Packets [" + ipAddresses.size() + "]: " + String.join(", ", ipAddresses.subList(0, 10)) + " ......");

        // Measure the time and space complexity for each approach
        measureLpmPerformance(linearRouter, trieRouter, hybridRouter, ipAddresses);
    }
    public static void main(String[] args) {
        // Test Case 1: 50 destinations, 30 HashMap, 20 Trie, No match
        System.out.println("50 Destinations | No Match Found");
        testCase(50, 30, 20, false);

        // Test Case 2: 100 destinations, 50 HashMap, 50 Trie, No match
        System.out.println("\n100 Destinations | No Match Found");
        testCase(100, 50, 50, false);

        // Test Case 3: 50 destinations, 30 HashMap, 20 Trie, Match
        System.out.println("\n30 Frequent Destinations | Match Found");
        testCase(50, 30, 20, true);

        // Test Case 4: 100 destinations, 50 HashMap, 50 Trie, Match
        System.out.println("\n50 Frequent Destinations | Match Found");
        testCase(100, 50, 50, true);

        // Test Case 5: 100 destinations, 75 HashMap, 25 Trie, Match
        System.out.println("\n75 Frequent Destinations | Match Found");
        testCase(100, 75, 25, true);

        // Test Case 6: 500 destinations, 300 HashMap, 200 Trie, Match
        System.out.println("\n500 Destinations | Match Found");
        testCase(500, 300, 200, true);

        // Test Case 7: 1000 destinations, 750 HashMap, 250 Trie, Match
        System.out.println("\n1000 Destinations | Match Found");
        testCase(1000, 750, 250, true);
        // Test Case 8: 10000 destinations, 7500 HashMap, 2500 Trie, Match
        System.out.println("\n10000 Destinations | Match Found");
        testCase(10000, 7500, 2500, true);

        // Test Case 9: 100000 destinations, 75000 HashMap, 25000 Trie, Match
        System.out.println("\n100000 Destinations | Match Found");
        testCase(100000, 75000, 25000, true);

        // Test Case 10: 1000000 destinations, 750000 HashMap, 250000 Trie, Match
        System.out.println("\n1000000 Destinations | Match Found");
        testCase(1000000, 750000, 250000, true);
    }

    // Method to measure and display LPM time and space complexity for all approaches
    private static void measureLpmPerformance(LinearSearchLPM linearRouter, PureTrieLPM trieRouter, HybridHashTrieLPM hybridRouter, List<String> ipAddresses) {
        Runtime runtime = Runtime.getRuntime();

        // Measure time and space for Linear Search
        runtime.gc();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();
        for (String ip : ipAddresses) {
            linearRouter.longestPrefixMatch(ip);
        }
        long endTime = System.nanoTime();
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Linear Search Time taken: " + (endTime - startTime) / 1_000_000.0 + " ms");
        System.out.println("Linear Search Memory usage: " + (memoryAfter - memoryBefore) / 1024.0 + " KB");

        // Measure time and space for Pure Trie
        runtime.gc();
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.nanoTime();
        for (String ip : ipAddresses) {
            trieRouter.longestPrefixMatch(ip);
        }
        endTime = System.nanoTime();
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Pure Trie Time taken: " + (endTime - startTime) / 1_000_000.0 + " ms");
        System.out.println("Pure Trie Memory usage: " + (memoryAfter - memoryBefore) / 1024.0 + " KB");

        // Measure time and space for Hybrid Hash-Trie
        runtime.gc();
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.nanoTime();
        for (String ip : ipAddresses) {
            hybridRouter.longestPrefixMatch(ip);
        }
        endTime = System.nanoTime();
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Hybrid Hash-Trie Time Taken: " + (endTime - startTime) / 1_000_000.0 + " ms");
        System.out.println("Hybrid Hash-Trie Memory usage: " + (memoryAfter - memoryBefore) / 1024.0 + " KB");
    }
}
