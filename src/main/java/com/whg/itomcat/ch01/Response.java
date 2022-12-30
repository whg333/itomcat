package com.whg.itomcat.ch01;

import java.io.*;

public class Response {

    private static final String lineSeparator = java.security.AccessController.doPrivileged(
            new sun.security.action.GetPropertyAction("line.separator"));

    private static final int BUFFER_SIZE = 1024;

    private final OutputStream output;
    private String rootPath;

    public Response(OutputStream output) {
        this.output = output;

        // String path = Main.class.getResource("").getPath();
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        rootPath = path.substring(0, path.length()-1);
    }

    public void sendStaticResource(String uri) throws IOException {
        System.out.println(rootPath);
        // path = path + "whg/test.txt";
        if("/".equals(uri)){
            uri = "/index.html";
        }
        String path = rootPath + uri;
        System.out.println("access "+path+"\n");

        // BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
        File file = new File(path);
        if(file.exists()){
            output.write(("HTTP/1.1 200 OK"+lineSeparator).getBytes());
            output.write(lineSeparator.getBytes());

            FileInputStream fis = new FileInputStream(file);
            // byte[] fileData = new byte[fis.available()];
            // fis.read(fileData);
            // fis.close();
            byte[] bytes = new byte[BUFFER_SIZE];
            int ch = fis.read(bytes);
            while (ch!=-1) {
                output.write(bytes, 0, ch);
                ch = fis.read(bytes);
            }

            // output.write(fileData);

            // String fileContent = new String(fileData);
            // writer.write("HTTP/1.1 200 OK");
            // writer.newLine();
            // writer.newLine();
            // writer.write(fileContent);
        }else{
            String errorMessage = "HTTP/1.1 404 File Not Found" + lineSeparator +
                    "Content-Type: text/html" + lineSeparator +
                    lineSeparator +
                    "<h3><font color=red>"+uri+"</font> Not Found</h3>";
            output.write(errorMessage.getBytes());

            // writer.write("HTTP/1.1 404 File Not Found");
            // writer.newLine();
            // writer.newLine();
            // writer.write("<h1>"+uri+" Not Found</h1>");
        }

        output.flush();
        // writer.flush();
        // writer.close();
    }

}
