package gui.functions;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.datafx.controller.FXMLController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;

import javax.annotation.PostConstruct;

/**
 * 主界面的控制器
 * @author zjy
 */
@FXMLController(value = "/resources/fxml/ui/Market.fxml" , title = "Material Design Example")
public class MarketController {

    @FXML
    private JFXTreeTableView<Share> allSharesList;
    @FXML
    private JFXTreeTableView<Share> recentlySharesList;

    private ObservableList<Share> allShares;//所有股票列表项的集合
    private ObservableList<Share> recentlyShares;//最近浏览股票列表项的集合
    private static final String titles[]={"股票代码","股票名称","现价（元）","涨跌（元）","涨跌幅（%）"};


    @PostConstruct
    public void init(){
        allShares = FXCollections.observableArrayList();
        recentlyShares = FXCollections.observableArrayList();
        //添加要显示的行的信息		下面是一个例子
        allShares.add(new Share("ID1","name1","price1","rise1","rise_percent1"));
        allShares.add(new Share("ID2","name2","14.9","+0.2","+0.03"));
        recentlyShares.add(new Share("ID3","name3","price3","rise3","rise_percent3"));
        recentlyShares.add(new Share("ID4","name4","16.7","-0.3","-0.13"));

        initTreeTableView(allSharesList,allShares);
        initTreeTableView(recentlySharesList,recentlyShares);
    }

    private void initTreeTableView(JFXTreeTableView<Share> treeTableView,ObservableList<Share> list){
        final TreeItem<Share> root = new RecursiveTreeItem<Share>(list, RecursiveTreeObject::getChildren);
        treeTableView.setRoot(root);

        //创建TreeTableView的列
        for(int index=0;index<titles.length;index++){
            setCustomerColumn(treeTableView,index);
        }
    }

    private void setCustomerColumn(JFXTreeTableView<Share> treeTableView,int index){
        JFXTreeTableColumn<Share, String> colum=new JFXTreeTableColumn<>(titles[index]);
        colum.setPrefWidth(100);
        colum.setCellValueFactory((TreeTableColumn.CellDataFeatures<Share, String> param) ->{
            StringProperty propertys[]={param.getValue().getValue().ID,param.getValue().getValue().name,
                    param.getValue().getValue().price,param.getValue().getValue().rise,
                    param.getValue().getValue().rise_percent};
            if(colum.validateValue(param)) return propertys[index];
            else return colum.getComputedValue(param);
        });

        colum.addEventHandler(EventType.ROOT,event -> {System.out.println(123);});

        treeTableView.getColumns().add(colum);
    }


    /**
     * 要显示的股票属性
     * @author zjy
     *
     */
    private  class Share extends RecursiveTreeObject<Share>{
        StringProperty ID;
        StringProperty name;
        StringProperty price;
        StringProperty rise;
        StringProperty rise_percent;

        /**
         *
         * @param ID 股票代码
         * @param name 股票名称
         * @param price 现价
         * @param rise 涨跌（价格）
         * @param rise_percent 涨跌幅（百分比）
         */
        public Share(String ID, String name, String price, String rise, String rise_percent) {
            this.ID = new SimpleStringProperty(ID);
            this.name = new SimpleStringProperty(name);
            this.price = new SimpleStringProperty(price);
            this.rise = new SimpleStringProperty(rise);
            this.rise_percent = new SimpleStringProperty(rise_percent);
        }
    }
}
