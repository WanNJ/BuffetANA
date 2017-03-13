package gui.functions;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.validation.ValidationFacade;
import gui.ChartController.ChartController;
import gui.ChartController.KLineChartController;
import gui.ChartController.TheVOLChartController;
import gui.ChartController.ThemometerVolBarChart;
import gui.utils.DatePickerUtil;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.util.VetoException;
import javafx.beans.value.ChangeListener;
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
		volPane.centerProperty().setValue(themometerVolBarChart.getChart());
	}


}


	

