package gui.functions;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXRippler;
import datafx.AnimatedFlowContainer;
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

import javax.annotation.PostConstruct;
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

        viewsBox.getChildren().addAll(new Flow(EstimateResultController.class).createHandler(context).start());
        viewsBox.getChildren().addAll(new Flow(AccumulatedIncomeController.class).createHandler(context).start());
        viewsBox.getChildren().addAll(new Flow(IncomeBarPieController.class).createHandler(context).start());
        viewsBox.getChildren().addAll(new Flow(BetterStrategyController.class).createHandler(context).start());

        viewList.addAll(Arrays.asList(
                context.getRegisteredObject(EstimateResultController.class),
                context.getRegisteredObject(AccumulatedIncomeController.class),
                context.getRegisteredObject(IncomeBarPieController.class),
                context.getRegisteredObject(EstimateResultController.class)
        ));

        showData();
    }

    public void showData(){
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
                        //TODO:
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
        });
        updateDataService.start();
    }
}
