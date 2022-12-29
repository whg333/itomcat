package com.whg.itomcat.ch01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Request {

    private final InputStream input;

    private String uri;

    public Request(InputStream input){
        this.input = input;
    }

    public void parse() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = reader.readLine();
        parseUri(line);

        while(!isEmpty(line)){
            System.out.println(line);
            line = reader.readLine();
        }
    }

    private void parseUri(String line) {
        String[] lineArr = line.split(" ");
        uri = lineArr[1];
    }

    private boolean isEmpty(String str){
        return str == null || str.equals("");
    }

    public String getUri(){
        return uri;
    }

}
