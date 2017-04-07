package gui.functions;

import com.jfoenix.controls.*;
import gui.utils.DatePickerUtil;
import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import javax.annotation.PostConstruct;

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
    }

    /**
     * 添加选股指标的按钮
     */
    private void addButtons(){
        JFXButton[] buttons={new JFXButton("指标1"),new JFXButton("指标2"),new JFXButton("指标3"),
                new JFXButton("指标4"),new JFXButton("指标5")};
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
                    comparisonCharacter.setMaxWidth(100);
                    JFXTextField value=new JFXTextField("250");
                    value.setMaxWidth(100);
                    filterCondition.add(comparisonCharacter,1,row);
                    filterCondition.add(value,3,row);

                    delete.setOnAction(event1 -> {
                        filterCondition.getRowConstraints().remove(rowConstraints);
                    });
                    filterCondition.add(delete,4,row);
                }else if ("排名条件".equals(pickingConditions.getSelectionModel().getSelectedItem().getText())){
                    rankingCondition.getRowConstraints().add(rowConstraints);
                    int row=rankingCondition.getRowConstraints().size()-1;//行坐标

                    rankingCondition.add(conditionName,0,row);

                    //添加次序ComboBox、范围ComboBox和权重TextField
                    JFXComboBox order=new JFXComboBox();
                    order.setValue("从小到大");
                    order.setMaxWidth(100);
                    JFXComboBox range=new JFXComboBox();
                    range.setValue("全部");
                    range.setMaxWidth(100);
                    JFXTextField weight=new JFXTextField("1");
                    weight.setMaxWidth(100);
                    rankingCondition.add(order,1,row);
                    rankingCondition.add(range,2,row);
                    rankingCondition.add(weight,3,row);

                    delete.setOnAction(event1 -> rankingCondition.getRowConstraints().remove(rowConstraints));
                    rankingCondition.add(delete,4,row);
                }
            });
        }
        quotaPane.getChildren().addAll(buttons);

    }

}
