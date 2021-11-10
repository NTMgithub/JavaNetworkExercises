
package bttuan2;

import java.util.StringTokenizer;

public class BTTuan2 {

    //Bài 1
    public double TinhCanBan(String chuoi){
        String[] cacPhepTinh = new String[]{"+", "-", "*", "/"};
        double result = 0;

        //Chuẩn hóa chuỗi
        chuoi = chuoi.trim();
        chuoi = chuoi.replaceAll("\\s+", " ");
        
        for (String phepTinh : cacPhepTinh){
            if (chuoi.contains(phepTinh)){ //Nếu tìm thấy char trong cacPhepTinh String[]
                
                String[] parts = chuoi.split("\\" + phepTinh);
                int numberLeft = Integer.parseInt(parts[0]);
                int numberRight = Integer.parseInt(parts[1]);
                
                switch (phepTinh){
                    case "+": 
                        result = numberLeft + numberRight;
                        break;
                    case "-": 
                        result = numberLeft - numberRight;
                        break;
                    case "*": 
                        result = numberLeft * numberRight;
                        break;
                    case "/": 
                        result = numberLeft/numberRight;
                        break;
                    default: return -1;
                }
                return result;
            }
        }
        return -1;
       
    }
    
    //Bài 2
    public void removeDuplicateWords(String data){
        String fullWords[];
        String output = "";
           
        data = data.toLowerCase();
        fullWords = data.split(" "); //xoa khoang trang
        
        //so sanh 
        for (int i=0; i < fullWords.length; i++ ){
            for (int j = i+1; j< fullWords.length; j++){
                if (fullWords[i].equals(fullWords[j])){
                    fullWords[j] = "removed";
                }
            }
        }
        
        //tim trong chuoi fullWords
        for (String word : fullWords){
            if (word != "removed"){
                output = output + word + " ";
            }
        }
        
        System.out.println("output: " + output);
    }

   
    
    public static void main(String[] args) {
        BTTuan2 bttuan2 = new BTTuan2();
        
        //System.out.println(bttuan2.TinhCanBan("123+34"));
        
        //main 
        String data ="Dai hoc sai gon la truong dai hoc lau doi nhat sai gon ";
        
        //bttuan2.removeDuplicateWords(data);
        
     
        
        
        
    }
    
}
