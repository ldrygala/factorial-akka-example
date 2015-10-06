import actors.FactorialMasterJava
import actors.messages.{CalculateResult, CalculateFactorial}
import akka.actor._
import utils.FactorialUtils

import scala.concurrent.duration.DurationInt

/**
 * Created by ldrygala on 2015-10-02.
 */
object FactorialAkkaScala extends App {
  val Factorial = 100000
  val PartSize = 1000
  val system = ActorSystem("factorialSystem")
  val master = system.actorOf(Props[FactorialMasterJava], "master")
  val inbox = Inbox.create(system)
  master.tell(CalculateFactorial(Factorial,PartSize), inbox.getRef())
  val akkaResult = inbox.receive(10 seconds).asInstanceOf[CalculateResult]

  system.shutdown

  val standardResult = FactorialUtils.calculateFactorialWithStandardScalaWay(Factorial)

  if (akkaResult.result == standardResult.result) {
    System.out.println("Akka time = " + akkaResult.executionTime + " millis")
    System.out.println("Standard time = " + standardResult.executionTime + " millis")
  }
}




