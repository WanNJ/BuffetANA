package gui.functions;

import com.jfoenix.controls.JFXDatePicker;
import gui.ChartController.ChartController;
import gui.ChartController.controller.TheVOLChartController;
import gui.ChartController.controller.UpDownChartController;
import gui.ChartController.pane.MutiChartPane;
import gui.ChartController.pane.TheVolPane;
import gui.utils.DatePickerUtil;
import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

/**
 * 市场温度计界面的控制器
 * @author zjy
 */
@FXMLController(value = "/resources/fxml/ui/Thermometer.fxml" , title = "Thermometer")
public class ThermometerController {

	@FXML private BorderPane volPane;
	@FXML private StackPane root;
	@FXML private JFXDatePicker from;
	@FXML private JFXDatePicker to;
	@FXML private BorderPane leastPane;

    @FXML private  BorderPane fivePane;
    @FXML private  BorderPane lastPane;

	@PostConstruct
	public void init(){

		//初始化界面用到的各种控件
		from.setDialogParent(root);
		to.setDialogParent(root);
		//为日期选择器加上可选范围的控制
		DatePickerUtil.initDatePicker(from,to);


		from.setValue(LocalDate.of(2014, 3, 1));
		to.setValue(LocalDate.of(2014, 3, 10));

		/**
		 *  为起始时间增加监听器
		 */
		from.setOnAction(event -> {
			handleTime();
		});

		/**
		 * 为结束时间增加监听器
		 */
		to.setOnAction(event -> {
			handleTime();
		});
		handleTime();
	}


	private void handleTime(){
		LocalDate first = from.getValue();
		LocalDate second = to.getValue();


//
//        first =LocalDate.of(2009,12,1);
//        second =LocalDate.of(2009,12,10);
//        //TODO delete

		if(first!=null && second!=null && first.isBefore(second)){
			upDateGraphVol(first,second);
			upDateGraphLeast(first,second);
		}

	}


	/**
	 * 监听器改图
	 * @param first
	 * @param second
	 */
	public void upDateGraphVol(LocalDate first , LocalDate second ){
		TheVOLChartController themometerVolBarChart = ChartController.INSTANCE.getTheVOLChartController();

		themometerVolBarChart.setStartDate(first);
		themometerVolBarChart.setEndDate(second);
		themometerVolBarChart.drawChat();
		TheVolPane theVolPane = new TheVolPane(themometerVolBarChart.getChart(),1.0);
		volPane.centerProperty().setValue(theVolPane);
	}

	/**
	 * 监听器改图
	 * @param first
	 * @param second
	 */
	public void upDateGraphLeast(LocalDate first , LocalDate second ){
		UpDownChartController upDownChartController = ChartController.INSTANCE.getUpDownChartController();

		upDownChartController.setStartDate(first);
		upDownChartController.setEndDate(second);
		upDownChartController.drawChat();
        MutiChartPane mutiChartPane = new MutiChartPane
                (upDownChartController.getBarChart(),upDownChartController.getLineChart(),1.0);

        MutiChartPane mutiChartPane5 = new MutiChartPane
                (upDownChartController.getBarChart5(),upDownChartController.getLineChart5(),1.0);

        MutiChartPane mutiChartPaneLast = new MutiChartPane
                (upDownChartController.getBarChartLast(),upDownChartController.getLineChartLast(),1.0);

        leastPane.centerProperty().setValue(mutiChartPane);
        fivePane.centerProperty().setValue(mutiChartPane5);
        lastPane.centerProperty().setValue(mutiChartPaneLast);
	}





}


	

