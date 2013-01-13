package org.sample.pubsub

import com.redis._
import akka.actor.ActorSystem
import akka.actor.Actor._
import akka.actor.Props
import net.liftweb.json._ // json lib for scala
import net.sf.json.JSONObject // json lib for java
import org.sample.pubsub.servlet._
import scala.collection.JavaConversions._
import java.nio.CharBuffer

class Sub(id: String) {
  var subscribeNo = 0
  val actorSystem = ActorSystem("PubSubActorSystem")
  println("starting subscription service...")
  val r = new RedisClient("localhost", 6379)
  val s = actorSystem.actorOf(Props(new Subscriber(r)))
  s ! Register(callback)

  def sub(channels: String*) = {
    s ! Subscribe(channels.toArray)
  }

  def unsub(channels: String*) = {
    s ! Unsubscribe(channels.toArray)
  }
  def unsubAll() = {
    s ! UnsubscribeAll
  }
  def close() {
    r.quit
  }
  def getSubscribeNo(): Int = {
    subscribeNo  //取出目前訂閱的頻道數
  }
  def callback(pubsub: PubSubMessage) = pubsub match {
    case S(channel, no) => {
      println("Subscriber: " + id + " subscribed to " + channel + " and count = " + no)
      subscribeNo = no
    }
    case U(channel, no) => {
      println("unsubscribed from " + channel + " and count = " + no)
      if (no == 0) {
        r.quit
      }
    }
    case M(channel, msg) =>
      msg match {
     /*   // exit will unsubscribe from all channels and stop subscription service
        case "exit" =>
          println("unsubscribe all ..")
          r.unsubscribe

        // message "+x" will subscribe to channel x
        case x if x startsWith "+" =>
          val s: Seq[Char] = x
          s match {
            case Seq('+', rest @ _*) => r.subscribe(rest.toString) { m => }
          }

        // message "-x" will unsubscribe from channel x
        case x if x startsWith "-" =>
          val s: Seq[Char] = x
          s match {
            case Seq('-', rest @ _*) => r.unsubscribe(rest.toString)
          }
	*/
        // other message receive
        case x =>
          //處裡String to JSON
          val messageJson = parse(x)
          val result = "subscriber: " + id + " received message on channel " + channel + " as : " + compact(render(messageJson))
          //從hashmap中取出對應的Websocket outbound，將訊息寫出
          val buffer = CharBuffer.wrap(result)
          val outbound = InitServlet.getSocketMap().get(id).getWsOutbound()
          outbound.writeTextMessage(buffer)
          outbound.flush()

      }
  }
}