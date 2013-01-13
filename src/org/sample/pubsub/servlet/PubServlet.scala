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
		new WebSocketHandler(ID,Character)   //�]�������@�s�W�ɡA�N�NID��J�����s����H��hashmap���A�G�n�brequest����ID�Mcharacter��ӰѼ�
	}
 
}