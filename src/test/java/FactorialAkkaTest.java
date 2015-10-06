import actors.FactorialMasterJava;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;
import messages.CalculateFactorial;
import messages.CalculationResult;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.duration.Duration;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;


public class FactorialAkkaTest {

    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        system.shutdown();
        system.awaitTermination(Duration.create("10 seconds"));
    }

    @Test
    public void shouldCreateWorkers() {
        new JavaTestKit(system) {{
            final TestActorRef<FactorialMasterJava> master =
                    TestActorRef.create(system, Props.create(FactorialMasterJava.class));

            master.tell(new CalculateFactorial(5, 1), getTestActor());

            Assert.assertEquals(5, master.underlyingActor().numbersOfWorkers);
        }};
    }

    @Test
    public void shouldCalculateFactorial() {
        new JavaTestKit(system) {{

            final TestActorRef<FactorialMasterJava> master = TestActorRef.create(system, Props.create(FactorialMasterJava.class));

            master.tell(new CalculateFactorial(5, 1), getTestActor());

            new AwaitAssert(Duration.create(2, TimeUnit.SECONDS)) {
                @Override
                protected void check() {
                    Assert.assertEquals(BigInteger.valueOf(120), master.underlyingActor().factorial);
                }
            };
        }};
    }

    @Test
    public void shouldReplyCalculatedFactorial() {
        new JavaTestKit(system) {{

            final ActorRef master = system.actorOf(Props.create(FactorialMasterJava.class));

            master.tell(new CalculateFactorial(5, 1), getTestActor());

            CalculationResult calculationResult = expectMsgClass(Duration.create(2, TimeUnit.SECONDS), CalculationResult.class);
            Assert.assertEquals(BigInteger.valueOf(120), calculationResult.result);
        }};
    }
}
