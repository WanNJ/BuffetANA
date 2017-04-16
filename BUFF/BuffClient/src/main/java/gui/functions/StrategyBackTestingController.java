package gui.functions;

import blservice.strategy.StrategyService;
import blstub.strategy.StrategyServiceImpl_Stub;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXRippler;
import datafx.AnimatedFlowContainer;
import factory.BLFactorySeviceOnlyImpl;
import factory.BlFactoryService;
import gui.utils.Updatable;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.ContainerAnimations;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import util.StrategyScoreVO;
import vo.*;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zjy
 */
@FXMLController(value = "/resources/fxml/ui/StrategyBackTesting.fxml" , title = "StrategyBackTesting")
public class StrategyBackTestingController {
    @FXMLViewFlowContext private ViewFlowContext context;

    @FXML private StackPane root;
    @FXML private JFXRippler rippler;
    @FXML private StackPane titleBurgerContainer;
    @FXML private JFXHamburger titleBurger;
    @FXML private JFXDrawer drawer;
    @FXML private VBox viewsBox;
    @FXML private StackPane spinnerPane;

    private List<Updatable> viewList=new ArrayList<>();

    @PostConstruct
    public void init() throws FlowException {
        context.register(this);

        // init the title hamburger icon
        drawer.setOnDrawerClosed((e) -> {
            rippler.setVisible(true);
        });
        drawer.setOnDrawerOpened((e) -> {
            rippler.setVisible(false);
        });
        drawer.setOnDrawerOpening((e) -> {
            titleBurger.getAnimation().setRate(1);
            titleBurger.getAnimation().play();
            rippler.setVisible(false);
        });
        drawer.setOnDrawerClosing((e) -> {
            titleBurger.getAnimation().setRate(-1);
            titleBurger.getAnimation().play();
            rippler.setVisible(false);
        });
        titleBurgerContainer.setOnMouseClicked((e)->{
            if (drawer.isHidden() || drawer.isHidding()) {
                drawer.open();
            }
            else {
                drawer.close();
            }
        });

        // side controller will add links to the content flow
        Flow sideMenuFlow = new Flow(StockChooseController.class);
        FlowHandler sideMenuFlowHandler = sideMenuFlow.createHandler(context);
        drawer.setSidePane(sideMenuFlowHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));
        //添加界面到viewsBox中显示
        viewsBox.getChildren().addAll(new Flow(EstimateResultController.class).createHandler(context).start());
        viewsBox.getChildren().addAll(new Flow(AccumulatedIncomeController.class).createHandler(context).start());
        viewsBox.getChildren().addAll(new Flow(IncomeBarPieController.class).createHandler(context).start());
        viewsBox.getChildren().addAll(new Flow(BetterStrategyController.class).createHandler(context).start());
        //将界面添加到待更新数据的集合里，将来若要更新数据，会依次调用这些类的updateData()方法
        viewList.addAll(Arrays.asList(
                context.getRegisteredObject(EstimateResultController.class),
                context.getRegisteredObject(AccumulatedIncomeController.class),
                context.getRegisteredObject(IncomeBarPieController.class),
                context.getRegisteredObject(BetterStrategyController.class)
        ));

        //showData();
    }

    /**
     * 更新各界面的数据
     * @param strateyChoosed 用于标记  均值策略和动量策略 是否已经被选中
     * @param strategyConditionVO 策略的基本信息
     * @param stockPoolConditionVO 记录股票池的选择信息
     * @param stockPickIndexList 选股条件的列表
     * @param traceBackVO 形成期、均线期、持仓期、持有股票数、动量策略中的取前百分之多少的股票等信息
     * @param from 开始日期
     * @param to 结束日期
     * @param mixedStrategyVOList 排名条件的列表
     */
    public void showData(boolean strateyChoosed, StrategyConditionVO strategyConditionVO,
                         StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexList,
                         TraceBackVO traceBackVO, LocalDate from,LocalDate to,
                         List<MixedStrategyVO> mixedStrategyVOList){
        //显示加载动画，把界面变为不可操作
        spinnerPane.setVisible(true);
        root.setDisable(true);
        //启动后台线程，计算后台数据
        Service<Void> updateDataService=new Service<Void>(){
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        BlFactoryService blFactoryService = new BLFactorySeviceOnlyImpl();
                        StrategyService strategyService=blFactoryService.createStrategyService();//TODO:待将stub换成真正的实现
                        if(strateyChoosed){
                            strategyService.init(strategyConditionVO,stockPoolConditionVO,stockPickIndexList);
                            strategyService.calculate(traceBackVO);
                        }else{
                            strategyService.initMixed(from,to,stockPoolConditionVO,
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

                        return null;
                    }
                };
            }
        };
        //加载完成时，恢复界面，分别更新每张图的数据
        updateDataService.setOnSucceeded(event -> {
            viewList.stream().forEach(Updatable::updateData);
            spinnerPane.setVisible(false);
            root.setDisable(false);
            drawer.close();
        });
        updateDataService.start();
    }
}
