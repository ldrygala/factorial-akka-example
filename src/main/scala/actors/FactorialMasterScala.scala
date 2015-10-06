package actors

import akka.actor.{Props, ActorRef, Actor}
import actors.messages._

/**
 * Created by ldrygala on 2015-10-06.
 */
class FactorialMasterScala extends Actor {
  var factorial = BigInt(1)
  var numbersOfWorkers = 0
  var principal: Option[ActorRef] = None
  var start: Option[Long] = None

  override def receive: Actor.Receive = distributeWork


  def distributeWork: Actor.Receive = {
    case calculateFactorial: CalculateFactorial => {
      start = Some(System.currentTimeMillis())
      principal = Some(sender())
      val multiplyRanges = createMultiplyRanges(calculateFactorial)
      multiplyRanges.foreach { multiplyRange =>
        val worker = context.actorOf(Props[FactorialWorkerScala])
        worker ! multiplyRange
        numbersOfWorkers += 1
      }
      context.become(waitingForResults)
    }
  }

  def waitingForResults: Actor.Receive = {
    case CalculateResult(tmpResult,_) => {
      numbersOfWorkers -= 1
      factorial = factorial * tmpResult
      if (numbersOfWorkers == 0) {
        principal.map(c => c ! CalculateResult(factorial, start.map(System.currentTimeMillis() - _).getOrElse(0L)))
        context.unbecome()
      }
    }
  }

  def createMultiplyRanges(calculateFactorial: CalculateFactorial) = {
    (1 to calculateFactorial.factorialNumber).grouped(calculateFactorial.partSize).map(r=>MultiplyRange(r.head, r.last))
  }

}
