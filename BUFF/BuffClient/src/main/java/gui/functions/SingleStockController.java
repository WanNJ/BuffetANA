package gui.functions;

import com.jfoenix.controls.JFXDatePicker;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import vo.KLinePieceVO;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

/**
 * Created by slow_time on 2017/3/4.
 */

@FXMLController(value = "/resources/fxml/ui/SingleStock.fxml" , title = "Single Stock")
public class SingleStockController {

    @FXMLViewFlowContext
    private ViewFlowContext context;

    //@FXML private JFXDrawer drawer;

    @FXML
    private StackPane root;
    @FXML
    private BorderPane borderPane;
    @FXML
    private StackPane detailsPane;
    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private Label openIndexLabel;
    @FXML
    private Label closeIndexLabel;
    @FXML

    private Label highIndexLabel;
    @FXML
    private Label lowIndexLabel;
    @FXML
    private Label volLabel;
    @FXML
    private Label adjCloseIndexLabel;
    @FXML
    private TableView<KLinePieceVO> stockDetailsTable;
    @FXML
    private TableColumn<KLinePieceVO, LocalDate> dateColum;
    @FXML
    private TableColumn<KLinePieceVO, Double> closeIndexColum;
    @FXML
    private TableColumn<KLinePieceVO, Double> rangeColum;


    
    private  Parent lineContent; // 包含所欲的画线内容

    private FlowHandler  LineHandler;

    @PostConstruct
    public void init() throws FlowException, VetoException {
        datePicker.setDialogParent(root);

        context = new ViewFlowContext();
        // set the default controller
        Flow innerFlow = new Flow(LinesPanelController.class);

        LineHandler = innerFlow.createHandler(context);
        context.register("LineHandler", LineHandler);
        //drawer.setContent(LineHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));
        borderPane.setCenter(LineHandler.start());
    }

}
