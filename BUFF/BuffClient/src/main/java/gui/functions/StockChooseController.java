package gui.functions;

import SinglePort.SingleBlService;
import blservice.strategy.StrategyService;
import com.jfoenix.controls.*;
import exception.WrongValueException;
import factory.BLFactorySeviceOnlyImpl;
import factory.BlFactoryService;
import gui.utils.DatePickerUtil;
import gui.utils.Dialogs;
import io.datafx.controller.FXMLController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import jdk.nashorn.internal.runtime.options.Option;
import stockenum.StockPickIndex;
import stockenum.StockPool;
import stockenum.StrategyType;
import util.StrategyScoreVO;
import vo.*;

import javax.annotation.PostConstruct;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

/**
 * @author zjy
 */


//TODO 添加数值监听器
@FXMLController(value = "/resources/fxml/ui/StockChoose.fxml" , title = "StockChoose")
public class StockChooseController {

    @FXML private StackPane root;
    @FXML private JFXComboBox<String> stockPool;//股票池
    @FXML private JFXComboBox<String> plate;//板块
    @FXML private JFXComboBox<String> industry;//行业
    @FXML private JFXCheckBox ST;//是否排除ST
    @FXML private JFXDatePicker from;//开始日期
    @FXML private JFXDatePicker to;//结束日期
    @FXML private FlowPane quotaPane;//选股指标面板
    @FXML private JFXTabPane pickingConditions;//选股条件TabPane
    @FXML private GridPane filterCondition;//筛选条件GridPane
    @FXML private GridPane rankingCondition;//排名条件GridPane
    @FXML private JFXComboBox<String> strategyType;//策略类型
    @FXML private JFXTextField holdingPeriod;//持仓期
    @FXML private JFXTextField formativePeriod;//形成期／几日均线
    @FXML private JFXTextField numOfShares;//持股数（持股比例）
    @FXML private JFXButton start;//开始回测
    @FXML private JFXButton save;//保存策略


    private StrategyService strategyService;

    private BlFactoryService blFactoryService;

    /**
     * 外部注入工厂
     * @param blFactoryService
     */
    public void setBlFactoryService(BlFactoryService blFactoryService){
        this.blFactoryService = blFactoryService;
    }

    /**
     * 外部注入bl
     * @param strategyService
     */
    public void setStrategyService(StrategyService strategyService){
        this.strategyService = strategyService;
    }

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

    /**
     * 用于标记  均值策略和动量策略 是否已经被选中
     */
    public static boolean strateyChoosed;






    //记录股票池的选择信息
    private StockPoolConditionVO stockPoolConditionVO;


    //记录回测的开始日期
    private LocalDate begin;

    // 记录回测的结束日期
    private  LocalDate end;

    private  List<MixedStrategyVO> mixedStrategyVOList;

    private  List<StockPickIndexVO> stockPickIndexList;

    private StrategyConditionVO strategyConditionVO;

    private TraceBackVO traceBackVO;



    @PostConstruct
    public void init(){

        blFactoryService = new BLFactorySeviceOnlyImpl();

        strategyService = blFactoryService.createStrategyService();
        //init all the VO value

        stockPoolConditionVO = new StockPoolConditionVO();

        mixedStrategyVOList = new ArrayList<>();

        stockPickIndexList = new ArrayList<>();


        //初始化界面用到的各种控件
        from.setDialogParent(root);
        to.setDialogParent(root);
        //为日期选择器加上可选范围的控制
        DatePickerUtil.initDatePicker(from,to);
        strateyChoosed = false;


        from.setValue(LocalDate.of(2013,1,1));
        to.setValue(LocalDate.of(2014,1,1));

        begin = LocalDate.of(2013,1,1);
        end = LocalDate.of(2014,1,1);


        //获取日期
        from.setOnAction(event -> {
            if(from.getValue().isBefore(LocalDate.of(2012,1,1))){
                Dialogs.showMessage("回测时间不支持", "起始时间不支持早于2012年");
            }
            begin = from.getValue();
        });

        to.setOnAction(event -> {
            end = to.getValue();
        });



        addButtons();



        strategyType.getItems().clear();
        strategyType.getItems().addAll("均值策略","动量策略","自定义策略");


        /**
         * add by wsw
         * 对策略类型选择的说明
         * 如果选择  均值策略或者动量策略
         * 调用  给予优化的方法
         *        显示  formativePeriod;//形成期／几日均线
         *       不显示  rank的标签
         *       给定一个策略模式  且不可以删除
         *       不在 GridPane rankingCondition;//排名条件GridPane 显示形成期如果选择  均值策略或者动量策略
         *
         * 如果选择  自定义策略
         * 调用  给予混合策略的方法
         *       不显示  formativePeriod;//形成期／几日均线
         *       显示  rank的标签
         *       可以选择多个策略模式   且可以删除
         *       在 GridPane rankingCondition;//排名条件GridPane 显示形成期
         *
         *
         */

        strategyType.setValue("自定义策略");
        formativePeriod.setVisible(false);

        strategyType.setOnAction(event -> {
            if("均值策略".equals(strategyType.getValue())){
                formativePeriod.setText(null);
                numOfShares.setPromptText("持股数");
                formativePeriod.setPromptText("几日均线");
                strateyChoosed = true;
                formativePeriod.setVisible(true);
                /**
                 * 自动在rank指标里修改是几日均线
                 */
                formativePeriod.setOnAction(e -> {
                    for(Node node : rankingCondition.getChildren()){
                        if(node instanceof Label){
                            if(((Label) node).getText().endsWith("均线偏离")){
                                String days = formativePeriod.getText();
                                ((Label) node).setText(days+"日均线偏离");
                            }
                        }
                    }
                });

                if("排名条件".equals(pickingConditions.getSelectionModel().getSelectedItem().getText())){
                    quotaPane.getChildren().clear();;
                }
                chooseStrategy(StrategyType.MA.toString());

            }else if("动量策略".equals(strategyType.getValue())){
                formativePeriod.setText(null);
                formativePeriod.setVisible(true);
                numOfShares.setPromptText("持股比例");
                formativePeriod.setPromptText("形成期");
                strateyChoosed = true;
                chooseStrategy(StrategyType.MOM.toString());
                if("排名条件".equals(pickingConditions.getSelectionModel().getSelectedItem().getText())){
                    quotaPane.getChildren().clear();;
                }
            }else if("自定义策略".equals(strategyType.getValue())){
                formativePeriod.setText(null);
                strateyChoosed = false;
                formativePeriod.setVisible(false);
                rankingCondition.getChildren().clear();
                numOfShares.setPromptText("持股数");
                formativePeriod.setPromptText("形成期");

                if("排名条件".equals(pickingConditions.getSelectionModel().getSelectedItem().getText())){
                    quotaPane.getChildren().clear();;
                    quotaPane.getChildren().addAll(rankButtons);
                }


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
                            if(!strateyChoosed)
                            quotaPane.getChildren().addAll(rankButtons);
                        }
                    }
                }
        );


        /**
         * 股票池选择的监听
         */
        stockPool.setOnAction(event -> {
            if("全部".equals(stockPool.getValue())){
                plate.getItems().clear();
                industry.getItems().clear();
                plate.getItems().add("无");
                industry.getItems().add("无");
            }else if("沪深300".equals(stockPool.getValue())){
                plate.getItems().clear();
                industry.getItems().clear();
                plate.getItems().add("无");
                industry.getItems().add("无");
            }else{
                //TODO  用户自定义模式   现在不知道 多选怎么实现
            }
        });


        /**
         * 开始回测
         */
        start.setOnAction(event -> {
            mixedStrategyVOList.clear();
            stockPickIndexList.clear();
            try {
                collectCurrentData();
            } catch (WrongValueException e) {
                Dialogs.showMessage(e.getErr());
            }

            if(strateyChoosed){
                strategyService.init(strategyConditionVO,stockPoolConditionVO,stockPickIndexList);
                strategyService.calculate(traceBackVO);
            }else{
                strategyService.initMixed(begin,end,stockPoolConditionVO,
                        stockPickIndexList,traceBackVO,mixedStrategyVOList);
            }


            BackDetailVO backDetailVO = strategyService.getBackDetailVO();



            System.out.println("alpha: " + backDetailVO.alpha);
            System.out.println("beta: " + backDetailVO.beta);
            System.out.println("yearProfitRate: " + backDetailVO.yearProfitRate);
            System.out.println("baseYearProfitRate: " + backDetailVO.baseYearProfitRate);
            System.out.println("sharpRate: " + backDetailVO.sharpRate);
            System.out.println("largestBackRate: " + backDetailVO.largestBackRate);

            StrategyScoreVO strategyScoreVO = strategyService.getStrategyEstimateResult();
            System.out.println("盈利能力: " + strategyScoreVO.profitAbility);
            System.out.println("稳定性: " + strategyScoreVO.stability);
            System.out.println("选股能力: " + strategyScoreVO.chooseStockAbility);
            System.out.println("绝对收益: " + strategyScoreVO.absoluteProfit);
            System.out.println("抗风险能力: " + strategyScoreVO.antiRiskAbility);
            System.out.println("策略总得分: " + strategyScoreVO.strategyScore);

        });








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


        quotaPane.getChildren().addAll(filterButtons);;

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


//                    JFXComboBox range=new JFXComboBox();
//                    range.setValue("全部");
//                    range.getItems().addAll("全部");
//                    range.setMaxWidth(100);


                    JFXTextField range=new JFXTextField();
                    range.setPromptText("形成天数");
                    if(strateyChoosed){
                        range.setVisible(false);
                    }

                    JFXTextField weight=new JFXTextField();
                    if(strateyChoosed){
                        weight.setPromptText("1");
                        weight.editableProperty().set(false);
                    }else{
                        weight.setPromptText("权重");
                    }

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


    /**
     * 选定了 是均值策略还是动量策略
     * @param stategyName
     */
    private  void  chooseStrategy(String stategyName){
        rankingCondition.getChildren().clear();
        RowConstraints rowConstraints=new RowConstraints();
        rankingCondition.getRowConstraints().add(rowConstraints);
        int row=rankingCondition.getRowConstraints().size()-1;//行坐标

        Label conditionName=new Label(stategyName);
        rankingCondition.add(conditionName,0,row);

        //添加次序ComboBox、范围ComboBox和权重TextField
        JFXComboBox order=new JFXComboBox();
        order.setValue("从小到大");
        order.getItems().addAll("从小到大","从大到小");
        order.setMaxWidth(130);
        JFXComboBox range=new JFXComboBox();
        range.setValue("全部");
        range.getItems().addAll("全部");
        range.setVisible(false);
        range.setMaxWidth(100);
        JFXTextField weight=new JFXTextField("1");
        weight.setMaxWidth(100);
        rankingCondition.add(order,1,row);
        rankingCondition.add(range,2,row);
        rankingCondition.add(weight,3,row);

//        delete.setOnAction(event1 -> {
//            rankingCondition.getRowConstraints().remove(rowConstraints);
//            rankingCondition.getChildren().removeAll(conditionName,order,range,weight,delete);
//        });
//        rankingCondition.add(hashCode4,row);
    }



    private void collectCurrentData() throws WrongValueException {

        String pool = stockPool.getValue();
        if("全部".equals(pool)){
            stockPoolConditionVO.stockPool = StockPool.All;
            stockPoolConditionVO.excludeST = ST.isSelected();

        }else if("沪深300".equals(pool)){
            stockPoolConditionVO.stockPool = StockPool.HS300;
            stockPoolConditionVO.excludeST = ST.isSelected();

        }else if("自选股票池".equals(pool)) {
            stockPoolConditionVO.stockPool = StockPool.UserMode;
            stockPoolConditionVO.excludeST = ST.isSelected();
            //TODO 其余多选属性

        }else{
            throw new WrongValueException("股票池没有选择");
        }


       getFilterCondition();

       getRankingCondition();

       this.stockPickIndexList = filterBlender();

       getTraceBackCondition();

    }




    private void getRankingCondition() throws WrongValueException {
        if(strateyChoosed){
            ObservableList<Node> observableList =  rankingCondition.getChildren();
            StrategyType strategyType = null;
            boolean asc = false;
            int js = 0;
            for(Node node : observableList) {
                if (node instanceof Label) {
                    if (((Label) node).getText().endsWith("动量策略")) {
                        strategyType = StrategyType.MOM;
                        js = 1;
                        //System.out.println(((Label) node).getText());
                    } else if (((Label) node).getText().endsWith("均线偏离")) {
                        strategyType = StrategyType.MA;
                        js = 1;
                        // System.out.println(((Label) node).getText());
                    }
                } else if (node instanceof JFXComboBox) {
                    if (js == 1) {
                        String comp = (String) ((JFXComboBox) node).getValue();
                        if (comp.equals("从小到大")) asc = true;
                        else asc = false;
                        js++;

                        strategyConditionVO  = new StrategyConditionVO
                                (strategyType,begin,end, asc);
                        System.out.println("Ranking save:   "
                                +strategyConditionVO.strategyType.toString());
                    }
                }


            }

        } else {
            ObservableList<Node> observableList =  rankingCondition.getChildren();
            StrategyType strategyType = null;
            boolean asc = false;
            int formationPeriod = 0;
            double weight;
            int js = 0;
            for(Node node : observableList){
                if(node instanceof Label){
                    if(((Label) node).getText().endsWith("动量策略")){
                        strategyType = StrategyType.MOM;
                        js = 1;
                        //System.out.println(((Label) node).getText());
                    }else if(((Label) node).getText().endsWith("均线偏离")){
                        strategyType = StrategyType.MA;
                        js = 1;
                        // System.out.println(((Label) node).getText());
                    }
                }else
                if(node instanceof JFXComboBox){
                    if(js==1) {
                        String comp = (String) ((JFXComboBox) node).getValue();
                        if(comp.equals("从小到大")) asc = true;
                        else asc = false;
                        js++;
                    }
                }else
                if(node instanceof JFXTextField){
                    if(js==2) {
                        String temp = ((JFXTextField) node).getText();
                        if(temp == null || "".equals(temp)){
                            throw new WrongValueException("形成期不能为空");
                        }else{
                            formationPeriod = Integer.parseInt(temp);
                        }
                        //System.out.println(temp);
                        js++;
                    }else if(js==3){
                        String temp = ((JFXTextField) node).getText();
                        if(temp == null || "".equals(temp)){
                            throw new WrongValueException("权重不能为空");
                        }else{
                            weight = Double.parseDouble(temp);
                        }
                        //System.out.println(temp);
                        MixedStrategyVO mixedStrategyVO =  new MixedStrategyVO(strategyType,weight,asc,formationPeriod);
                        mixedStrategyVOList.add(mixedStrategyVO);
                        System.out.println("Ranking save:   "+
                                mixedStrategyVO.strategyType.toString()+"   形成期:"+
                                mixedStrategyVO.formationPeriod+"   权重: "+
                                mixedStrategyVO.weight);

                        js=0;
                    }
                }

            }

            if(mixedStrategyVOList.size()==0){
                throw new WrongValueException(" 没有选择排名条件");
            }
        }



    }


    private void getFilterCondition()  throws  WrongValueException{
        ObservableList<Node> observableList =  filterCondition.getChildren();
        StockPickIndex stockPickIndex = null;
        boolean less = true;
        double value;
        int js = 0;
        for(Node node : observableList){
            if(node instanceof Label){
                String label = ((Label) node).getText();
                stockPickIndex  = getStockPickIndexByName(label);
                if(stockPickIndex!=null)  js = 1;
            }else
            if(node instanceof JFXComboBox){
                if(js==1) {
                    String comp = (String) ((JFXComboBox) node).getValue();
                    if(comp.equals("大于")) less = true;
                    else less = false;
                    js++;
                }
            }else
            if(node instanceof JFXTextField){
                if(js==2) {
                    String temp = ((JFXTextField) node).getText();
                    if(temp == null || "".equals(temp)){
                        throw new WrongValueException("比较值不能为空");
                    }else{
                        value = Double.parseDouble(temp);
                    }

                    StockPickIndexVO stockPickIndexVO = null;
                    if(less) {
                        stockPickIndexVO = new StockPickIndexVO(stockPickIndex, value, null);
                    }
                    else{
                        stockPickIndexVO = new StockPickIndexVO(stockPickIndex, null, value);
                    }
                    stockPickIndexList.add(stockPickIndexVO);
//                    System.out.println("Filter save:   "+
//                            stockPickIndexVO.stockPickIndex.toString()+ (less? "大于":"小于")+
//                            value);


                    js=0;
                }
            }

        }


    }



    private void getTraceBackCondition()  throws  WrongValueException{
        if(strateyChoosed){
            String holdStr = holdingPeriod.getText();
            if(holdStr==null||holdStr.equals("")){
                throw new WrongValueException(holdingPeriod.getPromptText()+"不能为空");
            }

            String formationStr = formativePeriod.getText();
            if(formationStr==null||formationStr.equals("")){
                throw new WrongValueException(formativePeriod.getPromptText()+"不能为空");
            }

            String numStr = numOfShares.getText();
            if(numStr==null||numStr.equals("")){
                throw new WrongValueException(numOfShares.getPromptText()+"不能为空");
            }

            if("动量策略".equals(strategyType.getValue())){
                this.traceBackVO = new TraceBackVO(Integer.parseInt(formationStr),Integer.parseInt(holdStr),
                        0, Double.parseDouble(numStr));
            }else{
                this.traceBackVO = new TraceBackVO(Integer.parseInt(formationStr),Integer.parseInt(holdStr),
                        Integer.parseInt(numStr));
            }
        }else{
            String holdStr = holdingPeriod.getText();
            if(holdStr==null||holdStr.equals("")){
                throw new WrongValueException(holdingPeriod.getPromptText()+"不能为空");
            }

            String numStr = numOfShares.getText();
            if(numStr==null||numStr.equals("")){
                throw new WrongValueException(numOfShares.getPromptText()+"不能为空");
            }


            this.traceBackVO = new TraceBackVO(0,Integer.parseInt(holdStr),Integer.parseInt(numStr));

        }
    }


    /**
     * 根据中文名字 获得过滤股票的类型
     * @param name
     * @return
     */
    private StockPickIndex getStockPickIndexByName(String name){
        for(StockPickIndex stockPickIndex : StockPickIndex.values()){
            if(name.equals(stockPickIndex.toString()))
                return stockPickIndex;
        }
        return null;
    }

    /**
     * 条件的拼接
     */
    private List<StockPickIndexVO> filterBlender(){
        List<StockPickIndexVO> stockPickIndexVOs = new ArrayList<>();

        for(StockPickIndex stockPickIndex : StockPickIndex.values()){
            OptionalDouble upBound = stockPickIndexList.stream().
                   filter(t->t.stockPickIndex.equals(stockPickIndex)&& t.upBound!=null)
                    .mapToDouble(t->t.upBound).min();
            OptionalDouble lowBound = stockPickIndexList.stream().
                    filter(t->t.stockPickIndex.equals(stockPickIndex)&&t.lowerBound!=null)
                    .mapToDouble(t->t.lowerBound).max();

            if(upBound.isPresent() || lowBound.isPresent()){
                StockPickIndexVO stockPickIndexVO  = new StockPickIndexVO(stockPickIndex,null,null);
                if(lowBound.isPresent())   stockPickIndexVO.lowerBound = lowBound.getAsDouble();
                if(upBound.isPresent())  stockPickIndexVO.upBound = upBound.getAsDouble();

                stockPickIndexVOs.add(stockPickIndexVO);

                System.out.println("FIlter:    "+stockPickIndexVO.stockPickIndex.toString()+
                "上限:   "+ (stockPickIndexVO.upBound==null?"null":stockPickIndexVO.upBound)+
                "   下限:   "+ (stockPickIndexVO.lowerBound==null?"null":stockPickIndexVO.lowerBound));
            }
        }
        return stockPickIndexVOs;
    }

}
