/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bttuan6.Bai3;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.StringTokenizer;
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

                Document doc = null;
                
                while (true) {
                    //Nhận dữ liệu từ client
                    String receiveData = dataInputStream.readUTF();
                    System.out.println("Client đã gửi: " + receiveData);

                    if (receiveData.equals("bye")) {
                        break;
                    }
                    
                    
                    if(!CheckFormat(receiveData)){
                        dataOutputStream.writeUTF(ANSI_RED+ "Không đúng định dạng!" + ANSI_RESET);
                    }else{
                        String stringURL = URLEncoder.encode(receiveData);

                        doc = Jsoup.connect("https://www.google.com/search?q=" + stringURL).get();

                        Elements stringCongThuc = doc.select("#rso > div.ULSxyf > div > div > div.card-section > div > div > div.BRpYC > div.TIGsTb > div.xwgN1d.XSNERd > div > span");
                        Elements stringKetQua = doc.select("#cwos");

                        dataOutputStream.writeUTF(ANSI_GREEN + stringCongThuc.text() + " " + stringKetQua.text() + ANSI_RESET);
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
    
    public static boolean CheckFormat(String stringInput){
        StringTokenizer strToken = new StringTokenizer(stringInput, "+-*/");
        
        while(strToken.hasMoreTokens()){
            if (!isNumeric(strToken.nextToken())){
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
   
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        int port = 1234;
        MainServer(port);
   
        
    }
    
    
    
    
}
