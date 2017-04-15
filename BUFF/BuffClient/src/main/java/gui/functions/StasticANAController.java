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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 统计的基础pane
 */
@FXMLController(value = "/resources/fxml/ui/StasticANA.fxml" , title = "test add in Grid Pane")
public class StasticANAController {


    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML BorderPane borderPane; //用于加载不同分析形式的 分析
    @FXML JFXTextField codeInput;  //股票代码输入
    @FXML Label codeName;  // 显示股票的名字
    @FXML JFXButton priceButton; //价格分布分析的按钮
    @FXML JFXButton relateButton;//相关性分析的按钮
    @FXML StackPane uproot;  //上面封装的stackpane
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

    /**
     * 存储加载线程时 生成的容器
     * add by wsw
     */
    private StackPane normPane;



    private String code; //股票代码

    @PostConstruct
    public void init() throws FlowException {

        /**
         * init pop up
         */
        popup.setPopupContainer(uproot);
        uproot.getChildren().remove(popup);

        stockList.getItems().clear();


        /**
         *  加载流控制
         */

        Flow normFlow = new Flow(NormANAController.class);

        normHandler = normFlow.createHandler(context);


        context.register("normHandler", normHandler);


        normPane = normHandler.start();




        this.code = null;


        codeInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                stockList.getItems().clear();
                List<String> list = CodeComplementUtil.CODE_COMPLEMENT_UTIL.getComplement(newValue);

                if(list==null || list.size()==0)
                    list.add("No Suggestion");


                popup.setSource(codeInput)  ;
                popup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 5, 33);

                for (String str:list) {
                    Label label = new Label(str);
                    label.setOnMouseClicked(e->{
                        if(!str.equals("No Suggestion"))
                            handleText(str);
                    });
                    stockList.getItems().add(label);
                }
            }
        });


        priceButton.setOnAction(event -> {
            if(code==null || "".equals(code)){
                Dialogs.showMessage("请输入股票的代码");
            }

            borderPane.getChildren().clear();
            borderPane.centerProperty().setValue(normPane);

            NormANAController normANAController =
                    (NormANAController)normHandler.getCurrentView().getViewContext().getController();
            normANAController.setCode(code);

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
