package util;

/**
 * Created by wshwbluebird on 2017/4/12.
 */
public class RangeF {
    //直方图的右分割值
    public double big;
    //直方图的作分割值
    public double small;

    //直方图统计数目
    public long cnt;

    public RangeF(){

    }

    public RangeF(double small , double big){
        this.big  = big;
        this.small = small;
        this.cnt = 0;
    }
}
