package util;

/**
 * Created by wshwbluebird on 2017/4/12.
 */
public class GuassY {
    public static double getGuassY(double maxn,  double loc , double bias2 ,double x){

        double po = -(x-loc)*(x-loc)/(2*bias2);
        double ep  =Math.exp(po);
        double pie =  ep/(Math.sqrt(Math.PI*2)*Math.sqrt(bias2));
        System.out.println("asd:   "+1/(Math.sqrt(Math.PI*2)*Math.sqrt(bias2)));
        return maxn*pie;
    }
}
