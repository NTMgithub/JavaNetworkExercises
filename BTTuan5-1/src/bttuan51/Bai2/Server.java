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
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.StringTokenizer;
import org.apache.commons.validator.routines.InetAddressValidator;
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
                Document doc = null;
                String traCuuPublicIP = "";
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
                                
                                traCuuPublicIP = strToken.nextToken();
                                doc = GetInformationAnyIP(traCuuPublicIP);
                                Elements cityName = doc.select("#search-results > div:nth-child(1) > article > div > div.data > table > tbody > tr:nth-child(9) > td:nth-child(2)");
                                Elements countryName = doc.select("#search-results > div:nth-child(1) > article > div > div.data > table > tbody > tr:nth-child(2) > td:nth-child(2)");
                                Elements continentName = doc.select("#search-results > div:nth-child(1) > article > div > div.data > table > tbody > tr:nth-child(1) > td:nth-child(2)");
                                Elements organizationName = doc.select("#search-results > div:nth-child(1) > article > div > div.data > table > tbody > tr:nth-child(16) > td:nth-child(2)");
                                
                                if (cityName.isEmpty()){
                                    dataOutputStream.writeUTF("Không tìm thấy dữ liệu!");
                                }else{
                                    dataOutputStream.writeUTF(ANSI_GREEN + "Tên tổ chức: " + organizationName.text().toUpperCase() + "\n" +
                                                            ANSI_GREEN + "Thành phố: " + cityName.text().toUpperCase() + "\n" +
                                                          ANSI_GREEN + "Quốc gia: " + countryName.text().toUpperCase() + "\n" +
                                                           ANSI_GREEN + "Châu lục: " + continentName.text().toUpperCase() + ANSI_RESET);
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

   
    //Get Public IP
    public static String GetPublicIP(){
        URL connectURL;
        String publicIP = "";
        try {
            connectURL = new URL("http://bot.whatismyipaddress.com/");
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
    
    //Tra cứu IP
    public static Document GetInformationAnyIP(String ipAddress){
        
        try {
            Document doc = Jsoup.connect("https://www.home.neustar/resources/tools/ip-geolocation-lookup-tool").data("ip",ipAddress).post();
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
