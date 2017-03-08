package gui.functions;

import blservice.singlestock.StockDetailService;
import com.jfoenix.controls.JFXDatePicker;
import factory.BlFactoryService;
import factory.BlFactoryServiceImpl;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import vo.StockBriefInfoVO;
import vo.StockDetailVO;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

/**
 * Created by slow_time on 2017/3/4.
 */

@FXMLController(value = "/resources/fxml/ui/SingleStock.fxml" , title = "Single Stock")
public class SingleStockController {

    @FXMLViewFlowContext
    private ViewFlowContext context;

    //@FXML private JFXDrawer drawer;

    @FXML
    private StackPane root;
    @FXML
    private BorderPane borderPane;
    @FXML
    private StackPane detailsPane;
    @FXML
    private JFXDatePicker datePicker;
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
    private TableView<StockBriefInfoVO> stockDetailsTable;
    @FXML
    private TableColumn<StockBriefInfoVO, LocalDate> dateColum;
    @FXML
    private TableColumn<StockBriefInfoVO, Number> closeIndexColum;
    @FXML
    private TableColumn<StockBriefInfoVO, Number> rangeColum;


    
    private  Parent lineContent; // 包含所欲的画线内容

    private FlowHandler  LineHandler;

    private StockDetailService stockDetailService;
    private BlFactoryService factory;
    private String code;

    @PostConstruct
    public void init() throws FlowException, VetoException {

        //初始化所要用到的逻辑层接口
        factory = new BlFactoryServiceImpl();
        stockDetailService = factory.createStockDetailService();

        //初始化界面用到的各种控件
        datePicker.setDialogParent(root);

        context = new ViewFlowContext();
        // set the default controller
        Flow innerFlow = new Flow(LinesPanelController.class);

        LineHandler = innerFlow.createHandler(context);
        context.register("LineHandler", LineHandler);
        //drawer.setContent(LineHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));
        borderPane.setCenter(LineHandler.start());

        //为日期选择器绑定监听器
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                showStockDetails(item);
                            }
                        };
                    }
                };
        datePicker.setDayCellFactory(dayCellFactory);
        //为股票简要信息列表绑定监听器
        stockDetailsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showStockDetails(newValue.date.get())
        );
    }

    public void setStockCode(String code) {
        this.code = code;
    }

    private void showStockDetails(LocalDate date) {
        StockDetailVO stockDetailVO = stockDetailService.getSingleStockDetails(code, date);
        openIndexLabel.setText(String.valueOf(stockDetailVO.openPrice));
        closeIndexLabel.setText(String.valueOf(stockDetailVO.closePrice));
        highIndexLabel.setText(String.valueOf(stockDetailVO.highPrice));
        lowIndexLabel.setText(String.valueOf(stockDetailVO.lowPrice));
        volLabel.setText(String.valueOf(stockDetailVO.vol));
        adjCloseIndexLabel.setText(String.valueOf(stockDetailVO.adjCloseIndex));
    }

    private void showStockBriefInfo() {
        ObservableList<StockBriefInfoVO> stockBriefInfoVOs = stockDetailService.getStockBriefInfo(code);
        stockDetailsTable.setItems(stockBriefInfoVOs);
        dateColum.setCellValueFactory(cellData -> cellData.getValue().date);
        closeIndexColum.setCellValueFactory(cellData -> cellData.getValue().closePrice);
        rangeColum.setCellValueFactory(cellData -> cellData.getValue().range);
    }
}
