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

    public double getMedian() {
        dataList.sort((a, b) -> Double.compare(a, b));
        if (dataList.size() % 2 == 0) {
            return (dataList.get(size / 2 - 1) + dataList.get(size / 2)) / 2.0;
        }
        return dataList.get(size / 2);
    }

//    Test Code
//    public static void main(String[] args) {
//        ArrayList<Double> data = new ArrayList<Double>();
//        data.add(new Double(2));
//        data.add(new Double(2));
//        data.add(new Double(2));
//        data.add(new Double(2));
//        data.add(new Double(2));
//        data.add(new Double(2));
//
//        Statistics s = new Statistics(data);
//
//        System.out.println(s.getVariance());
//    }
}

