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
        InitServlet.socketMap = new HashMap<String,MessageInbound>();  //利用HashMap來存放websocket連線對象，
        															   //才能將從頻道接收到的訊息透過websocket傳送給前端
        super.init(config);  
        System.out.println("Server start============");  
    }  
      
    public static synchronized Map<String,MessageInbound> getSocketMap() {  
        return InitServlet.socketMap;  
    }  
}