package com.intellij.plugin.cljfmt.repl;

import com.dampcake.bencode.BencodeInputStream;
import com.intellij.plugin.cljfmt.action.BencodeOutputStreamUTF8;
import com.intellij.plugin.cljfmt.exception.ReplConnectionException;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ReplClient {
    private Socket conn;
    private String host;
    private int port;

    public ReplClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
        try {
            conn = new Socket(host, port);
        } catch (IOException e) {
            throw new ReplConnectionException(host, port);
        }
    }
    public void close() {
        try {
            conn.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String eval(String code) {
        try {
            BencodeOutputStreamUTF8 bo = new BencodeOutputStreamUTF8(conn.getOutputStream());
            BencodeInputStream bi = new BencodeInputStream(conn.getInputStream());

            bo.writeDictionary(new HashMap<Object, Object>() {{
                put("code", code);
                put("op", "eval");
            }});

            Map<String, Object> resp = bi.readDictionary();
            String error = (String) resp.get("err");

            if (error != null) {
                throw new RuntimeException(error);
            } else {
                return (String) resp.get("value");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
