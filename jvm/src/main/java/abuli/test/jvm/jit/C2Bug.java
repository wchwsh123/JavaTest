package abuli.test.jvm.jit;

/**
 * 满足热点探测条件，会出发JIT优化（例如高过一定次数的循环）
 * -XX：CompileThreshold 查询
 */
public class C2Bug {

    public static void main(String[] args) {
        final C2Bug instance = new C2Bug();
        for (int i = 0; i < 999_999; i++) {
            instance.test();
        }
    }

    public void test() {
        int i = 8;
        while ((i -= 3) > 0) ;
        System.out.println("i = " + i);
    }

}
