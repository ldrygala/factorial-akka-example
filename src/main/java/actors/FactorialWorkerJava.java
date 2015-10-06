package actors;

import akka.actor.UntypedActor;
import messages.CalculationResult;
import messages.MultiplyRange;
import utils.FactorialUtils;

public class FactorialWorkerJava extends UntypedActor {

        @Override
        public void onReceive(Object message) throws Exception {
            if (message instanceof MultiplyRange) {
                MultiplyRange factorialRange = MultiplyRange.class.cast(message);
                sender().tell(new CalculationResult(FactorialUtils.multiplyRange(factorialRange.start, factorialRange.end).bigInteger()), self());
                context().stop(self());
            } else {
                unhandled(message);
            }
        }


    }