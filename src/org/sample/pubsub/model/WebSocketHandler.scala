package org.sample.pubsub.model
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.CharBuffer
import org.sample.pubsub._
import net.liftweb.json._
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.sample.pubsub.servlet._
import scala.collection.JavaConversions._ // 加了這個 才可以在scala中使用foreach操控java list
import net.sf.json.JSONObject

class WebSocketHandler(id: String, Character: String) extends MessageInbound {
  var sub: Sub = _ //scala中設定null( _ )全域宣告（要先指定類別）
  var pub: Pub = _
  var content: String = _
  var contentArray: Array[String] = _
  protected override def onBinaryMessage(arg0: ByteBuffer) {
    // TODO Auto-generated method stub  

  }
  protected override def onTextMessage(msg: CharBuffer) {
    content = msg.toString()
    contentArray = content.split(":")
    //將CharBuffer的內容轉成string進行split處理，  0:代表 pub or sub
    // for subscriber:  1:channel名稱  
    // for publisher:   1:channel名稱  2:傳送的訊息 
    if (contentArray(0).equals("SUBSCRIBE")) {

      sub.sub(contentArray(1))

    } else if (contentArray(0).equals("PUBLISH")) {
      
        val jsonObject = new JSONObject();
        jsonObject.put(contentArray(1), contentArray(2))
        pub.publish(contentArray(1), jsonObject)
      
    }

  }

  protected override def onClose(status: Int) {
    //要先判斷是Subscriber or Publisher要關掉
    if (Character.equals("publisher"))
      pub.close()
    else if (Character.equals("subscriber")) {
      if (sub.getSubscribeNo == 0) {   //如果此subscriber訂閱的頻道數為零，則可直接close與redis的連結
        sub.close()
      } else {						   //如果此subscriber訂閱的頻道數不為零，就必須先unsubscribe，再close
        sub.unsubAll()
      }
    }
    InitServlet.getSocketMap().remove(id);
  }

  protected override def onOpen(outbound: WsOutbound) {
    super.onOpen(outbound)
    if (Character.equals("publisher")) {
      pub = new Pub() //websocket建立好後，建立redis物件
      InitServlet.getSocketMap().put(id, this)
    } else if (Character.equals("subscriber")) {
      sub = new Sub(id)
      InitServlet.getSocketMap().put(id, this)
    }

  }
}

