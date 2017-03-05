package gui.functions;

import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import vo.DailyKLineVO;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Created by slow_time on 2017/3/4.
 */

@FXMLController(value = "/resources/fxml/ui/SingleStock.fxml" , title = "Single Stock")
public class SingleStockController {
    @FXML
    private BorderPane borderPane;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label openIndexLabel;
    @FXML
    private Label closeIndexLabel;
    @FXML
    private Label highIndexLabel;
    @FXML
    private Label lowIndexLabel;
    @FXML
    private Label volLabel;
    @FXML
    private Label adjCloseIndexLabel;
    @FXML
    private TableView<DailyKLineVO> stockDetailsTable;
    @FXML
    private TableColumn<DailyKLineVO, LocalDate> dateColum;
    @FXML
    private TableColumn<DailyKLineVO, Double> closeIndexColum;
    @FXML
    private TableColumn<DailyKLineVO, Double> rangeColum;


    private  Parent lineContent; // 包含所欲的画线内容

    @FXML
    private void initialize() {
        try {

            //加载四条线的节点信息
            lineContent = (Parent) FXMLLoader.load(getClass().getResource("/resources/fxml/ui/LinesPane.fxml"));
            borderPane.setCenter(lineContent);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
