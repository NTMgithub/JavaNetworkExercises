/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bttuan5.Bai3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


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
            
            String sendLine = "";
            
            while(true){
                //Nhận dữ liệu từ client
                int receiveNumber = dataInputStream.read();
                System.out.println("Client đã gửi: " + receiveNumber);
                
                 for (int i = 2; i <= receiveNumber; i++) {
                    while(receiveNumber % i == 0) {
                        sendLine += i + "  ";
                        receiveNumber /= i;
                    }
                }
                
                //Gửi data về client
                dataOutputStream.writeUTF(sendLine);
                dataOutputStream.flush();//gửi
                
                //reset sendline
                sendLine = "";
                
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
        int port = 1234;
        
        Server.ChatServer(port);
    }
    
}
