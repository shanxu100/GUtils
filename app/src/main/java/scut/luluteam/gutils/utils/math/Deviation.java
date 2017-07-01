package scut.luluteam.gutils.utils.math;

/**
 * Created by guan on 4/13/17.
 */

public class Deviation {

    /**
     * 传统的利用平均数求方差的方法,需要遍历数组两次
     *
     * @param a 目标数组
     * @return 方差
     */
    public static double ComputeVariance(double a[]) {
        double variance = 0;//方差
        double average = 0;//平均数
        int i, len = a.length;
        double sum = 0, sum2 = 0;
        for (i = 0; i < len; i++) {
            sum += a[i];
        }
        average = sum / len;
        for (i = 0; i < len; i++) {
            sum2 += (a[i] - average) * (a[i] - average);
        }
        variance = sum2 / len;
        return variance;
    }

    /**
     * 只遍历数组一次求方差，利用公式DX^2=EX^2-(EX)^2
     *
     * @param a
     * @return
     */
    public static double ComputeVariance2(double a[]) {
        double variance = 0;//方差
        double sum = 0, sum2 = 0;
        int i = 0, len = a.length;
        for (; i < len; i++) {
            sum += a[i];
            sum2 += a[i] * a[i];
        }
        variance = sum2 / len - (sum / len) * (sum / len);
        return variance;
    }

    /**
     * 只遍历数组一次求方差，利用公式DX^2=EX^2-(EX)^2
     *
     * @param a
     * @return
     */
    public static double ComputeVariance2(Object a[]) {
        double[] b = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = (double) a[i];
        }
        return ComputeVariance2(b);
    }


}
