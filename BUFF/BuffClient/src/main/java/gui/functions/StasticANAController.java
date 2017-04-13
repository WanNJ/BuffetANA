package gui.functions;

/**
 * Created by wshwbluebird on 2017/4/13.
 */

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import gui.utils.CodeComplementUtil;
import io.datafx.controller.FXMLController;
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


    @FXML BorderPane borderPane; //用于加载不同分析形式的 分析
    @FXML JFXTextField codeInput;  //股票代码输入
    @FXML Label codeName;  // 显示股票的名字
    @FXML JFXButton priceButton; //价格分布分析的按钮
    @FXML StackPane uproot;  //上面封装的stackpane
    /**
     * 自动补全提示框
     */
    @FXML private JFXPopup popup;

    /**
     * 返回提示的列表
     */
    @FXML private JFXListView stockList;

    private String code; //股票代码

    @PostConstruct
    public void init(){

        this.code = null;
        /**
         * init pop up
         */
        popup.setPopupContainer(uproot);
        uproot.getChildren().remove(popup);


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



    }


    private void handleText(String str){
        String[] sep = str.split("\\(");
        String temp = sep[0];
        codeInput.setText(sep[0]);
        int len = sep[1].length()-1;
        codeName.setText(sep[1].substring(0,len));


        /**
         * not sure work
         */
        uproot.getChildren().remove(popup);
    }


}
