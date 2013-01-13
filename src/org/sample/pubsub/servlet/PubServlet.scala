package org.sample.pubsub.servlet

import org.sample.pubsub.model.WebSocketHandler
import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import java.lang.String


class PubServlet extends WebSocketServlet {
  private val serialVersionUID = -7178893327801338294L;
 override def createWebSocketInbound (subProtocol: String, request: HttpServletRequest): StreamInbound = {
		val ID = request.getParameter("ID")
		val Character = request.getParameter("character")
		new WebSocketHandler(ID,Character)   //因為必須一連上時，就將ID塞入紀錄連接對象的hashmap中，故要在request中放ID和character兩個參數
	}
 
}