package bldriver;

import org.apache.commons.math3.analysis.integration.gauss.GaussIntegrator;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.fitting.GaussianCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import util.GuassY;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/4/12.
 */
public class CurveFittingTest {
    public static void main(String[] args) {
    WeightedObservedPoints obs = new WeightedObservedPoints();

        obs.add(4.0254623,  531026.0);
        obs.add(4.03128248, 984167.0);
        obs.add(4.03839603, 1887233.0);
        obs.add(4.04421621, 2687152.0);
        obs.add(4.05132976, 3461228.0);
        obs.add(4.05326982, 3580526.0);
        obs.add(4.05779662, 3439750.0);
        obs.add(4.0636168,  2877648.0);
        obs.add(4.06943698, 2175960.0);
        obs.add(4.07525716, 1447024.0);
        obs.add(4.08237071, 717104.0);
        obs.add(4.08366408, 620014.0);

        List<Double> list = new ArrayList<>();
        for(int i = 0 ; i <12 ; i++){
            list.add(obs.toList().get(i).getX());
        }

    double[] parameters = GaussianCurveFitter.create().fit(obs.toList());
        System.out.println(parameters.length);
        for (double i : parameters) {
            System.out.println(i);
        }

        NormalDistribution normal = new NormalDistribution(parameters[1], Math.sqrt(parameters[2]));
        double norm  = parameters[0]/normal.density(parameters[1]);
        list.stream().forEach(t-> System.out.println(t+"   "+normal.density(t)*norm));


    }



}
