package bldriver;

import blserviceimpl.statistics.SingleCodePredictServiceImpl;
import vo.NormalStasticVO;

/**
 * Created by wshwbluebird on 2017/4/13.
 */
public class NormDriver {
    public static void main(String[] args) {

        NormalStasticVO normalStasticVO = SingleCodePredictServiceImpl.SINGLE_CODE_PREDICT.getNormalStasticVO("000004");

        normalStasticVO.guessLine.forEach(t-> System.out.println(t.value));
    }

}
