
package bttuan3;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Bai1 {

    public static String GetIP(String domainName) throws UnknownHostException{
        InetAddress iNet = InetAddress.getByName(domainName);
        return iNet.getHostAddress();
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String domainName;
        
        do{
            System.out.print("Nhập domain name: ");
            domainName = scanner.nextLine();
            
            try {
                System.out.println("IP address: " + Bai1.GetIP(domainName));
            } catch (UnknownHostException ex) {
                System.out.println("Không tìm thấy IP tương ứng!");
            }
            
        }while (!domainName.equals("exit"));
        
        
    }
    
}
