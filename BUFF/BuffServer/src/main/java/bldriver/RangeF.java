package bldriver;

/**
 * Created by wshwbluebird on 2017/4/12.
 */
public class RangeF {
    public double big;
    public double small;
    public long cnt;

    public RangeF(){

    }

    public RangeF(double small , double big){
        this.big  = big;
        this.small = small;
        this.cnt = 0;
    }
}
