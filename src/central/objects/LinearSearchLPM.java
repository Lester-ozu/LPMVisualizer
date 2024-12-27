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

        traversal.clear();
        traversal.add(ip);

        String binaryIp = convertToBinary(ip);
        String longestMatch = null;
        int maxPrefixLength = -1;

        for (IPRoute route : routes) {
            traversal.add("0="+route.getPrefix());
            String prefix = route.getPrefix();
            String[] prefixParts = prefix.split("/");
            String binaryPrefix = convertToBinary(prefixParts[0]);
            int prefixLength = Integer.parseInt(prefixParts[1]);  // Get the prefix length

            // Truncate the binary prefix to the correct length
            binaryPrefix = binaryPrefix.substring(0, prefixLength);

            // Check for match
            if (binaryIp.startsWith(binaryPrefix)) {
                traversal.set(traversal.size()-1, "1=" + route.getPrefix());
                // Update longest match if this prefix is longer
                if (prefixLength > maxPrefixLength) {
                    maxPrefixLength = prefixLength;
                    longestMatch = route.getDestination();
                }
            }
        }
        return longestMatch != null ? longestMatch : defaultGateway;
    }

    private String convertToBinary(String ip) {
        // Convert the IP to binary inefficiently
        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid IP address format: " + ip);
        }

        StringBuilder binary = new StringBuilder();
        for (String part : parts) {
            int octet = Integer.parseInt(part);
            if (octet < 0 || octet > 255) {
                throw new IllegalArgumentException("Invalid IP address part: " + part);
            }
            // Convert each octet to binary and pad with leading zeroes
            String binaryOctet = String.format("%8s", Integer.toBinaryString(octet)).replace(' ', '0');
            binary.append(binaryOctet);
        }
        return binary.toString();
    }


}
