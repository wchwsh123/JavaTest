package abuli.test.utils;

import java.util.function.Supplier;

public class StatisticsUtils {

    public static class TimeCost<R> implements Supplier<R> {

        private final Supplier<R> function;

        public TimeCost(Supplier<R> function) {
            this.function = function;
        }

        @Override
        public R get() {
            final long startTime = System.currentTimeMillis();
            final R result = function.get();
            System.out.printf("CostMills:%s||Result:%s%n", (System.currentTimeMillis() - startTime), result);
            return result;
        }
    }
}
