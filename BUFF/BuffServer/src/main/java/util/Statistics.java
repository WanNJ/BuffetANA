package util;

import java.util.*;

/**
 * Created by Accident on 2017/3/9.
 */
public class Statistics {
    private List<Double> dataList;
    private int size;

    public Statistics(List<Double> data) {
        this.dataList = data;
        size = data.size();
    }

    public double getMean() {
        double sum = 0.0;
        for (double a : dataList)
            sum += a;
        return sum / size;
    }

    public double getVariance() {
        double mean = getMean();
        double temp = 0;
        for (double a : dataList)
            temp += (a - mean) * (a - mean);
        return temp / size;
    }

    public double getStdDev() {
        return Math.sqrt(getVariance());
    }
}

