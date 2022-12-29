package com.whg.itomcat.ch01;

import java.io.*;

public class Response {

    private final OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void sendStaticResource(String uri) throws IOException {
        // String path = Main.class.getResource("").getPath();
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println(path);
        // path = path + "whg/test.txt";
        if("/".equals(uri)){
            uri = "index.html";
        }
        path = path + uri;
        System.out.println("access "+path+"\n");

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
        File file = new File(path);
        if(file.exists()){
            FileInputStream fi = new FileInputStream(file);
            byte[] fileData = new byte[fi.available()];
            fi.read(fileData);
            fi.close();

            String fileContent = new String(fileData);
            writer.write("HTTP/1.1 200 OK");
            writer.newLine();
            writer.newLine();
            writer.write(fileContent);
        }else{
            writer.write("HTTP/1.1 404 File Not Found");
            writer.newLine();
            writer.newLine();
            writer.write("<h1>"+uri+" Not Found</h1>");
        }

        writer.flush();
        writer.close();
    }

}
