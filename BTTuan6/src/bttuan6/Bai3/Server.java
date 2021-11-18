/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bttuan6.Bai3;

import bttuan6.Bai2.*;
import bttuan6.Bai1.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.NumberFormat;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author ADMIN
 */
public class Server {
    //color 
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    
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

                Document docProduct = null;
                Document docReview = null;
                String productID = "";
                String productName = "";
                int productPrice;
                
                String reviewList = "";

                while (true) {
                    //Nhận dữ liệu từ client
                    String receiveData = dataInputStream.readUTF();
                    System.out.println("Client đã gửi: " + receiveData);

                    if (receiveData.equals("bye")) {
                        break;
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
    
   
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        int port = 1234;
        MainServer(port);
   
     
    }
    
    
    
    
}
