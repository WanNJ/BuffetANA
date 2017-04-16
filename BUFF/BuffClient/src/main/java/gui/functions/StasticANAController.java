package gui.functions;

/**
 * Created by wshwbluebird on 2017/4/13.
 */

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import gui.utils.CodeComplementUtil;
import gui.utils.Dialogs;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 统计的基础pane
 */
@FXMLController(value = "/resources/fxml/ui/StasticANA.fxml" , title = "test add in Grid Pane")
public class StasticANAController {
    @FXMLViewFlowContext private ViewFlowContext context;

    @FXML private VBox vBox;
    @FXML private JFXTextField codeInput;  //股票代码输入
    @FXML private Label codeName;  // 显示股票的名字
    @FXML private JFXButton priceButton; //价格分布分析的按钮
    @FXML private JFXButton correlationButton;//相关性分析的按钮
    @FXML private StackPane uproot;  //上面封装的stackpane

    /**
     * 自动补全提示框
     */
    @FXML private JFXPopup popup;

    /**
     * 返回提示的列表
     */
    @FXML private JFXListView stockList;


    /**
     *画图的handler
     */
    private FlowHandler normHandler;
    private StackPane normPane;//存储加载线程时 生成的容器
    private FlowHandler correlationHandler;
    private StackPane correlationPane;
    private String code; //股票代码

    @PostConstruct
    public void init() throws FlowException {

        /**
         * init pop up
         */
        popup.setPopupContainer(uproot);

        stockList.getItems().clear();

        this.code = null;

        //添加codeInput的监听
        codeInput.textProperty().addListener(((observable, oldValue, newValue) -> {
            stockList.getItems().clear();
            List<String> list = CodeComplementUtil.CODE_COMPLEMENT_UTIL.getComplement(newValue);

            if(list==null || list.size()==0)
                list.add("No Suggestion");

            popup.setSource(codeInput)  ;
            popup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 10, 45);

            for (String str:list) {
                Label label = new Label(str);
                label.setOnMouseClicked(e->{
                    if(!str.equals("No Suggestion"))
                        handleText(str);
                });
                stockList.getItems().add(label);
            }
        }));

        //添加priceButton的监听
        priceButton.setOnAction(event -> {
            if(code==null || "".equals(code)){
                Dialogs.showMessage("请输入股票的代码");
                return;
            }

            /**
             *  加载流控制
             */
            vBox.getChildren().remove(normPane);
            Flow normFlow = new Flow(NormANAController.class);
            normHandler = normFlow.createHandler(context);
            try {
                normPane = normHandler.start();
            } catch (FlowException e) {
                e.printStackTrace();
            }
            vBox.getChildren().add(normPane);

            NormANAController normANAController =
                    (NormANAController)normHandler.getCurrentView().getViewContext().getController();
            normANAController.setCode(code);

        });

        correlationButton.setOnAction(event -> {
            if(code==null || "".equals(code)){
                Dialogs.showMessage("请输入股票的代码");
                return;
            }

            /**
             *  加载流控制
             */
            vBox.getChildren().remove(correlationPane);
            Flow correlationFlow = new Flow(IndustryCorrelationController.class);
            correlationHandler = correlationFlow.createHandler(context);
            try {
                correlationPane = correlationHandler.start();
            } catch (FlowException e) {
                e.printStackTrace();
            }
            vBox.getChildren().add(correlationPane);
            IndustryCorrelationController industryCorrelationController =
                    (IndustryCorrelationController)correlationHandler.getCurrentView().getViewContext().getController();
            industryCorrelationController.setSelectedCode(code);
        });
    }

    private void handleText(String str){
        String[] sep = str.split("\\(");
        String temp = sep[0];
        codeInput.setText(sep[0]);
        this.code = sep[0];
        int len = sep[1].length()-1;
        codeName.setText(sep[1].substring(0,len));


        /**
         * not sure work
         */
        uproot.getChildren().remove(popup);
    }


}
