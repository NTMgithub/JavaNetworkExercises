/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bttuan5.Bai5;

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
            
            double ketQua;
            
            while(true){
                //Nhận dữ liệu từ client
                String receiveData = dataInputStream.readUTF();
                System.out.println("Client đã gửi: " + receiveData);
                
                if (receiveData.equals("bye")) break;
                
                ketQua = Server.TinhCanBan(receiveData);
                
                if (ketQua == -1){
                    //Gửi data về client
                    dataOutputStream.writeUTF("Vui lòng nhập đúng định dạng!");
                }else{
                    //Gửi data về client
                    dataOutputStream.writeUTF("Kết quả: " + ketQua);
                }
                
                dataOutputStream.flush();//gửi
                
                
            }
            
            //Đóng các luồng (close theo đúng thứ tự)
            dataOutputStream.close();
            dataInputStream.close();
            socket.close();
            serverSocket.close();
           
        } catch (IOException ex) {
            System.out.println("Lỗi nhập xuất!");
        }
        
    }
    
    public static double TinhCanBan(String chuoi){
        String[] cacPhepTinh = new String[]{"+", "-", "*", "/"};
        double result = 0;

        //Chuẩn hóa chuỗi
        chuoi = chuoi.trim();
        chuoi = chuoi.replaceAll("\\s+", " ");
        
        for (String phepTinh : cacPhepTinh){
            if (chuoi.contains(phepTinh)){ //Nếu tìm thấy char trong cacPhepTinh String[]
                
                String[] parts = chuoi.split("\\" + phepTinh);
                int numberLeft = Integer.parseInt(parts[0]);
                int numberRight = Integer.parseInt(parts[1]);
                
                switch (phepTinh){
                    case "+": 
                        result = numberLeft + numberRight;
                        break;
                    case "-": 
                        result = numberLeft - numberRight;
                        break;
                    case "*": 
                        result = numberLeft * numberRight;
                        break;
                    case "/": 
                        result = numberLeft/numberRight;
                        break;
                    default: return -1;
                }
                return result;
            }
        }
        return -1;
       
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int port = 1234;
        
        Server.ChatServer(port);
    }
    
}
