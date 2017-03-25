package gui.functions;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import datafx.AnimatedFlowContainer;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.ContainerAnimations;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import javax.annotation.PostConstruct;

/**
 * @author zjy
 */
@FXMLController(value = "/resources/fxml/ui/StrategyBackTesting.fxml" , title = "StrategyBackTesting")
public class StrategyBackTestingController {
    @FXMLViewFlowContext private ViewFlowContext context;

    @FXML private StackPane titleBurgerContainer;
    @FXML private JFXHamburger titleBurger;
    @FXML private JFXDrawer drawer;

    @PostConstruct
    public void init() throws FlowException {
        // init the title hamburger icon
        drawer.setOnDrawerOpening((e) -> {
            titleBurger.getAnimation().setRate(1);
            titleBurger.getAnimation().play();
        });
        drawer.setOnDrawerClosing((e) -> {
            titleBurger.getAnimation().setRate(-1);
            titleBurger.getAnimation().play();
        });
        titleBurgerContainer.setOnMouseClicked((e)->{
            if (drawer.isHidden() || drawer.isHidding()) drawer.open();
            else drawer.close();
        });

        // side controller will add links to the content flow
        Flow sideMenuFlow = new Flow(StockChooseController.class);
        FlowHandler sideMenuFlowHandler = sideMenuFlow.createHandler(context);
        drawer.setSidePane(sideMenuFlowHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));
    }
}
