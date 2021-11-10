package bttuan1;

import java.util.Scanner;


public class BTTuan1 {

    //Bài 1
    public int XuatTong(int n){
        int tong = 0;
        
        if (n > 0){
            for (int dem = 1; dem <= n; dem++){
                tong = tong + dem;
            }
            return tong;
        }else{
            return 0;
        }
    }
    
    //Bài 2
    public int TongUocSo(int n){
        int tong = 0;
        
        if (n >= 3){
            for (int i = 1; i < n; i++){
                if (n%i == 0){
                    tong = tong + i;
                }
            }
            return tong;
        }else{
            return 0;
        }
        
    }
    
    //Bài 3
    public int KiemTraNguyenTo(int number){
        if (number < 2) return 0;
        for (int i = 2; i <= Math.sqrt(number);i++){
            if (number % i == 0){
                return 0;
            }
        }
        return 1;
    }
    public int TinhTongNguyenTo(int n){
        int tong = 0;
        
        for (int i = 2; i < n; i++){
            if (KiemTraNguyenTo(i) == 1){
                tong = tong + i;
            }
        }
        return tong;
    }
    
    //Bài 4
    public void PhanTichThuaSoNguyenTo(int n){
        int dem = 0;
        int i = 2;
        //for (int i=2; i<=n; i++){
        while (n > 1) {
            if (n % i == 0) {
                dem++;
                if (n == i) {
                    System.out.print(" " + i + "^" + dem);
                }
                
                n /= i; //chia n xuống i lần
            } else {
                if (dem > 0) {
                    System.out.print(" " + i + "^" + dem);
                    dem = 0;
                }
                i++;
            }
        }
        //}
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BTTuan1 bttuan1 = new BTTuan1();
        int n = 0;
        
        do{
            System.out.print("Nhập n (>=3): ");
            n = scanner.nextInt();
        }while (n < 3);
        
        System.out.println("1. Tổng S=1+2..+n: " + bttuan1.XuatTong(n)); 
        
        System.out.println("2. Tổng ước số của n: " + bttuan1.TongUocSo(n)); 
        
        //System.out.println("3. Tổng số nguyên tố <=n: " + bttuan1.TinhTongNguyenTo(n)); 
        
        System.out.print("4. Phân tích n thành thừa số nguyên tố: "); 
        bttuan1.PhanTichThuaSoNguyenTo(n);
        
        System.out.println();
        
    }
    
    
    
    
}
