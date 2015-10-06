package messages;

import java.io.Serializable;

public  class MultiplyRange implements Serializable {
        public final int start;
        public final int end;

        public MultiplyRange(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
