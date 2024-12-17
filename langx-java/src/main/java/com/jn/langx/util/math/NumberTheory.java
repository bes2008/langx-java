package com.jn.langx.util.math;

public class NumberTheory {
    /**
     * 最大公约数 与 最小公倍数
     *
     * <a href="https://oi-wiki.org/math/number-theory/gcd/">GCD</a>
     */
    public static class GcdAndLcm {
        /**
         * 求两个数的最大公约数
         */
        public static int gcd2(int a, int b){
            if (b == 0) {
                return a;
            } else {
                return gcd2(b, a % b);
            }
        }

        /**
         * 求多个数的最大公约数
         */
        public static int gcdN(int... nums){
            if(nums==null || nums.length==0){
                return gcd2(0,0);
            }

            int result = nums[0];
            for (int i = 1; i < nums.length; i++) {
                result = gcd2(result, nums[i]);
                if (result == 1) {
                    // 由于1是所有正整数的最小公约数，所以一旦达到1就可以提前退出
                    return 1;
                }
            }
            return result;
        }

        /**
         * 求两个数的最小公倍数
         */
        public static int lcm2(int a, int b){
            // gcd(a,b) * lcm(a,b) = a *b
            if(a==0 || b==0){
                return 0;
            }
            return (a * b ) / gcd2(a,b);
        }

        /**
         * 求多个数的最小公倍数
         */
        public static int findLCM(int... numbers) {
            if (numbers==null ||numbers.length == 0) {
                return 0;
            }

            int result = numbers[0];
            for (int i = 1; i < numbers.length; i++) {
                result = lcm2(result, numbers[i]);
            }
            return result;
        }
    }
}
