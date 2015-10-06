import actors.FactorialMasterScala
import actors.messages.{CalculateResult, CalculateFactorial}
import akka.actor.{Props, ActorSystem}
import scala.concurrent.duration.DurationInt
import akka.testkit.{TestActorRef, ImplicitSender, TestKit}
import org.scalatest.{FlatSpecLike, FlatSpec, BeforeAndAfterAll, Matchers}

/**
 * Created by ldrygala on 2015-10-03.
 */
class FactorialAkkaSpec
  extends TestKit(ActorSystem("factorialSystem"))
  with ImplicitSender
  with Matchers
  with FlatSpecLike
  with BeforeAndAfterAll {


  "FactorialMasterActor" should "create workers" in {
    val master = TestActorRef[FactorialMasterScala](Props[FactorialMasterScala])
    master ! CalculateFactorial(5, 1)
    master.underlyingActor.numbersOfWorkers shouldBe 5
  }

  it should "calculate factorial correctly" in {
    val master = TestActorRef[FactorialMasterScala](Props[FactorialMasterScala])
    master ! CalculateFactorial(5, 1)
    within(2 seconds) {
      master.underlyingActor.factorial shouldBe 120
    }
  }
  it should "reply with calculated factorial" in {
    val master = system.actorOf(Props[FactorialMasterScala])
    master ! CalculateFactorial(5, 1)
    expectMsgPF() {
      case CalculateResult(f,_) => f shouldBe 120
    }
  }

  override protected def afterAll(): Unit = {
    system.shutdown()
    system.awaitTermination(10.seconds)
  }
}
