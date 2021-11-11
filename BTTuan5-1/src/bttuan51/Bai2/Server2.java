/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bttuan51.Bai2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.json.JSONObject;


/**
 *
 * @author ADMIN
 */
public class Server2 {
    //color 
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";
    
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

                String privateIP = "";
                String publicIP = "";
                String traCuuPublicIP = "";
                JSONObject obj = null;
                //Socket socket2 = new Socket();
    
                while (true) {
                    //Nhận dữ liệu từ client
                    String receiveData = dataInputStream.readUTF();
                    System.out.println("Client đã gửi: " + receiveData);

                    
                    if (receiveData.isEmpty()){
                        dataOutputStream.writeUTF("Vui lòng nhập lệnh!");
                    }else{
                        if (receiveData.equals("bye")) {
                            break;
                        }
                        
                        if (receiveData.equals("hello") || CheckFormatTraCuuIP(receiveData)){
                            //Get thông tin IP server
                            if (receiveData.equals("hello")){
                                //socket2.connect(new InetSocketAddress("google.com", 80));
                                privateIP = InetAddress.getLocalHost().getHostAddress();
                                publicIP = GetPublicIP();
                                dataOutputStream.writeUTF(ANSI_GREEN + "Private IP: " + privateIP + "\n" + "Public IP: " + publicIP + ANSI_RESET);
                            }
                            
                            //Tra cứu thông tin public IP
                            if (CheckFormatTraCuuIP(receiveData)){
                                StringTokenizer strToken = new StringTokenizer(receiveData);
                                strToken.nextToken();
                                
                                traCuuPublicIP = strToken.nextToken(); //Ip cần tra cứu
                                
                                obj = GetInformationAnyIP(traCuuPublicIP);//get json
                                
                                if (obj.getString("status").equals("success")){
                                    dataOutputStream.writeUTF(ANSI_GREEN + "Tên tổ chức: " + obj.getString("org") + "\n" +
                                                            ANSI_GREEN + "Thành phố: " + obj.getString("city") + "\n" +
                                                          ANSI_GREEN + "Quốc gia: " + obj.getString("country") + "\n" +
                                                           ANSI_GREEN + "Châu lục: " + obj.getString("continent") + ANSI_RESET);
                                    
                                }else{
                                    dataOutputStream.writeUTF("Không tìm thấy dữ liệu!");
                                }
                            }
                            
                        }else{
                            dataOutputStream.writeUTF(ANSI_CYAN + "Không đúng định dạng!" + "\n" + 
                                                     ANSI_CYAN +  "Hướng dẫn:" + "\n" +
                                                     ANSI_CYAN + "hello: hiển thị private IP, public IP của Server" + "\n" +
                                                     ANSI_CYAN +  "req x: hiển thị thông tin thành phố, quốc gia, châu lục."+ "\n" + "Với x là địa chỉ IPv4" + ANSI_RESET);
                        }
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
   

    //Get Public IP
    public static String GetPublicIP(){
        URL connectURL;
        String publicIP = "";
        try {
            connectURL = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(connectURL.openStream()));
            publicIP = in.readLine();
            in.close();
            return publicIP;
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
   }
    
    //Kiểm tra format: req ip
    public static boolean CheckFormatTraCuuIP(String receiveData){
        StringTokenizer strToken = new StringTokenizer(receiveData);
        InetAddressValidator validator = InetAddressValidator.getInstance(); //Lớp dùng để check IP valid
        String ipReceive = "";
        
        if (strToken.countTokens() != 2){
             return false;
        }else{
            if (strToken.nextToken().equals("req")){
                ipReceive = strToken.nextToken();
                if (validator.isValidInet4Address(ipReceive)) return true;
                
            }else return false;
            
        }
        return false;
        
    }
    
    //Tra cứu IP tra ve json
    public static JSONObject GetInformationAnyIP(String ipAddress){
        URL connectURL;
        try {
            connectURL = new URL("http://ip-api.com/json/" + ipAddress + "?fields=status,continent,country,city,org");
            BufferedReader in = new BufferedReader(new InputStreamReader(connectURL.openStream()));
            JSONObject obj = new JSONObject(readAll(in));
            return obj;
        }catch (MalformedURLException ex) {
            System.out.println(ex);
        }catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }
    
    //Đọc all char gán vào String
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
          sb.append((char) cp);
        }
        return sb.toString();
    }
   
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        int port = 1234;
       
        MainServer(port);
       
           
    }
    
}
