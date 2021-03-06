package util;

/**
 * Created by wshwbluebird on 2017/3/28.
 */
public class RunTimeSt {
    static long timestart = 0;

    static long timelast = 0;


    /**
     * 开始计时
     */
    public static void Start(){

        timestart = System.currentTimeMillis();
        timelast = timestart;
    }

    /**
     * 从开始 到当前这一阶段  执行力多少ms
     * @param phase
     */
    public static void getRunTime(String phase){
        long timecur = System.currentTimeMillis();
        if(timestart == 0){
            System.out.println("计时未开始");
        }else {
            System.out.println("运行到  " + phase + "阶段  花费： " + (timecur - timestart) + "ms");
        }
    }


    /**
     * 从上一阶段 到当前这一阶段  执行力多少ms
     * @param phase
     */
    public static void getRunTimeFromLast(String phase){
        long timecur = System.currentTimeMillis();
        if(timelast == 0){
            System.out.println("计时未开始");
        }else {
            System.out.println("运行到  " + phase + "阶段  花费： " + (timecur - timelast) + "ms");
            timelast = timecur;
        }
    }
}
