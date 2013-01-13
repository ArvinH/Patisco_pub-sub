package org.sample.pubsub


import com.redis._
import akka.actor.Actor


sealed trait Msg
case class Subscribe(channels: Array[String]) extends Msg
case class Register(callback: PubSubMessage => Any) extends Msg
case class Unsubscribe(channels: Array[String]) extends Msg
case object UnsubscribeAll extends Msg
case class Publish(channel: String, msg: String) extends Msg

class Subscriber(client: RedisClient) extends Actor {
  var callback: PubSubMessage => Any = { m => }
  def receive = {
    case Subscribe(channels) =>
      client.subscribe(channels.head, channels.tail: _*)(callback)
      self ! true
    case Register(cb) =>
      callback = cb
      self ! true
    case Unsubscribe(channels) =>
      client.unsubscribe(channels.head, channels.tail: _*)
      self ! true
    case UnsubscribeAll =>
      client.unsubscribe
      self ! true
  }
}
