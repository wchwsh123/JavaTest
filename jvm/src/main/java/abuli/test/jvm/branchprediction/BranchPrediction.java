package abuli.test.jvm.branchprediction;

import abuli.test.utils.StatisticsUtils;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 */
public class BranchPrediction {

    public static void main(String[] args) {
        //case_performance_calc();
        //case_performance_AB_or_A_B();
        //case_performance_most_likely();
        case_performance_merge_condition();
    }

    private static void case_performance_merge_condition() {
        final int[] data1 = buildArray(100000000);
        final int[] data2 = buildArray(100000000);
        for (int i = 0; i < data1.length; i++) {
            if (data1[i] < 10) {
                data1[i] = 0;
            }
        }
        for (int i = 0; i < data2.length; i++) {
            if (data2[i] < 10) {
                data2[i] = 0;
            }
        }
        final StatisticsUtils.TimeCost<Long> case1 = new StatisticsUtils.TimeCost<>(() -> {
            long sum = 0;
            for (int i = 0; i < data1.length; i++) {
                if (data1[i] != 0 && data2[i] != 0) {
                    ++sum;
                }
            }
            return sum;
        });
        final StatisticsUtils.TimeCost<Long> case2 = new StatisticsUtils.TimeCost<>(() -> {
            long sum = 0;
            for (int i = 0; i < data1.length; i++) {
                if (data1[i] * data2[i] != 0) {
                    ++sum;
                }
            }
            return sum;
        });
        case1.get();
        case2.get();
    }

    /**
     * if/else 语句的分支顺序很重要
     */
    private static void case_performance_most_likely() {
        final int[] data = buildArray(99999999);
        final StatisticsUtils.TimeCost<Long> case1 = new StatisticsUtils.TimeCost<>(() -> {
            long sum = 0;
            for (int number : data) {
                if (number > 400) {
                    sum *= number;
                } else if (number > 300) {
                    sum -= number;
                } else if (number > 270) {
                    sum /= number;
                } else {
                    sum += number;
                }
                sum += number;
            }
            return sum;
        });
        final StatisticsUtils.TimeCost<Long> case2 = new StatisticsUtils.TimeCost<>(() -> {
            long sum = 0;
            for (int number : data) {
                if (number <= 270) {
                    sum += number;
                } else if (number <= 300) {
                    sum /= number;
                } else if (number <= 400) {
                    sum -= number;
                } else {
                    sum *= number;
                }
            }
            return sum;
        });
        case1.get();
        case2.get();
    }

    private static void case_performance_AB_or_A_B() {
        final int loopCount = 100000;
        final int[] data1 = buildArray(99999);
        final int[] data2 = buildArray(99999);
        final StatisticsUtils.TimeCost<Long> case1 = new StatisticsUtils.TimeCost<>(() -> {
            long sum = 0;
            for (int i = 0; i < loopCount; i++) {
                for (int number : data1) {
                    sum += number;
                }
                for (int number : data2) {
                    sum -= number;
                }
            }
            return sum;
        });
        final StatisticsUtils.TimeCost<Long> case2 = new StatisticsUtils.TimeCost<>(() -> {
            long sum = 0;
            for (int i = 0; i < loopCount; i++) {
                for (int j = 0; j < data1.length; j++) {
                    sum += data1[j];
                    sum -= data2[j];
//                    sum = sum + data1[j] - data2[j]; //一样
                }
            }
            return sum;
        });
        case1.get();
        case2.get();
    }

    /**
     * for循环含分支结构
     * 1.未排序数组，单次遍历性能好，多次遍历性能逐渐下降
     * 2.排序数组反之
     */
    private static void case_performance_calc() {
        final int[] data = buildArray(32768);
        final StatisticsUtils.TimeCost<Long> case1 = new StatisticsUtils.TimeCost<>(() -> sum(data));
        final StatisticsUtils.TimeCost<Long> case2 = new StatisticsUtils.TimeCost<>(() -> {
            Arrays.sort(data);
            return sum(data);
        });
        case1.get();
        case2.get();
    }

    private static int[] buildArray(int arraySize) {
        final int[] data = new int[arraySize];
        final Random random = new Random();
        for (int i = 0; i < arraySize; i++) {
            data[i] = random.nextInt() % 256;
        }
        return data;
    }

    private static long sum(int[] data) {
        long sum = 0;
        for (int i = 0; i < 100000; i++) {
            for (int number : data) {
                if (number > 128) {
                    sum += number;
                }
            }
        }
        return sum;
    }

}
