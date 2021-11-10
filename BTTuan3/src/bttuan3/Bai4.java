
package bttuan3;


import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class Bai4 {

  
    public static void main(String[] args) throws SocketException, UnknownHostException, IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
        String ip = socket.getLocalAddress().getHostAddress();
        
        String[] strIP = ip.split("\\.");
          
        String rangeIP = strIP[0] + "." + strIP[1] + "." +strIP[2] + ".";
        
        for (int i=101; i <= 254 ; i++){
            String IPcheck = rangeIP + i;
            InetAddress iNet = InetAddress.getByName(IPcheck);
            
            if (iNet.isReachable(2000)){
                System.out.println("Checking IP " + IPcheck + " ==> " + IPcheck + " is online");
            }else{
                System.out.println("Checking IP " + IPcheck);
            }
            
        }
        
        
    }
    
}
