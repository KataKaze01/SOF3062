package com.example.demo.client;

import java.net.HttpURLConnection;

public class RestClientDemo {
    // Bạn có thể đổi thành link Firebase của bạn hoặc giữ localhost nếu đang chạy ứng dụng Spring Boot
    static String HOST = "http://localhost:8080/simple/students";

    public static void main(String[] args) throws Exception {
        System.out.println("--- GET ALL ---");
        getAll();

        System.out.println("\n--- POST (Create) ---");
        post();

        System.out.println("\n--- GET ALL (After Post) ---");
        getAll();
    }

    private static void getAll() throws Exception {
        HttpURLConnection conn = HttpClient.openConnection("GET", HOST);
        String response = HttpClient.readData(conn);
        System.out.println(response);
    }

    private static void post() throws Exception {
        String jsonInputString = "{\"id\": \"SVNEW\", \"name\": \"Sinh Vien Moi\", \"mark\": 9.9, \"gender\": true}";
        HttpURLConnection conn = HttpClient.openConnection("POST", HOST);
        String response = HttpClient.writeData(conn, jsonInputString);
        System.out.println("Response: " + response);
    }
    // Tương tự cho put(), delete()...
}