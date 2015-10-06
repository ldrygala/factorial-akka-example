package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Procedure;
import messages.CalculateFactorial;
import messages.CalculationResult;
import messages.MultiplyRange;

import java.math.BigInteger;
import java.util.List;

public class FactorialMasterJava extends UntypedActor {
        private int numbersOfWorkers = 0;
        private BigInteger factorial = BigInteger.ONE;
        private long startTime;
        private ActorRef principal;

        Procedure<Object> waitingForResults = new Procedure<Object>() {

            @Override
            public void apply(Object message) throws Exception {
                if (message instanceof CalculationResult) {
                    factorial = factorial.multiply(CalculationResult.class.cast(message).result);
                    numbersOfWorkers--;
                    if (numbersOfWorkers == 0) {
                        principal.tell(new CalculationResult(factorial, (System.currentTimeMillis() - startTime)), getSelf());
                    }
                } else {
                    unhandled(message);
                }
            }
        };

        @Override
        public void onReceive(Object message) throws Exception {
            startTime = System.currentTimeMillis();
            distributeWork(message);
        }

        public void distributeWork(Object message) {
            if (message instanceof CalculateFactorial) {
                principal = getSender();
                List<MultiplyRange> factorials = utils.FactorialUtils.createMultiplyRanges(CalculateFactorial.class.cast(message));
                for (MultiplyRange factorialRange : factorials) {
                    ActorRef worker = context().actorOf(Props.create(FactorialWorkerJava.class));
                    worker.tell(factorialRange, self());
                    numbersOfWorkers++;
                }
                getContext().become(waitingForResults);
            } else {
                unhandled(message);
            }
        }
    }