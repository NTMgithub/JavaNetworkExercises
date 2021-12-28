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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class SendMessage implements Runnable {
    private DataOutputStream out;
    private Socket socket;
    public SendMessage(Socket s, DataOutputStream o) {
        this.socket = s;
        this.out = o;
    }
    public void run() {
        try {
            while(true) {
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                String data = stdIn.readLine();
                System.out.println("Input from client: " + data);
                out.writeUTF(data);
                out.flush();
                if(data.equals("bye"))
                    break;
            }
            System.out.println("Client closed connection");
            out.close();
            socket.close();
        } catch (IOException e) {}
    }
}

class ReceiveMessage implements Runnable {
    private DataInputStream in;
    private Socket socket;
    public ReceiveMessage(Socket s, DataInputStream i) {
        this.socket = s;
        this.in = i;
    }
    public void run() {
        try {
            while(true) {
                String data = in.readUTF();
                System.out.println("Receive: " + data);
            }
        } catch (IOException e) {}
    }
}

public class Client2 {
    private static String host = "localhost";
    private static int port = 1234;
    private static Socket socket;

    private static DataOutputStream out;
    private static DataInputStream in;

    public static void main(String[] args) throws IOException, InterruptedException {
        socket = new Socket(host, port);
        System.out.println("Client connected");
        System.out.println("Nhập username: ");
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
        ExecutorService executor = Executors.newFixedThreadPool(5);
        SendMessage send = new SendMessage(socket, out);
        ReceiveMessage recv = new ReceiveMessage(socket, in);
        executor.execute(send);
        executor.execute(recv);
    }
}
