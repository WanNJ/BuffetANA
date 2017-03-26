package gui.functions;

import blservice.singlestock.AllStockService;
import blservice.singlestock.StockDetailService;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import factory.BLFactorySeviceOnlyImpl;
import factory.BlFactoryService;
import gui.utils.Dialogs;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import vo.StockBriefInfoVO;
import vo.StockDetailVO;
import vo.StockNameAndCodeVO;

import javax.annotation.PostConstruct;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

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
    private Label stockNameLabel;
    @FXML
    private Label stockCodeLabel;
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
    @FXML
    private JFXTreeTableView stocksTable;
    @FXML
    private JFXTextField search;

    
    private  Parent lineContent; // 包含所欲的画线内容

    private FlowHandler  LineHandler;

    private StockDetailService stockDetailService;
    private AllStockService allStockService;
    private BlFactoryService factory;
    private String code;
    private ObservableList<StockBriefInfoVO> stockBriefInfoVOs = FXCollections.observableArrayList();
    private StockChangeController stockChangeController;

    /**
     * add by wsw
     * 用来存储子视图的控制器
     */
    private LinesPanelController linesPanelController;


    @PostConstruct
    public void init() throws FlowException, VetoException {
        context.register(this);


        //初始化所要用到的逻辑层接口
        factory = new BLFactorySeviceOnlyImpl();
        stockDetailService = factory.createStockDetailService();
        allStockService =factory.createAllStockService();

        //初始化JFXTreeTableView
        stockChangeController=new StockChangeController();
        List<StockNameAndCodeVO> list= null;
        try {
            list = allStockService.getAllStock();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        stockChangeController.initTreeTableView(stocksTable,search,list,ID -> setStockInfo(ID));



        datePicker.setDialogParent(root);

        context = new ViewFlowContext();
        // set the default controller
        Flow innerFlow = new Flow(LinesPanelController.class);

        LineHandler = innerFlow.createHandler(context);
        context.register("LineHandler", LineHandler);
        borderPane.setCenter(LineHandler.start());

        this.linesPanelController = (LinesPanelController) LineHandler.getCurrentView().getViewContext().getController();


        //初始化界面数据
        this.setStockInfo("1");

        //初始化界面用到的各种控件
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        closeIndexColumn.setCellValueFactory(cellData -> cellData.getValue().closePriceProperty());
        rangeColumn.setCellValueFactory(cellData -> cellData.getValue().rangeProperty());

        //将涨跌幅用颜色区分开来，涨幅用红色表示，跌幅用绿色表示
        rangeColumn.setCellFactory(column -> {
            return new TableCell<StockBriefInfoVO, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        System.out.println("hhh");
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        if (item.startsWith("-"))
                            this.setTextFill(Color.GREEN);
                        else
                            this.setTextFill(Color.RED);
                    }
                }
            };
        });


        stockDetailsTable.setItems(this.stockBriefInfoVOs);

        //为股票简要信息列表绑定监听器
        stockDetailsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    showStockDetails(newValue.dateProperty().get());
                    datePicker.setValue(newValue.dateProperty().get());
                }
        );

        //为日期选择器绑定监听器
        datePicker.setOnAction(event -> {
            LocalDate date = datePicker.getValue();
            showStockDetails(date);
        });
    }

    /**
     * 设置当前显示的股票
     * @param code 股票代码
     */
    public void setStockInfo(String code) {
        stockChangeController.select(code);

        this.code = code;
        if(stockBriefInfoVOs.size() >= 1)
            this.stockBriefInfoVOs.remove(0, stockBriefInfoVOs.size());
        try {
            stockBriefInfoVOs.addAll(stockDetailService.getStockBriefInfo(code));
            stockBriefInfoVOs.sort((stockBriefInfoVO1, stockBriefInfoVO2) -> {
                if(stockBriefInfoVO1.date.isEqual(stockBriefInfoVO2.date))
                    return 0;
                else
                    return stockBriefInfoVO1.date.isBefore(stockBriefInfoVO2.date) ? 1 : -1;
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //如果该code存在数据，则将最近一天的details显示出来，若该code对应的股票无数据，则提示用户
        if(stockBriefInfoVOs != null && stockBriefInfoVOs.size() >= 1) {
            stockNameLabel.setText(stockBriefInfoVOs.get(0).name);
            stockCodeLabel.setText(code);
            showStockDetails(stockBriefInfoVOs.get(0).date);
        }
        else {
            Dialogs.showMessage("Error", "code为" + code + "的股票无数据");
        }
        showAllGragh();
    }



    private void showStockDetails(LocalDate date) {
        StockDetailVO stockDetailVO = null;
        try {
            stockDetailVO = stockDetailService.getSingleStockDetails(code, date);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if(stockDetailVO == null)
            Dialogs.showMessage("Error", "code为 " + code + " 的股票\n在 " + date+ " 这一天无数据");
        else {
            openIndexLabel.setText(String.valueOf(stockDetailVO.openPrice));
            closeIndexLabel.setText(String.valueOf(stockDetailVO.closePrice));
            highIndexLabel.setText(String.valueOf(stockDetailVO.highPrice));
            lowIndexLabel.setText(String.valueOf(stockDetailVO.lowPrice));
            volLabel.setText(String.valueOf(stockDetailVO.vol));
            adjCloseIndexLabel.setText(String.valueOf(stockDetailVO.adjCloseIndex));
            datePicker.setValue(date);
        }
    }

    /**
     * add by wsw
     * 通过加载股票的代码  调用下层界面的空一起进行画图
     * 时间已有默认的值
     * 监听时间变化 在子controller里 ，无需在这里设置
     */
    private void showAllGragh(){
        if(this.code!=null  && !"".equals(this.code)){
          linesPanelController.HandleCode(this.code);
        }else{
            System.err.println("no code information");
        }
    }

}
