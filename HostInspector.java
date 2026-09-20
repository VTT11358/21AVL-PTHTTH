package Lab4.network;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostInspector {
   public HostInspector() {
   }

   public static void main(String[] var0) {
      if (var0.length != 1) {
         System.out.println("Usage: java Lab4.network.HostInspector <hostname>");
      } else {
         try {
            InetAddress[] var1 = InetAddress.getAllByName(var0[0]);
            System.out.println("Host: " + var0[0]);

            for(InetAddress var5 : var1) {
               System.out.println("- IP: " + var5.getHostAddress());
               System.out.println("  Canonical: " + var5.getCanonicalHostName());
               System.out.println("  Loopback: " + var5.isLoopbackAddress());
               System.out.println("  Site local: " + var5.isSiteLocalAddress());
               if (var5 instanceof Inet4Address) {
                  System.out.println("  Type: IPv4");
               } else if (var5 instanceof Inet6Address) {
                  System.out.println("  Type: IPv6");
               }

               System.out.println();
            }
         } catch (UnknownHostException var6) {
            System.err.println("Không phân giải được host: " + var0[0]);
         }

      }
   }
}