/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bttuan51.Bai4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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
    
    public static void MainServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        while (true) {
            try {
                System.out.println("Đợi client kết nối...");
                socket = serverSocket.accept();
                System.out.println("Đã kết nối!");

                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                Document doc = null;

                while (true) {
                    //Nhận dữ liệu từ client
                    String receiveData = dataInputStream.readUTF();
                    System.out.println("Client đã gửi: " + receiveData);

                    if (receiveData.equals("bye")) {
                        break;
                    }

                    doc = Server.GetDocumentMaSoThue(receiveData);
                    Elements eHoTen = doc.select("#main > section:nth-child(1) > div > table > tbody > tr:nth-child(3) > td:nth-child(2) > span > a");
                    Elements eQueQuan = doc.select("#main > section:nth-child(1) > div > table > tbody > tr:nth-child(2) > td:nth-child(2) > span");

                    if (!eHoTen.text().equals("")) {
                        dataOutputStream.writeUTF(ANSI_GREEN + "Họ tên: " + ANSI_RESET + eHoTen.text() + "\n"
                                + ANSI_GREEN + "Quê quán: " + ANSI_RESET + eQueQuan.text());
                    } else {
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
    
    public static Document GetDocumentMaSoThue(String soCMND){
        try {
            Document doc = Jsoup.connect("https://masothue.com/Search/").data("q", soCMND).data("type", "auto").get();
            return doc;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        int port = 1234;
        
        MainServer(port);
            
    }
    
}
