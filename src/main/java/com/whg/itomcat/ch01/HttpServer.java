package com.whg.itomcat.ch01;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    private static final String SHUTDOWN = "/SHUTDOWN";
    private boolean shutdown = false;

    public void start(int port, int backlog) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(port, backlog);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Server start on "+port+" ...");

        while(!shutdown){
            try {
                Socket client = server.accept();
                shutdown = handler(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Server stop on "+port+" ...");
    }

    private boolean handler(Socket client) throws IOException {
        InputStream input = client.getInputStream();
        Request request = new Request(input);
        boolean parsed = request.parse();
        if(!parsed){
            input.close();
            client.close();
            return false;
        }

        OutputStream output = client.getOutputStream();
        Response response = new Response(output);
        String uri = request.getUri();
        response.sendStaticResource(uri);

        input.close();
        output.close();
        client.close();

        return isShutDown(uri);
    }

    public static boolean isShutDown(String uri){
        return uri.equals(SHUTDOWN);
    }

    public static void main(String[] args) {
        new HttpServer().start(8080, 1);
    }

}
