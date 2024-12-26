package central.objects;

import java.util.List;

public class LinearSearchLPM {
    private List<IPRoute> routes;  // Unordered list of routes
    private String defaultGateway = "0.0.0.0/0";  // Default gateway

    public LinearSearchLPM(List<IPRoute> routes) {
        this.routes = routes;
    }

    // LPM using linear search with an unordered list
    public String longestPrefixMatch(String ip, List<String> traversal) {
        String binaryIp = convertToBinary(ip);
        String longestMatch = null;
        int maxPrefixLength = -1;

        for (IPRoute route : routes) {
            String binaryPrefix = convertToBinary(route.getPrefix());
            traversal.add(route.getPrefix());
            if (binaryIp.startsWith(binaryPrefix)) {
                int prefixLength = binaryPrefix.length();
                // Update longest match if this prefix is longer
                if (prefixLength > maxPrefixLength) {
                    maxPrefixLength = prefixLength;
                    longestMatch = route.getDestination();
                }
            }
        }
        return longestMatch != null ? longestMatch : defaultGateway;
    }

    private boolean isMatchWithRedundantCheck(String binaryIp, String binaryPrefix) {
        // A redundant check to simulate extra work that is not needed
        int minLength = Math.min(binaryIp.length(), binaryPrefix.length());
        for (int i = 0; i < minLength; i++) {
            if (binaryIp.charAt(i) != binaryPrefix.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    private String convertToBinary(String ip) {
        // Convert the IP to binary inefficiently
        if (ip.contains("/")) {
            ip = ip.split("/")[0];  // Remove subnet mask part
        }

        String[] parts = ip.split("\\.");
        StringBuilder binary = new StringBuilder();

        for (String part : parts) {
            // Convert each octet to binary and add leading zeroes inefficiently
            String binaryOctet = String.format("%8s", Integer.toBinaryString(Integer.parseInt(part))).replace(' ', '0');
            binary.append(binaryOctet);
        }
        return binary.toString();
    }


}
