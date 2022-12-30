package com.whg.itomcat.ch01;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    private boolean shutdown = false;

    public void start(int port, int backlog) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(port, backlog);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while(!shutdown){
            try {
                Socket client = server.accept();
                handler(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handler(Socket client) throws IOException {
        InputStream input = client.getInputStream();
        Request request = new Request(input);
        boolean parsed = request.parse();
        if(!parsed){
            input.close();
            client.close();
            return;
        }

        OutputStream output = client.getOutputStream();
        Response response = new Response(output);
        response.sendStaticResource(request.getUri());

        input.close();
        output.close();
        client.close();
    }

    public static void main(String[] args) {
        new HttpServer().start(8080, 1);
    }

}
