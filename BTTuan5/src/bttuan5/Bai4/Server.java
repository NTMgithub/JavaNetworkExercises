/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bttuan5.Bai4;

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
            
            int countSoLanDoan = 1;
            long startTime = System.nanoTime();
            int randomNumber = (int)Math.floor(Math.random()*(100)+1); 
            System.out.println("Số đã tạo: " + randomNumber);
            
            while(true){
                //Nhận dữ liệu từ client
                int receiveData = dataInputStream.read();
                System.out.println("Client đã gửi: " + receiveData);
                
                if (receiveData == randomNumber){
                    long thoiGianDoan = (System.nanoTime() - startTime)/1000000000;
                    //Gửi data về client
                    dataOutputStream.writeUTF("Bạn đã đoán đúng!\n" + 
                                            "Số lần đoán: " + countSoLanDoan + "\n" +
                                             "Thời gian đoán: " + thoiGianDoan + " giây");
                    break;
                }
                
                if (randomNumber < receiveData ){
                    //Gửi data về client
                    dataOutputStream.writeUTF("Kết quả NHỎ HƠN số bạn đoán!");
                    countSoLanDoan++;
                }else{
                    //Gửi data về client
                    dataOutputStream.writeUTF("Kết quả LỚN HƠN số bạn đoán!");
                    countSoLanDoan++;
                }
                
                dataOutputStream.flush();//gửi
                
                
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
