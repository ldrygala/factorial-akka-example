package messages;

import java.io.Serializable;
import java.math.BigInteger;

public class CalculationResult implements Serializable {
        public final BigInteger result;
        public final long executionTime;

        public CalculationResult(BigInteger result, long executionTime) {
            this.result = result;
            this.executionTime = executionTime;
        }
        public CalculationResult(BigInteger result) {
            this.result = result;
            this.executionTime = 0;
        }
    }