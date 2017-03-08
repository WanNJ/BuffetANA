package vo;

import java.time.LocalDate;

/**
 * Created by slow_time on 2017/3/5.
 */
public class MAPieceVO {

    public double MA5;
    public double MA10;
    public double MA30;
    public double MA60;
    public double MA120;
    public double MA240;
    public  LocalDate date;


    public MAPieceVO() {

    }

    public MAPieceVO(double MA5, double MA10, double MA30, double MA60, double MA120, double MA240, LocalDate date) {
        this.MA5 = MA5;
        this.MA10 = MA10;
        this.MA30 = MA30;
        this.MA60 = MA60;
        this.MA120 = MA120;
        this.MA240 = MA240;
        this.date = date;
    }
}
