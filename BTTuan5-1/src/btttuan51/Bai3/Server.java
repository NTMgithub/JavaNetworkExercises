/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package btttuan51.Bai3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
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
    
    public static void MainServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        while (true) {
            try {
                System.out.println("Đợi client kết nối...");
                socket = serverSocket.accept();
                System.out.println("Đã kết nối!");

                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                int n = 0;
                BigDecimal piCalc = new BigDecimal(0.0);
                BigDecimal pi = new BigDecimal(3.141592653589793238462643);

                while (true) {
                    //Nhận dữ liệu từ client
                    String receiveData = dataInputStream.readUTF();
                    System.out.println("Client đã gửi: " + receiveData);

                    if (receiveData.equals("bye")) {
                        break;
                    }

                    try {
                        n = Integer.parseInt(receiveData);
                        if (n < 1000000){
                            dataOutputStream.writeUTF("Vui lòng nhập số từ 1,000,000");
                        }else{
                            piCalc = TinhSoPIMonteCarlo(n);
                            dataOutputStream.writeUTF("Số pi đã tính: " + piCalc + "\n" + "Số pi thường dùng: " + pi);
                        }
                        
                    } catch (NumberFormatException e) {
                        dataOutputStream.writeUTF("Vui lòng nhập số!");
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
    
    public static BigDecimal TinhSoPIMonteCarlo(int n){
        int circlePoints = 0; //Số điểm bên trong hình tròn
        int squarePoints = 0; //Số điểm bên trong hình vuông
        BigDecimal piCalc = new BigDecimal(0.0);
        
        for(int i = 0; i<n;i++){
            double x = Server.getRand();
            double y = Server.getRand();
            
            if (x*x + y*y <= 1){ //nếu <= 1 thì tăng số điểm trong hình tròn
                circlePoints++;
            }
            
            squarePoints++; //Tăng số điểm trong hinh vuông (tất cả điểm)
        }
        
        //pi = 4*(số điểm trong hình tròn/số điểm trong hình vuông)
        //=> Tính số pi = (4.0*circlePoints)/squarePoints
        piCalc = BigDecimal.valueOf((4.0 * (double)circlePoints) / (double)squarePoints);
        
        return piCalc;
        
    }
    
    // get random value double
    public static double getRand() {
     Random rd = new Random();
     return rd.nextDouble();
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        int port = 1234;
       
        MainServer(port);
     
        
    }
    
}
