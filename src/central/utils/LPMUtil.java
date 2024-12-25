package central.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import central.objects.IPRoute;

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
}
