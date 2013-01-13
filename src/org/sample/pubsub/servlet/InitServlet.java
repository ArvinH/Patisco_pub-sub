package org.sample.pubsub.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.catalina.websocket.MessageInbound;

public class InitServlet extends HttpServlet {

	private static final long serialVersionUID = -3163557381361759907L;
	
	private static Map<String,MessageInbound> socketMap;  
    
    public void init(ServletConfig config) throws ServletException {  
        InitServlet.socketMap = new HashMap<String,MessageInbound>();  //�Q��HashMap�Ӧs��websocket�s�u��H�A
        															   //�~��N�q�W�D�����쪺�T���z�Lwebsocket�ǰe���e��
        super.init(config);  
        System.out.println("Server start============");  
    }  
      
    public static synchronized Map<String,MessageInbound> getSocketMap() {  
        return InitServlet.socketMap;  
    }  
}