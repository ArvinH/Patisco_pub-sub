package org.sample.pubsub

import com.redis._
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Actor

class Publisher(client: RedisClient) extends Actor {
  def receive = {
    case Publish(channel, message) =>
      client.publish(channel, message)
      self ! true
  }
}