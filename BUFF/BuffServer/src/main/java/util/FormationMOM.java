package util;

import java.time.LocalDate;

/**
 * 用来封装某一支股票形成期内的收益情况的VO类
 * Created by slow_time on 2017/3/29.
 */
public class FormationMOM {
    /**
     * 具体哪一天的收益
     */
    public LocalDate date;

    /**
     * 收益率
     */
    public double yeildRate;


    public FormationMOM(LocalDate date, double yeildRate) {
        this.date = date;
        this.yeildRate = yeildRate;
    }

    /**
     * Used for cloning
     * @param formationMOM
     */
    public FormationMOM(FormationMOM formationMOM) {
        this.date = formationMOM.date;
        this.yeildRate = formationMOM.yeildRate;
    }
}
