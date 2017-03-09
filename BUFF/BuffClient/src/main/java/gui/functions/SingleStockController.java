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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import vo.StockBriefInfoVO;
import vo.StockDetailVO;

import javax.annotation.PostConstruct;
import java.rmi.RemoteException;
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
    private TableColumn<StockBriefInfoVO, LocalDate> dateColumn;
    @FXML
    private TableColumn<StockBriefInfoVO, Number> closeIndexColumn;
    @FXML
    private TableColumn<StockBriefInfoVO, String> rangeColumn;


    
    private  Parent lineContent; // 包含所欲的画线内容

    private FlowHandler  LineHandler;

    private StockDetailService stockDetailService;
    private BlFactoryService factory;
    private String code;
    private ObservableList<StockBriefInfoVO> stockBriefInfoVOs = FXCollections.observableArrayList();


    @PostConstruct
    public void init() throws FlowException, VetoException {

        //初始化所要用到的逻辑层接口
        factory = new BlFactoryServiceImpl();
        stockDetailService = factory.createStockDetailService();

        //初始化界面用到的各种控件

        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        closeIndexColumn.setCellValueFactory(cellData -> cellData.getValue().closePriceProperty());
        rangeColumn.setCellValueFactory(cellData -> cellData.getValue().rangeProperty());

        //将涨跌幅用颜色区分开来，涨幅用红色表示，跌幅用绿色表示
//        rangeColumn.setCellFactory(column -> {
//            return new TableCell<StockBriefInfoVO, String>() {
//                @Override
//                protected void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty);
//
//                    if (item == null || empty) {
//                        System.out.println("hhh");
//                        setText(null);
//                        setStyle("");
//                    } else {
//                        setItem(item);
//                        if (item.startsWith("-"))
//                            this.setTextFill(Color.GREEN);
//                        else
//                            this.setTextFill(Color.RED);
//                    }
//                }
//            };
//        });
        stockDetailsTable.setItems(this.stockBriefInfoVOs);

        //为股票简要信息列表绑定监听器
        stockDetailsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    showStockDetails(newValue.dateProperty().get());
                    datePicker.setValue(newValue.dateProperty().get());
                }
        );

        datePicker.setDialogParent(root);

        context = new ViewFlowContext();
        // set the default controller
        Flow innerFlow = new Flow(LinesPanelController.class);

        LineHandler = innerFlow.createHandler(context);
        context.register("LineHandler", LineHandler);
        //drawer.setContent(LineHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));
        borderPane.setCenter(LineHandler.start());


        //为日期选择器绑定监听器
        datePicker.setOnAction(event -> {
            LocalDate date = datePicker.getValue();
            showStockDetails(date);
        });

        //初始化界面数据
        datePicker.setValue(LocalDate.of(2014, 4, 29));
        this.setStockInfo("1");
    }

    public void setStockInfo(String code) {
        this.code = code;
        this.stockBriefInfoVOs.removeAll();
        try {
            stockDetailService.getStockBriefInfo(code).forEach(stockBriefInfoVO -> {
                this.stockBriefInfoVOs.add(stockBriefInfoVO);
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        showStockDetails(LocalDate.of(2014, 4, 29));
    }



    private void showStockDetails(LocalDate date) {
        StockDetailVO stockDetailVO = null;
        try {
            stockDetailVO = stockDetailService.getSingleStockDetails(code, date);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if(stockDetailVO == null)
            //之后会替换成一个弹框提示用户
            System.out.println("所选时间没有数据");
        else {
            openIndexLabel.setText(String.valueOf(stockDetailVO.openPrice));
            closeIndexLabel.setText(String.valueOf(stockDetailVO.closePrice));
            highIndexLabel.setText(String.valueOf(stockDetailVO.highPrice));
            lowIndexLabel.setText(String.valueOf(stockDetailVO.lowPrice));
            volLabel.setText(String.valueOf(stockDetailVO.vol));
            adjCloseIndexLabel.setText(String.valueOf(stockDetailVO.adjCloseIndex));
        }
    }
}
