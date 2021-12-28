/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appchatvoinguoila;

/**
 *
 * @author ADMIN
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Worker2 implements Runnable {
    private String myName;
    private Socket socket;
    DataInputStream in;
    DataOutputStream out;
    private String fileName = "..\\AppChatVoiNguoiLa\\src\\appchatvoinguoila\\users.txt";
    private String dataAdd = "";
   
    public Worker2(Socket s, String name) throws IOException {
        this.socket = s;
        this.myName = name;
        this.in = new DataInputStream(s.getInputStream());
        this.out = new DataOutputStream(s.getOutputStream());
    }
    public void run() {
        System.out.println("Client " + socket.toString() + " accepted");
        try {
            String input = "";
            String requestText = "";
            String contentText = "";
            while(true) {
                input = in.readUTF();
                System.out.println("Server received: " + input + " from " + socket.toString() + " # Client " + myName);
                if(input.equals("bye"))
                    break;
                
                StringTokenizer strToken = new StringTokenizer(input, ";");
                requestText = strToken.nextToken();
                contentText = strToken.nextToken();
                
                if (requestText.equals("createAccount")){
                    for(Worker2 worker : Server2.workers) {
                        if(myName.equals(worker.myName)) {
                            dataAdd = contentText + ";" + myName + ";0";
                            if (AddNewUser(fileName, dataAdd).equals("found")){
                                worker.out.writeUTF("Username đã tồn tại;"+ " ");
                                worker.out.flush();
                                break;
                            }else{
                                worker.out.writeUTF("Đã thêm username!;"+ contentText);
                                worker.out.flush();
                                break;
                            }
                        }
                    }
                }
                
                System.out.println(input);
                System.out.println(requestText);
                String userNameOther = "";
                
                if (requestText.equals("requestChat")){
                    //tìm username của thread đang rảnh
                    for(Worker2 worker2 : Server2.workers) {
                        if (!myName.equals(worker2.myName)) {
                            System.out.println(FindUsername(fileName, contentText));
                            System.out.println(FindStateConnect(fileName, contentText));
                            userNameOther = FindUsernameViaSTT(fileName, worker2.myName);
                        
                        }
                    }
                    
                    //tìm đúng thread đang yêu cầu để gửi tin nhắn tới
                    for(Worker2 worker2 : Server2.workers) {
                        if (myName.equals(worker2.myName)) {
                            if (userNameOther.equals("")){
                                worker2.out.writeUTF("responseRequestChat;notfound");
                                worker2.out.flush();
                                break;
                            }else{
                                if (FindUsername(fileName, userNameOther).equals("found") && FindStateConnect(fileName, userNameOther).equals("0")) {
                                    worker2.out.writeUTF("responseRequestChat;" + userNameOther);
                                    worker2.out.flush();
                                    break;
                                }
                            }
                            
                        }
                    }
                    
                }
                
                
                
            }
            
            System.out.println("Closed socket for client " + myName + " " + socket.toString());
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    
    public static String AddNewUser(String fileName, String receiveData) throws FileNotFoundException, IOException{
        //Thêm mới
        FileOutputStream fileOutput = new FileOutputStream(fileName, true); //thuộc tính true giúp ghi tiếp vào file
        BufferedWriter fileDataOutput = new BufferedWriter(new OutputStreamWriter(fileOutput, StandardCharsets.UTF_8));

        StringTokenizer strToken = new StringTokenizer(receiveData, ";");
        String usernameAdd = strToken.nextToken();
        String thuTuThreadAdd = strToken.nextToken();
        String stateConnectAdd = strToken.nextToken();

        if (!FindUsername(fileName, usernameAdd).equals("notfound")) {
            return "found";
        } else {

            fileDataOutput.write(usernameAdd + ";" + thuTuThreadAdd + ";" + stateConnectAdd+ "\n"); //viết xuống file

            fileDataOutput.close();
            fileOutput.close();

            return "Thêm thành công!";
        }
        
    }
    
    
    public static void replaceLines(String fileName, String replaceLine) {
        try {
            // input the (modified) file content to the StringBuffer "input"
            BufferedReader file = new BufferedReader(new FileReader(fileName));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = file.readLine()) != null) {
                line = replaceLine; // replace the line here
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            file.close();

            // write the new string with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream(fileName);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();

        } catch (Exception e) {
            System.out.println("Problem reading file.");
        }
    }
    
    public static String FindUsername(String fileName, String searchWord){
        try {
            //tạo luồng nối đến tập tin
            FileInputStream is = new FileInputStream(fileName);
            //Dùng phương tiện Scanner để đọc
            Scanner input = new Scanner(is,"UTF-8");//đọc theo bảng mã utf-8
            //trong khi còn dòng để đọc
            
            while (input.hasNextLine()) {
                //Đọc 1 dòng
                String line = input.nextLine();

                if (line.isEmpty()){
                    return "notfound";
                }
                StringTokenizer strToken = new StringTokenizer(line, ";");
                String textLeft = strToken.nextToken();

                if (searchWord.equalsIgnoreCase(textLeft)){
                    return "found";
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
    
    public static String FindUsernameViaSTT(String fileName, String sttThread){
        try {
            //tạo luồng nối đến tập tin
            FileInputStream is = new FileInputStream(fileName);
            //Dùng phương tiện Scanner để đọc
            Scanner input = new Scanner(is,"UTF-8");//đọc theo bảng mã utf-8
            //trong khi còn dòng để đọc
            
            while (input.hasNextLine()) {
                //Đọc 1 dòng
                String line = input.nextLine();

                if (line.isEmpty()){
                    return "notfound";
                }
                StringTokenizer strToken = new StringTokenizer(line, ";");
                String userName = strToken.nextToken();
                String threadSTT = strToken.nextToken();

                if (sttThread.equalsIgnoreCase(threadSTT)){
                    return userName;
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
    
    public static String FindStateConnect(String fileName, String userName){
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
                String userNameFile = strToken.nextToken();
                strToken.nextToken();
                String stateConnect = strToken.nextToken();

                if (userName.equalsIgnoreCase(userNameFile)){
                    return stateConnect;
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
    
     
    
}