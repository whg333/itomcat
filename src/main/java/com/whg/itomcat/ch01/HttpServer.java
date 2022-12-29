package com.whg.itomcat.ch01;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    public static void main(String[] args) {
        new HttpServer().start();
    }

    public void start() {
        try {
            ServerSocket server = new ServerSocket(8080, 1);
            while(true){
                Socket client = server.accept();
                handler(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handler(Socket client) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(client.getInputStream()));
        String line = reader.readLine();
        while(!isEmpty(line)){
            System.out.println(line);
            line = reader.readLine();
        }
        System.out.println();

        // String path = Main.class.getResource("").getPath();
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println(path);
        path = path + "whg/test.txt";
        File file = new File(path);
        FileInputStream fi = new FileInputStream(file);
        byte[] fileData = new byte[fi.available()];
        fi.read(fileData);
        String fileContent = new String(fileData);

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(client.getOutputStream()));
        writer.write("HTTP/1.1 200 OK");
        writer.newLine();
        writer.newLine();
        // writer.write("Hello, itomcat.");
        writer.write(fileContent);
        writer.flush();

        reader.close();
        writer.close();
        client.close();
    }

    private boolean isEmpty(String str){
        return str == null || str.equals("");
    }

}
