/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bttuan2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author ADMIN
 */
public class Bai3 {

    public String TuDien(String filename, String searchWord) {
    try {
        //tạo luồng nối đến tập tin
        FileInputStream is = new FileInputStream(filename);
        //Dùng phương tiện Scanner để đọc
        Scanner input = new Scanner(is,"UTF-8");//đọc theo bảng mã utf-8
        //trong khi còn dòng để đọc
        while (input.hasNextLine()) {
            //Đọc 1 dòng
            String line = input.nextLine();

            String[] parts = line.split(";");
            String textLeft = parts[0];
            String textRight = parts[1];
                
            //System.out.println(line + "\n");
                
            if (searchWord.equalsIgnoreCase(textLeft) || searchWord.equalsIgnoreCase(textRight)){
                if (searchWord.equalsIgnoreCase(textLeft)){
                    return textRight;
                    //System.out.println(searchWord + " => " + textRight);
                }

                if (searchWord.equalsIgnoreCase(textRight)){
                    return textLeft;
                    //System.out.println(searchWord + " => " + textLeft);
                }
            }
                //in ra màn hình
                //System.out.println( (i++) +". "+ textLeft );
            }
            //đóng luồng
            is.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "notfound";
    }
    
    
    
    public static void main(String[] args) {
        Bai3 bai3 = new Bai3();
        Scanner scanner = new Scanner(System.in);
        String searchWord;
        String fileName = "..\\BTTuan2\\src\\bttuan2\\Dictionary.txt";
       
        System.out.print("Nhập từ: ");
        
        searchWord = scanner.nextLine();
        
        if (!bai3.TuDien(fileName, searchWord).equals("notfound")){
            System.out.println(searchWord + " => " + bai3.TuDien(fileName, searchWord));
        }else{
            System.out.println("Không tìm thấy dữ liệu!");
        }
        
        
    }
    
}
