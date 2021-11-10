
package bttuan3;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Bai3 {

    public static boolean CheckConnectIP(String ipAddress) throws UnknownHostException, IOException{
        InetAddress iNet = InetAddress.getByName(ipAddress);
        
        if (iNet.isReachable(5000)){ //check reachable, timeout 5000
            return true;
        }else{
            return false;
        }
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String fileName = "D:\\Study\\HK1_Nam4\\LapTrinhMang\\BTTuan3\\BTTuan3\\src\\bttuan3\\IpAddressList.txt";
        
        FileInputStream inputFile = new FileInputStream(fileName);
        
        Scanner scanner = new Scanner(inputFile, "UTF-8");
        
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            
            if (Bai3.CheckConnectIP(line)){
                System.out.println("IP " + line + " is reachable" );
            }else{
                System.out.println("IP " + line + " is NOT reachable" );
            }
            
        }
        
    }
    
}
