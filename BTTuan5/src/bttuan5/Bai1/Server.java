/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bttuan5.Bai1;

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
            
            //Scanner sc = new Scanner(System.in);
            String sendLine = "";
            
            while(true){
                
                //Nhận dữ liệu từ client
                String receiveLine = dataInputStream.readUTF();
                System.out.println("Client đã gửi: " + receiveLine);
                
                if (receiveLine.equals("bye")) break;
                
                sendLine = Server.ReverseString(receiveLine);

                //Gửi data về client
                dataOutputStream.writeUTF(sendLine); //gắn string vào luồng dataouput
                dataOutputStream.flush();//gửi data
                
                
            }
            
            //Đóng các luồng (close theo đúng thứ tự)
            dataOutputStream.close();
            dataInputStream.close();
            socket.close();
            
        } catch (IOException ex) {
            System.out.println("Lỗi nhập xuất!");
        }
        
    }
    
    public static String ReverseString(String line){
        StringBuilder str = new StringBuilder(line);
        return str.reverse().toString();
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int port = 1234;
        
        Server.ChatServer(port);
    }
    
}
