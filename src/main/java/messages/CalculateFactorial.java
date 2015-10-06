package messages;

import java.io.Serializable;

public class CalculateFactorial implements Serializable {
        public final int factorialNumber;
        public final int partSize;

        public CalculateFactorial(int factorialNumber, int partSize) {
            this.factorialNumber = factorialNumber;
            this.partSize = partSize;
        }
    }