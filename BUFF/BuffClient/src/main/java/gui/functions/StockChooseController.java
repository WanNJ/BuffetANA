package gui.functions;

import com.jfoenix.controls.*;
import gui.utils.DatePickerUtil;
import io.datafx.controller.FXMLController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import stockenum.StockPickIndex;
import stockenum.StockPool;
import stockenum.StrategyType;

import javax.annotation.PostConstruct;
import java.awt.event.ActionListener;

/**
 * @author zjy
 */
@FXMLController(value = "/resources/fxml/ui/StockChoose.fxml" , title = "StockChoose")
public class StockChooseController {

    @FXML private StackPane root;
    @FXML private JFXComboBox stockPool;//股票池
    @FXML private JFXComboBox plate;//板块
    @FXML private JFXComboBox industry;//行业
    @FXML private JFXCheckBox ST;//是否排除ST
    @FXML private JFXDatePicker from;//开始日期
    @FXML private JFXDatePicker to;//结束日期
    @FXML private FlowPane quotaPane;//选股指标面板
    @FXML private JFXTabPane pickingConditions;//选股条件TabPane
    @FXML private GridPane filterCondition;//筛选条件GridPane
    @FXML private GridPane rankingCondition;//排名条件GridPane
    @FXML private JFXComboBox<String> strategyType;//策略类型
    @FXML private JFXTextField holdingPeriod;//持仓期
    @FXML private JFXTextField formativePeriod;//形成期
    @FXML private JFXTextField numOfShares;//持股数（持股比例）
    @FXML private JFXButton start;//开始回测
    @FXML private JFXButton save;//保存策略
    /**
     * add by wsw
     * 保存那些 过滤指标的button
     */
    private JFXButton[] filterButtons;
    /**
     * add by wsw
     * 保存那些 排序指标的button
     */
    private JFXButton[] rankButtons;



    @PostConstruct
    public void init(){
        //初始化界面用到的各种控件
        from.setDialogParent(root);
        to.setDialogParent(root);
        //为日期选择器加上可选范围的控制
        DatePickerUtil.initDatePicker(from,to);

        addButtons();

        strategyType.setOnAction(event -> {
            if("均值策略".equals(strategyType.getValue())){
                numOfShares.setPromptText("持股数");
            }else if("动量策略".equals(strategyType.getValue())){
                numOfShares.setPromptText("持股比例");
            }
        });

        //设置股票池的选择
        stockPool.getItems().addAll
                (StockPool.All.toString(),StockPool.HS300.toString()
                        ,StockPool.UserMode.toString());

        //设置切换 tabpane监听
        pickingConditions.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                        if(newValue.getText().equals("筛选条件")){
                            quotaPane.getChildren().clear();
                            quotaPane.getChildren().addAll(filterButtons);
                        }else{
                            quotaPane.getChildren().clear();
                            quotaPane.getChildren().addAll(rankButtons);
                        }
                    }
                }
        );

    }







    /**
     * 添加选股指标的按钮
     */
    private void addButtons(){
        /**
         * add by wsw
         * 增加button的标签
         */
        filterButtons = new JFXButton[StockPickIndex.values().length];
        rankButtons = new JFXButton[StrategyType.values().length];

        //加载标签
        for (StockPickIndex stockPickIndex : StockPickIndex.values()){
            filterButtons[stockPickIndex.ordinal()] = new JFXButton(stockPickIndex.toString());
        }
        for (StrategyType strategyType : StrategyType.values()){
            rankButtons[strategyType.ordinal()] = new JFXButton(strategyType.toString());
        }

        //设置监听
        setButtonListener(filterButtons);
        setButtonListener(rankButtons);


        quotaPane.getChildren().addAll(filterButtons);
        //quotaPane.getChildren().addAll(rankButtons);

    }


    /**
     * add by wsw
     * 将添加的button的代码进行复用
     */
    private void setButtonListener(JFXButton[]  buttons){
        for(JFXButton button:buttons){
            button.setOnAction(event -> {
                //添加行
                RowConstraints rowConstraints=new RowConstraints();
                rowConstraints.setValignment(VPos.CENTER);

                //添加指标名称Label
                Label conditionName=new Label(button.getText());
                conditionName.setFont(Font.font(20));

                //添加删除按钮
                JFXButton delete=new JFXButton("");
                ImageView imageView=new ImageView(new Image("/resources/images/delete.png"));
                imageView.setFitHeight(20);
                imageView.setFitWidth(20);
                delete.setGraphic(imageView);

                if("筛选条件".equals(pickingConditions.getSelectionModel().getSelectedItem().getText())){
                    filterCondition.getRowConstraints().add(rowConstraints);
                    int row=filterCondition.getRowConstraints().size()-1;//行坐标

                    filterCondition.add(conditionName,0,row);

                    //添加比较符ComboBox和值TextField
                    JFXComboBox comparisonCharacter=new JFXComboBox();
                    comparisonCharacter.setValue("小于");
                    comparisonCharacter.getItems().addAll("小于","大于");
                    comparisonCharacter.setMaxWidth(100);
                    JFXTextField value=new JFXTextField("250");
                    value.setMaxWidth(100);
                    filterCondition.add(comparisonCharacter,1,row);
                    filterCondition.add(value,3,row);

                    delete.setOnAction(event1 -> {
                        filterCondition.getRowConstraints().remove(rowConstraints);
                        filterCondition.getChildren().removeAll(conditionName,comparisonCharacter,value,delete);
                    });
                    filterCondition.add(delete,4,row);



                }else if ("排名条件".equals(pickingConditions.getSelectionModel().getSelectedItem().getText())){
                    rankingCondition.getRowConstraints().add(rowConstraints);
                    int row=rankingCondition.getRowConstraints().size()-1;//行坐标

                    rankingCondition.add(conditionName,0,row);

                    //添加次序ComboBox、范围ComboBox和权重TextField
                    JFXComboBox order=new JFXComboBox();
                    order.setValue("从小到大");
                    order.getItems().addAll("从小到大","从大到小");
                    order.setMaxWidth(130);
                    JFXComboBox range=new JFXComboBox();
                    range.setValue("全部");
                    range.getItems().addAll("全部");
                    range.setMaxWidth(100);
                    JFXTextField weight=new JFXTextField("1");
                    weight.setMaxWidth(100);
                    rankingCondition.add(order,1,row);
                    rankingCondition.add(range,2,row);
                    rankingCondition.add(weight,3,row);

                    delete.setOnAction(event1 -> {
                        rankingCondition.getRowConstraints().remove(rowConstraints);
                        rankingCondition.getChildren().removeAll(conditionName,order,range,weight,delete);
                    });
                    rankingCondition.add(delete,4,row);


                }
            });
        }
    }

}
