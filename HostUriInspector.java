package Lab4.network;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class HostUriInspector {

    public static void main(String[] args) {

        // Phải có đúng 2 tham số:
        // args[0] = hostname
        // args[1] = URI

        if (args.length != 2) {

            System.out.println(
                    "Usage: java Lab4.network.HostUriInspector <hostname> <uri>"
            );

            return;
        }

        String hostname = args[0];
        String uriText = args[1];

        // ========================================
        // PHẦN 1: PHÂN GIẢI HOSTNAME
        // ========================================

        System.out.println("===== HOSTNAME =====");

        try {

            InetAddress[] addresses =
                    InetAddress.getAllByName(hostname);

            System.out.println(
                    "Host: " + hostname
            );

            for (InetAddress address : addresses) {

                System.out.println(
                        "IP: "
                                + address.getHostAddress()
                );

                // Xác định IPv4 / IPv6
                if (address instanceof Inet4Address) {

                    System.out.println(
                            "Type: IPv4"
                    );

                } else if (address instanceof Inet6Address) {

                    System.out.println(
                            "Type: IPv6"
                    );
                }

                System.out.println(
                        "Loopback: "
                                + address.isLoopbackAddress()
                );

                System.out.println(
                        "Site local: "
                                + address.isSiteLocalAddress()
                );

                System.out.println(
                        "Canonical: "
                                + address.getCanonicalHostName()
                );

                System.out.println();
            }

        } catch (UnknownHostException e) {

            System.out.println(
                    "Không phân giải được host: "
                            + hostname
            );
        }

        // ========================================
        // PHẦN 2: PHÂN TÍCH URI
        // ========================================

        System.out.println("===== URI =====");

        try {

            URI uri = new URI(uriText);

            System.out.println(
                    "URI: " + uri
            );

            System.out.println(
                    "Scheme: " + uri.getScheme()
            );

            System.out.println(
                    "Host: " + uri.getHost()
            );

            System.out.println(
                    "Port: " + uri.getPort()
            );

            System.out.println(
                    "Path: " + uri.getPath()
            );

            System.out.println(
                    "Query: " + uri.getQuery()
            );

            System.out.println(
                    "Fragment: " + uri.getFragment()
            );

        } catch (URISyntaxException e) {

            System.out.println(
                    "URI không hợp lệ: "
                            + uriText
            );
        }
    }
}