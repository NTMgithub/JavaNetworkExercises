/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bttuan6.Bai1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
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
                Document doc2 = null;
                Response response = null;
                Document getPage = null;
                String hoTen = "";
                String noiSinh = "";
                String nganhHoc = "";
                String khoaDaoTao = "";
                String diemHocKy = "";

                while (true) {
                    //Nhận dữ liệu từ client
                    String receiveData = dataInputStream.readUTF();
                    System.out.println("Client đã gửi: " + receiveData);

                    if (receiveData.equals("bye")) {
                        break;
                    }
                    
                    
                    String url = "http://thongtindaotao.sgu.edu.vn/default.aspx?page=nhapmasv&flag=XemDiemThi";
                    String url2 = "http://thongtindaotao.sgu.edu.vn/Default.aspx?page=xemdiemthi&id=" + receiveData;
                    //get thông tin request data trước
                    getPage = Jsoup.connect(url).get();
                    //get cookies
                    response = Jsoup.connect(url).method(Connection.Method.GET).execute();

                    doc = Jsoup.connect(url)
                            .data("__EVENTTARGET", "")
                            .data("__EVENTARGUMENT", "")
                            .data("__VIEWSTATE", getPage.getElementById("__VIEWSTATE").val())
                            .data("ctl00$ContentPlaceHolder1$ctl00$txtMaSV", receiveData)
                            .data("ctl00$ContentPlaceHolder1$ctl00$btnOK", "OK")
                            .cookies(response.cookies())
                            .method(Connection.Method.POST)
                            .post();
                    
                    try {
                        hoTen = doc.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblTenSinhVien").text();
                        noiSinh = doc.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblNoiSinh").text();
                        nganhHoc = doc.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lbNganh").text();
                        khoaDaoTao = doc.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblKhoaHoc").text();
                        
                        //Truyền thông tin request data của trang doc vào để lấy điểm học kỳ bất kỳ
                        doc2 = Jsoup.connect(url2)
                                .data("__EVENTTARGET", "")
                                .data("__EVENTARGUMENT", "")
                                .data("__VIEWSTATE", doc.getElementById("__VIEWSTATE").val())
                                .data("__VIEWSTATEGENERATOR", doc.getElementById("__VIEWSTATEGENERATOR").val())
                                .data("ctl00$ContentPlaceHolder1$ctl00$txtChonHK", "20192") //20 -> 2020; 19 -> 2019 ; 2 -> học kỳ 2
                                .data("ctl00$ContentPlaceHolder1$ctl00$btnChonHK", "Xem")
                                .cookies(response.cookies())
                                .post();

                        //doc2 = res.parse();
                        Elements rowDiem = doc2.getElementsByClass("row-diem");

                        for (Element item : rowDiem) {
                            diemHocKy += item.select("td").get(1).text()
                                    + " - " + item.select("td").get(2).text()
                                    + " - " + item.select("td").get(9).text() + "\n";
                        }

                        dataOutputStream.writeUTF(ANSI_GREEN + "Họ tên: " + hoTen + "\n" +
                                                  ANSI_GREEN + "Nơi sinh: " + noiSinh + "\n" +
                                                  ANSI_GREEN + "Ngành: " + nganhHoc + "\n" +
                                                  ANSI_GREEN + "Khóa: " + khoaDaoTao + "\n" + ANSI_RESET +
                                                  "Điểm chi tiết: Học kỳ 2 năm 2019 - 2020\n" + diemHocKy);
                        // System.out.println(doc2.toString());
                    } catch (NullPointerException e) {
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
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        int port = 1234;
        MainServer(port);
        
                       
        
    }
    
    
    
    
}
