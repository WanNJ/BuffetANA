package gui.functions;

import blservice.market.MarketService;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import factory.BlFactoryService;
import factory.BlFactoryServiceImpl;
import gui.ChartController.*;
import gui.sidemenu.SideMenuController;
import gui.utils.DatePickerUtil;
import gui.utils.LocalHistoryService;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import vo.MarketStockDetailVO;

import javax.annotation.PostConstruct;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zjy
 */
@FXMLController(value = "/resources/fxml/ui/StockChoose.fxml" , title = "StockChoose")
public class StockChooseController {
    @PostConstruct
    public void init(){

    }
}
