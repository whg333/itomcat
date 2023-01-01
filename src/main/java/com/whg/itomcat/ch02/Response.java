package com.whg.itomcat.ch02;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.*;
import java.util.Locale;

public class Response implements ServletResponse {

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
        // System.out.println(rootPath);
        if("/".equals(uri)){
            uri = "/index.html";
        }
        String path = rootPath + uri;
        System.out.println("access "+path+"\n");

        File file = new File(path);
        if(file.exists()){
            output.write(("HTTP/1.1 200 OK"+lineSeparator).getBytes());
            output.write(lineSeparator.getBytes());

            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[BUFFER_SIZE];
            int ch = fis.read(bytes);
            while (ch!=-1) {
                output.write(bytes, 0, ch);
                ch = fis.read(bytes);
            }
        }else if(HttpServer.isShutDown(uri)){
            String errorMessage = "HTTP/1.1 200 OK" + lineSeparator +
                    "Content-Type: text/html" + lineSeparator +
                    lineSeparator +
                    "<h3><font color=red>Server Shutdown!!!</font></h3>";
            output.write(errorMessage.getBytes());
        }else{
            String errorMessage = "HTTP/1.1 404 File Not Found" + lineSeparator +
                    "Content-Type: text/html" + lineSeparator +
                    lineSeparator +
                    "<h3><font color=red>"+uri+"</font> Not Found!</h3>";
            output.write(errorMessage.getBytes());
        }

        // output.flush();
    }


    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(output,true);
    }

    @Override
    public void setContentLength(int i) {

    }

    @Override
    public void setContentType(String s) {

    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
