import actors.FactorialMasterJava;
import utils.FactorialUtils;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import messages.CalculateFactorial;
import messages.CalculationResult;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by ldrygala on 2015-10-01.
 */
public class FactorialAkkaJava {

    public static final int FACTORIAL = 100000;
    public static final int PART_SIZE = 1000;


    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("factorialSystem");
        ActorRef actorRef = system.actorOf(Props.create(FactorialMasterJava.class), "master");
        Inbox inbox = Inbox.create(system);
        actorRef.tell(new CalculateFactorial(FACTORIAL, PART_SIZE), inbox.getRef());


        CalculationResult akkaResult = (CalculationResult) inbox.receive(Duration.create(10, TimeUnit.SECONDS));
        system.shutdown();

        CalculationResult standardResult = FactorialUtils.calculateFactorialWithStandardJavaWay(FACTORIAL);

        if (akkaResult.result.equals(standardResult.result)) {

            System.out.println("Akka time = " + akkaResult.executionTime + " millis");
            System.out.println("Standard time = " + standardResult.executionTime + " millis");
        }

    }




}
