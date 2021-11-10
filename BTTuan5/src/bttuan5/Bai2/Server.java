/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bttuan5.Bai2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 *
 * @author ADMIN
 */
public class Server {
    
    //Bài 1
    public static void ChatServer(int port){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            
            System.out.println("Đợi client kết nối...");
            Socket socket = serverSocket.accept();
            System.out.println("Đã kết nối!");
            
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            
            while(true){
                //Nhận dữ liệu từ client
                int receiveNumber = dataInputStream.read();
                System.out.println("Client đã gửi: " + receiveNumber);
                
                if (Server.CheckPerfectNumber(receiveNumber)){
                     //Gửi data về client
                    dataOutputStream.writeUTF(receiveNumber + " là số hoàn hảo!"); 
                    dataOutputStream.flush();//gửi data
                }else{
                    while(true){
                        receiveNumber++;
                        if (Server.CheckPerfectNumber(receiveNumber)){
                            dataOutputStream.writeUTF("Số hoàn hảo lớn hơn và gần là " + receiveNumber ); 
                            dataOutputStream.flush();//gửi data
                            break;
                        }
                    }
                    
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
    
    public static boolean CheckPerfectNumber(int number){
        long sum = 0;
        int i = 1;
        while (i <= number/2){
            if (number%i == 0){ //ước số của n
                sum += i;
            }
            i++;
        }

        if (sum == number){
            return true;
        }else{
            return false;
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int port = 1234;
        
        Server.ChatServer(port);
    }
    
}
