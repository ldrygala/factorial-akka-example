package actors

import actors.messages.{MultiplyRange, CalculateResult}
import akka.actor.Actor
import utils.FactorialUtils

class FactorialWorkerScala extends Actor {
  override def receive = {
    case MultiplyRange(start, end) => {
      sender() ! CalculateResult(FactorialUtils.multiplyRange(start, end))
      context.stop(self)
    }
  }

}