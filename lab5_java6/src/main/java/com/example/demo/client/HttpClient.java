package com.example.demo.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

    // Mở kết nối đến URL [cite: 285-287]
    public static HttpURLConnection openConnection(String method, String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        return connection;
    }

    // Đọc dữ liệu trả về [cite: 289-304]
    public static String readData(HttpURLConnection connection) throws IOException {
        try (InputStream is = (connection.getResponseCode() >= 400) ? connection.getErrorStream() : connection.getInputStream();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            if (is == null) return "";
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            return out.toString("UTF-8");
        }
    }

    // Gửi dữ liệu đi (cho POST, PUT) [cite: 306-308]
    public static String writeData(HttpURLConnection connection, String data) throws IOException {
        try (var os = connection.getOutputStream()) {
            os.write(data.getBytes("UTF-8"));
        }
        return readData(connection);
    }
}