
package bttuan5.Bai4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author ADMIN
 */
public class Client {
   
    
    //Bài 1
    public static void ChatClient(String domain, int port){
        try {
            Socket socket = new Socket(domain, port);
            
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            
            Scanner sc = new Scanner(System.in);
            int sendData;
            
            while(true){
                System.out.print("Nhập tin gửi: ");
                sendData = sc.nextInt();
                
                if (sendData < 1 && sendData > 100){
                    System.out.println("Vui lòng nhập trong khoảng 1-100");
                    continue;
                }else{
                    //Gửi data lên server
                    dataOutputStream.write(sendData); 
                    dataOutputStream.flush();//đẩy lên server
                }
                
                //Nhận dữ liệu từ server
                String receiveLine = dataInputStream.readUTF();
                System.out.println("Server trả lời: " + receiveLine); 

                if (receiveLine.contains("Bạn đã đoán đúng!")){
                    break;
                }
                
                
            }
            
            //Đóng các luồng (close theo đúng thứ tự)
            //dataOutputStream.close();
            //dataInputStream.close();
            //socket.close();
            
        } catch (IOException ex) {
            System.out.println("Lỗi nhập xuất!");
        }
        
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String domain = "localhost";
        int port = 1234;
        
        Client.ChatClient(domain, port);
        
        
    }
    
}
