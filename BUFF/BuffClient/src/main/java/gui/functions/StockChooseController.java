package gui.functions;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;

import javax.annotation.PostConstruct;

/**
 * @author zjy
 */
@FXMLController(value = "/resources/fxml/ui/StockChoose.fxml" , title = "StockChoose")
public class StockChooseController {

    @FXML private JFXComboBox stockPool;//股票池
    @FXML private JFXComboBox plate;//板块
    @FXML private JFXComboBox industry;//行业
    @FXML private FlowPane quotaPane;//选股指标面板
    @FXML private JFXTabPane pickingConditions;//选股条件TabPane
    @FXML private GridPane filterCondition;//筛选条件GridPane
    @FXML private GridPane rankingCondition;//排名条件GridPane

    @PostConstruct
    public void init(){
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
                    comparisonCharacter.setMaxWidth(100);
                    JFXTextField value=new JFXTextField("250");
                    value.setMaxWidth(100);
                    filterCondition.add(comparisonCharacter,1,row);
                    filterCondition.add(value,3,row);

                    delete.setOnAction(event1 -> filterCondition.getRowConstraints().remove(rowConstraints));
                    filterCondition.add(delete,4,row);
                }else if ("排名条件".equals(pickingConditions.getSelectionModel().getSelectedItem().getText())){
                    rankingCondition.getRowConstraints().add(rowConstraints);
                    int row=rankingCondition.getRowConstraints().size()-1;//行坐标

                    rankingCondition.add(conditionName,0,row);

                    //添加次序ComboBox、范围ComboBox和权重TextField
                    JFXComboBox order=new JFXComboBox();
                    order.setMaxWidth(100);
                    JFXComboBox range=new JFXComboBox();
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
