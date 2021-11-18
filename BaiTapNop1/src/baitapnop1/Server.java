/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baitapnop1;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.StringTokenizer;

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
                
                while (true) {
                    //Nhận dữ liệu từ client
                    String receiveData = dataInputStream.readUTF();
                    System.out.println("Client đã gửi: " + receiveData);

                    if (receiveData.equals("bye")) {
                        break;
                    }
                    
                    if (!receiveData.isEmpty()) {
                        //Tra cứu
                        String ketQua = FindInDictionary(fileName, receiveData);

                        StringTokenizer strToken = new StringTokenizer(receiveData, ";");
                        String firstToken = strToken.nextToken();
                        
                        if (firstToken.equals("ADD")){
                            if (CheckFormatADD(receiveData)) {
                                dataOutputStream.writeUTF(AddNewWord(fileName, receiveData));
                            } else {
                                dataOutputStream.writeUTF("Cú pháp ADD không đúng!\nCú pháp đúng: ADD;x;y");
                            }
                        }else{
                            if (firstToken.equals("DEL")){
                                if (CheckFormatDEL(receiveData)) {
                                    dataOutputStream.writeUTF(DelWord(fileName, receiveData));
                                } else {
                                    dataOutputStream.writeUTF("Cú pháp DEL không đúng!\nCú pháp đúng: DEL;x");
                                }
                            }else{
                                if (!ketQua.equals("notfound")) {
                                    dataOutputStream.writeUTF(ANSI_GREEN + receiveData + " => " + ketQua + ANSI_RESET);
                                } else {
                                    dataOutputStream.writeUTF("Không tìm thấy dữ liệu!");
                                }
                            }
                        }
                     
                        
                    }else{
                       dataOutputStream.writeUTF("Không được trống!");
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
    
    public static boolean CheckFormatADD(String stringInput){
        StringTokenizer strToken = new StringTokenizer(stringInput,";");
        
        if (strToken.countTokens() != 3 ){
            return false;
        }else{
            if (!strToken.nextToken().equals("ADD")) {
                return false;
            } else {
                if (!(strToken.nextToken() instanceof String)) { //x: phải là String
                    return false;
                }
            }
        }
        return true;
    }
    
    public static String AddNewWord(String fileName, String receiveData) throws FileNotFoundException, IOException{
        //Thêm mới
        FileOutputStream fileOutput = new FileOutputStream(fileName, true); //thuộc tính true giúp ghi tiếp vào file
        BufferedWriter fileDataOutput = new BufferedWriter(new OutputStreamWriter(fileOutput, StandardCharsets.UTF_8));

        StringTokenizer strToken = new StringTokenizer(receiveData, ";");
        strToken.nextToken(); //String "ADD"

        String englishAdd = strToken.nextToken();
        String vietnameseAdd = strToken.nextToken();

        if (!FindInDictionary(fileName, englishAdd).equals("notfound")) {
            return "Từ này đã có trong từ điển!";
        } else {

            fileDataOutput.write(englishAdd + ";" + vietnameseAdd + "\n"); //viết xuống file

            fileDataOutput.close();
            fileOutput.close();

            return ANSI_GREEN + "Thêm thành công!" + ANSI_RESET;
        }
        
    }
    
    public static boolean CheckFormatDEL(String stringInput){
        StringTokenizer strToken = new StringTokenizer(stringInput,";");
        
        if (strToken.countTokens() != 2){
            return false;
        }else{
            if (!strToken.nextToken().equals("DEL")) {
                return false;
            } else {
                if (!(strToken.nextToken() instanceof String)) { //x: phải là String
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public static String DelWord(String fileName, String receiveData) throws FileNotFoundException, IOException{
        //Xóa từ trong từ điển
        StringTokenizer strToken = new StringTokenizer(receiveData, ";");
        strToken.nextToken(); //String "DEL"
        String removeWord = strToken.nextToken();

        if (!FindInDictionary(fileName, removeWord).equals("notfound")) {
            StringBuffer sb = new StringBuffer("");

            FileInputStream is = new FileInputStream(fileName);
            //Dùng phương tiện Scanner để đọc
            Scanner input = new Scanner(is, "UTF-8");//đọc theo bảng mã utf-8
            //trong khi còn dòng để đọc
            while (input.hasNextLine()) {
                String line = input.nextLine();

                StringTokenizer strTokenRemove = new StringTokenizer(line, ";");
                String word = strTokenRemove.nextToken();

                if (!word.equals(removeWord)) {
                    sb.append(line + "\n"); //Add toàn bộ vào StringBuffer temp trừ line chứa word
                }
            }

            File f = new File(fileName);
            f.delete(); //xóa file

            //Viết nội dung mới (đã xóa line chứa từ cần xóa) vào file mới
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8));
            out.write(sb.toString());

            is.close();
            out.close();

            return ANSI_GREEN + "Xóa thành công!" + ANSI_RESET;

        } else {
            return "Từ này không có trong từ điển!";
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

                StringTokenizer strToken = new StringTokenizer(line, ";");
                String textLeft = strToken.nextToken();
                String textRight = strToken.nextToken();

                if (searchWord.equalsIgnoreCase(textLeft) || searchWord.equalsIgnoreCase(textRight)){
                    if (searchWord.equalsIgnoreCase(textLeft)){
                        return textRight;
                    }

                    if (searchWord.equalsIgnoreCase(textRight)){
                        return textLeft;
                    }
                }
            }

            //đóng luồng
            is.close();
            input.close();
        } catch (IOException e) {
            return "notfound";
        }
        return "notfound";
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        int port = 1234;
        String fileName = "..\\BaiTapNop1\\src\\baitapnop1\\Dictionary.txt";
        
        MainServer(port, fileName);
      
        
       
    }
    
}
