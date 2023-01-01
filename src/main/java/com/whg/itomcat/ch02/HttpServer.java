package com.whg.itomcat.ch02;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Server stop on "+port+" ...");
    }

    private boolean handler(Socket client) throws IOException,
            ClassNotFoundException, InstantiationException, IllegalAccessException,
            ServletException {
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
        if(uri.contains("/servlet")){
            Class<?> clazz = Thread.currentThread().getContextClassLoader()
                    .loadClass("servlet.PrimitiveServlet");
            Object obj = clazz.newInstance();
            Servlet servlet = (Servlet)obj;
            servlet.service(request, response);
        }else{
            response.sendStaticResource(uri);
        }

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
