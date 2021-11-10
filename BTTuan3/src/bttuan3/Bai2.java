
package bttuan3;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Bai2 {

    public static void main(String[] args) throws FileNotFoundException {
        Bai1 bai1 = new Bai1();
        String fileName = "D:\\Study\\HK1_Nam4\\LapTrinhMang\\BTTuan3\\BTTuan3\\src\\bttuan3\\DomainNameList.txt";
        
        FileInputStream inputFile = new FileInputStream(fileName);
        Scanner scanner = new Scanner(inputFile, "UTF-8");
        
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            
            try {
                System.out.println("Domain name " + line + " has IP " + bai1.GetIP(line));
            } catch (UnknownHostException ex) {
                System.out.println("Domain name " + line + " is not valid");
            }
        }
        
    }
    
}
