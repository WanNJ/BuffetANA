package gui.functions;

import blservice.statistics.SingleCodePredictService;
import blserviceimpl.statistics.SingleCodePredictServiceImpl;
import gui.ChartController.ChartController;
import gui.ChartController.controller.NormHistChartController;
import gui.ChartController.pane.NormHistPane;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import vo.NormalStasticVO;

import javax.annotation.PostConstruct;

/**
 * Created by wshwbluebird on 2017/4/13.
 */


@FXMLController(value = "/resources/fxml/ui/NormANA.fxml" , title = "test add in Grid Pane")
public class NormANAController {

    @FXML Label kurtosis; // 峰度
    @FXML Label isNorm;  //可否拟合
    @FXML Label mean;   //可否拟合
    @FXML Label sigma;  //可否拟合
    @FXML Label recIn;  //推荐入手价格
    @FXML Label recOut;  // 推荐出售价格
    @FXML BorderPane normHistPane ;  // 画直方图的图

    private SingleCodePredictService singleCodePredictService;

    public void setSingleCodePredictService(SingleCodePredictService singleCodePredictService){
        this.singleCodePredictService = singleCodePredictService;
    }

    @PostConstruct
    public void init(){
        //TODO  这种注入方式不对
        this.singleCodePredictService = SingleCodePredictServiceImpl.SINGLE_CODE_PREDICT;

    }

    public void setCode(String code){
        NormalStasticVO normalStasticVO = singleCodePredictService.getNormalStasticVO(code);
        NormHistChartController normHistChartController = ChartController.INSTANCE.getNormHistChartController();

        normHistChartController.setGuassLineVOs(normalStasticVO.guessLine);
        normHistChartController.setRanges(normalStasticVO.normalHist);

        normHistChartController.drawChat();

        NormHistPane histPane = new NormHistPane(normHistChartController.getChart(),1.0);
        normHistPane.centerProperty().setValue(histPane);

        kurtosis.setText(String.valueOf(normalStasticVO.kurtosis));

    }

}
