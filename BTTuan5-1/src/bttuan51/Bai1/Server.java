/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bttuan51.Bai1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author ADMIN
 */
public class Server {
    //color 
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    
    private static ServerSocket serverSocket = null;
    private static Socket socket = null;
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    
    public static void MainServer(int port, String fileName) throws IOException {
        serverSocket = new ServerSocket(port);

        while (true) {
            try {
                System.out.println("Đợi client kết nối...");
                socket = serverSocket.accept();
                System.out.println("Đã kết nối!");

                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                String ketQua = "";
                
                while (true) {
                    //Nhận dữ liệu từ client
                    String receiveData = dataInputStream.readUTF();
                    System.out.println("Client đã gửi: " + receiveData);

                    if (receiveData.equals("bye")) {
                        break;
                    }
                    
                    ketQua = FindInDictionary(fileName, receiveData);

                    if (!ketQua.equals("notfound")){
                        dataOutputStream.writeUTF(ANSI_GREEN + receiveData + " => " + ketQua + ANSI_RESET);
                    }else{
                        dataOutputStream.writeUTF("Không tìm thấy dữ liệu!");
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

    }
    
    public static String FindInDictionary(String fileName, String searchWord){
        try {
        //tạo luồng nối đến tập tin
        FileInputStream is = new FileInputStream(fileName);
        //Dùng phương tiện Scanner để đọc
        Scanner input = new Scanner(is,"UTF-8");//đọc theo bảng mã utf-8
        //trong khi còn dòng để đọc
        while (input.hasNextLine()) {
            //Đọc 1 dòng
            String line = input.nextLine();

            String[] parts = line.split(";");
            String textLeft = parts[0];
            String textRight = parts[1];
                
            //System.out.println(line + "\n");
                
            if (searchWord.equalsIgnoreCase(textLeft) || searchWord.equalsIgnoreCase(textRight)){
                if (searchWord.equalsIgnoreCase(textLeft)){
                    return textRight;
                    //System.out.println(searchWord + " => " + textRight);
                }

                if (searchWord.equalsIgnoreCase(textRight)){
                    return textLeft;
                    //System.out.println(searchWord + " => " + textLeft);
                }
            }
                //in ra màn hình
                //System.out.println( (i++) +". "+ textLeft );
            }
            //đóng luồng
            is.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "notfound";
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        int port = 1234;
        String fileName = "..\\BTTuan5-1\\src\\bttuan51\\Bai1\\Dictionary.txt";
        
        MainServer(port, fileName);
            
    }
    
}
