package org.sample.pubsub.model
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.CharBuffer
import org.sample.pubsub._
import net.liftweb.json._
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.sample.pubsub.servlet._
import scala.collection.JavaConversions._ // �[�F�o�� �~�i�H�bscala���ϥ�foreach�ޱ�java list
import net.sf.json.JSONObject

class WebSocketHandler(id: String, Character: String) extends MessageInbound {
  var sub: Sub = _ //scala���]�wnull( _ )����ŧi�]�n�����w���O�^
  var pub: Pub = _
  var content: String = _
  var contentArray: Array[String] = _
  protected override def onBinaryMessage(arg0: ByteBuffer) {
    // TODO Auto-generated method stub  

  }
  protected override def onTextMessage(msg: CharBuffer) {
    content = msg.toString()
    contentArray = content.split(":")
    //�NCharBuffer�����e�নstring�i��split�B�z�A  0:�N�� pub or sub
    // for subscriber:  1:channel�W��  
    // for publisher:   1:channel�W��  2:�ǰe���T�� 
    if (contentArray(0).equals("SUBSCRIBE")) {

      sub.sub(contentArray(1))

    } else if (contentArray(0).equals("PUBLISH")) {
      
        val jsonObject = new JSONObject();
        jsonObject.put(contentArray(1), contentArray(2))
        pub.publish(contentArray(1), jsonObject)
      
    }

  }

  protected override def onClose(status: Int) {
    //�n���P�_�OSubscriber or Publisher�n����
    if (Character.equals("publisher"))
      pub.close()
    else if (Character.equals("subscriber")) {
      if (sub.getSubscribeNo == 0) {   //�p�G��subscriber�q�\���W�D�Ƭ��s�A�h�i����close�Predis���s��
        sub.close()
      } else {						   //�p�G��subscriber�q�\���W�D�Ƥ����s�A�N������unsubscribe�A�Aclose
        sub.unsubAll()
      }
    }
    InitServlet.getSocketMap().remove(id);
  }

  protected override def onOpen(outbound: WsOutbound) {
    super.onOpen(outbound)
    if (Character.equals("publisher")) {
      pub = new Pub() //websocket�إߦn��A�إ�redis����
      InitServlet.getSocketMap().put(id, this)
    } else if (Character.equals("subscriber")) {
      sub = new Sub(id)
      InitServlet.getSocketMap().put(id, this)
    }

  }
}

