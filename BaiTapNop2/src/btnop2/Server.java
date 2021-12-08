/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package btnop2;

/**
 *
 * @author ADMIN
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.List;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    //color 
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    
    public static int buffsize = 4096;
    public static int port = 1234;
    public static String APIKEY = "7e6b1897-1c06-4165-84a3-dfaf17b002de";
    
    public void UDPServer() {
        DatagramSocket socket;
        DatagramPacket dpreceive, dpsend;
        while (true) {
            try {
                socket = new DatagramSocket(port);
                dpreceive = new DatagramPacket(new byte[buffsize], buffsize);
                while (true) {
                    socket.receive(dpreceive);
                    String receiveData = new String(dpreceive.getData(), 0, dpreceive.getLength()); //Nhận dữ liệu từ client
                    System.out.println("Server received: " + receiveData + " from "
                            + dpreceive.getAddress().getHostAddress() + " at port "
                            + socket.getLocalPort());
                    
                    if (receiveData.equals("bye")) {
                        System.out.println("Server socket closed");
                        socket.close();
                        break;
                    }
                    
                    String stringResult = "";
                    
                    // Xử lý
                    StringTokenizer strToken = new StringTokenizer(receiveData, ";");

                    if (strToken.countTokens() > 3) {
                        stringResult = ANSI_YELLOW + "Cú pháp không hợp lệ!\n" + ANSI_RESET;
                    } else {
                        //TH: Hello và Country (1 token)
                        if (strToken.countTokens() == 1) {
                            String firstToken = strToken.nextToken();

                            if (firstToken.equals("Hello")) {
                                List<String> listCountry = this.GetCountryList(APIKEY);

                                stringResult += ANSI_GREEN + "== DANH SÁCH QUỐC GIA ==\n" + ANSI_RESET;
                                for (String item : listCountry) {
                                    stringResult += ANSI_GREEN + item + "\n" + ANSI_RESET;
                                }
                            } else {
                                List<String> listCountry = this.GetCountryList(APIKEY);
                                List<String> listStateTemp = new ArrayList<String>();

                                for (String itemCountry : listCountry) {
                                    if (firstToken.equals(itemCountry)) {
                                        List<String> listState = this.GetStateList(APIKEY, itemCountry);

                                        for (String itemState : listState) {
                                            listStateTemp.add(itemState);
                                        }
                                    }
                                }

                                if (listStateTemp.isEmpty()) {
                                    stringResult = ANSI_YELLOW + "Country này không có trong kho dữ liệu! Mời nhập lại!\n" + ANSI_RESET;
                                } else {
                                    stringResult += ANSI_GREEN + "== DANH SÁCH STATE ==\n" + ANSI_RESET;
                                    for (String itemState : listStateTemp) {
                                        stringResult += ANSI_GREEN + itemState + "\n" + ANSI_RESET;
                                    }
                                }
                            }
                        }

                        //TH: Country;State (2 token)
                        if (strToken.countTokens() == 2) {
                            String firstToken = strToken.nextToken();
                            String secondToken = strToken.nextToken();

                            List<String> listCountry = this.GetCountryList(APIKEY);
                            List<String> listCityTemp = new ArrayList<String>();

                            for (String itemCountry : listCountry) {
                                if (firstToken.equals(itemCountry)) {
                                    List<String> listState = this.GetStateList(APIKEY, itemCountry);

                                    for (String itemState : listState) {
                                        if (itemState.equals(secondToken)) {
                                            List<String> listCity = this.GetCityList(APIKEY, itemCountry, itemState);

                                            for (String itemCity : listCity) {
                                                listCityTemp.add(itemCity);
                                            }
                                        }
                                    }
                                }
                            }

                            if (listCityTemp.isEmpty()) {
                                stringResult += ANSI_YELLOW + "Country hoặc State không không đúng! Mời nhập lại!\n" + ANSI_RESET;
                            } else {
                                stringResult += ANSI_GREEN + "== DANH SÁCH CITY ==\n" + ANSI_RESET;
                                for (String itemCity : listCityTemp) {
                                    stringResult += ANSI_GREEN + itemCity  + "\n" + ANSI_RESET;
                                }
                            }

                        }

                        //TH: Country;State;City (3 token)
                        if (strToken.countTokens() == 3) {
                            String firstToken = strToken.nextToken();
                            String secondToken = strToken.nextToken();
                            String thirdToken = strToken.nextToken();

                            List<String> listCountry = this.GetCountryList(APIKEY);

                            for (String itemCountry : listCountry) {
                                if (firstToken.equals(itemCountry)) { //check city
                                    List<String> listState = this.GetStateList(APIKEY, itemCountry);

                                    for (String itemState : listState) {
                                        if (itemState.equals(secondToken)) { //check state
                                            List<String> listCity = this.GetCityList(APIKEY, itemCountry, itemState);

                                            for (String itemCity : listCity) { //check city
                                                if (itemCity.equals(thirdToken)) {
                                                    stringResult += "Chỉ số AQI là: " + this.GetAQI(APIKEY, itemCountry, itemState, itemCity) + "\n";

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }


                    
                    dpsend = new DatagramPacket(stringResult.getBytes(), stringResult.getBytes().length,
                            dpreceive.getAddress(), dpreceive.getPort());
                    System.out.println("Server sent back: " + dpsend + "");
                    socket.send(dpsend);
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }

    }
    

    public static List<String> GetCountryList(String APIKEY){
        Document doc = null;
        List<String> listResults = new ArrayList<String>();
        String getCountryAPIURL = "https://api.airvisual.com/v2/countries?key=" + APIKEY + "";
    
        try {
            doc = Jsoup.connect(getCountryAPIURL).ignoreContentType(true).get();
        } catch (IOException ex) {
            System.out.println(ex);
        }

        JSONObject jsonObj = new JSONObject(doc.body().text());

        JSONArray jsonArr = jsonObj.getJSONArray("data");

        //get list
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject objItem = jsonArr.getJSONObject(i);
            listResults.add(objItem.getString("country"));
        }
        
        return listResults;
    }
    
    public static List<String> GetStateList(String APIKEY, String countryName){
        Document doc = null;
        List<String> listResults = new ArrayList<String>();
        String getStatesAPIURL = "http://api.airvisual.com/v2/states?country="+ countryName +"&key=" + APIKEY + "";
    
        try {
            doc = Jsoup.connect(getStatesAPIURL).ignoreContentType(true).get();
        } catch (IOException ex) {
            System.out.println(ex);
        }

        JSONObject jsonObj = new JSONObject(doc.body().text());

        JSONArray jsonArr = jsonObj.getJSONArray("data");

        //get list 
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject objItem = jsonArr.getJSONObject(i);
            listResults.add(objItem.getString("state"));
        }
        
        return listResults;
    }
    
    public static List<String> GetCityList(String APIKEY, String countryName, String stateName){
        Document doc = null;
        List<String> listResults = new ArrayList<String>();
        String getCityAPIURL = "http://api.airvisual.com/v2/cities?state="+ stateName +"&country="+ countryName +"&key=" + APIKEY + "";
    
        try {
            doc = Jsoup.connect(getCityAPIURL).ignoreContentType(true).get();
        } catch (IOException ex) {
            System.out.println(ex);
        }

        JSONObject jsonObj = new JSONObject(doc.body().text());

        JSONArray jsonArr = jsonObj.getJSONArray("data");

        //get list 
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject objItem = jsonArr.getJSONObject(i);
            listResults.add(objItem.getString("city"));
        }
        
        return listResults;
    }
    
    public static int GetAQI(String APIKEY, String countryName, String stateName, String cityName){
        Document doc = null;
        String getAQIAPIURL = "http://api.airvisual.com/v2/city?city="+ cityName + "&state="+ stateName +"&country="+ countryName +"&key=" + APIKEY + "";
    
        try {
            doc = Jsoup.connect(getAQIAPIURL).ignoreContentType(true).get();
        } catch (IOException ex) {
            System.out.println(ex);
        }

        JSONObject jsonObj = new JSONObject(doc.body().text());
        JSONObject jsonObjData = jsonObj.getJSONObject("data");
        JSONObject jsonObjCurrent = jsonObjData.getJSONObject("current");
        JSONObject jsonObjPollution = jsonObjCurrent.getJSONObject("pollution");
        
        return jsonObjPollution.getInt("aqius");
            
    }
    
    
    
    public static void main(String[] args){
        Server server = new Server();
        server.UDPServer();
        
        
        
   }
    
}

        


