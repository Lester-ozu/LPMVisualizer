package central.objects;

public class IPRoute {
    private String prefix;
    private String destination;

    public IPRoute(String prefix, String destination) {
        this.prefix = prefix;
        this.destination = destination;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return "IPRoute{" +
                "prefix='" + prefix + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
