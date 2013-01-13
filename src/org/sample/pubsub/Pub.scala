package org.sample.pubsub

import com.redis._
import akka.actor.ActorSystem
import akka.actor.Actor._
import akka.actor.Props
import net.liftweb.json._
import net.sf.json.JSONObject

class Pub {
  val actorSystem = ActorSystem("PubSubActorSystem")
  println("starting publishing service..")
  val r = new RedisClient("localhost", 6379)        //開啟Redis的連結
  val p = actorSystem.actorOf(Props(new Publisher(r)))

  def publish(channel: String, message: JSONObject) = {
    p ! Publish(channel, message.toString())
  }
  def close(){
    r.quit
    
  }
}