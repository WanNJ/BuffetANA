package gui.functions;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import vo.StockNameAndCodeVO;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 个股界面TreeTableView的控制委托类
 * Created by zjy on 2017/3/13.
 * @author zjy
 */
public class StockChangeController {
    private static final String titles[]={"股票代码","股票名称"};
    private JFXTreeTableView<Share> treeTableView;
    private ObservableList<Share> list;

    public void initTreeTableView(JFXTreeTableView<Share> treeTableView, TextField search, Collection<StockNameAndCodeVO> collection){
        this.treeTableView=treeTableView;
        //初始化ObservableList
        this.list = FXCollections.observableArrayList();
        this.list.addAll(collection.stream().map(share->new Share(share.code,share.name)).collect(Collectors.toList()));

        final TreeItem<Share> root = new RecursiveTreeItem<Share>(list, RecursiveTreeObject::getChildren);
        treeTableView.setRoot(root);

        //创建TreeTableView的列
        for(int index=0;index<titles.length;index++){
            setCustomerColumn(treeTableView,index);
        }
        //为treeTableView加上单击跳转的监听
        treeTableView.setOnMouseClicked(event -> {
            if(null!=treeTableView.getSelectionModel().getSelectedItem()){
                System.out.println("change:"+treeTableView.getSelectionModel().getSelectedItem().getValue().ID.get());//TODO:跳转界面
            }
        });
    }

    private void setCustomerColumn(JFXTreeTableView<Share> treeTableView,int index){
        JFXTreeTableColumn<Share, String> colum=new JFXTreeTableColumn<>(titles[index]);
        colum.setPrefWidth(100);
        colum.setCellValueFactory((TreeTableColumn.CellDataFeatures<Share, String> param) ->{
            StringProperty propertys[]={param.getValue().getValue().ID,param.getValue().getValue().name};
            if(colum.validateValue(param)) return propertys[index];
            else return colum.getComputedValue(param);
        });

        treeTableView.getColumns().add(colum);
    }

    /**
     * 要显示的股票属性
     * @author zjy
     *
     */
    private  class Share extends RecursiveTreeObject<Share> {
        StringProperty ID;
        StringProperty name;

        /**
         *
         * @param ID 股票代码
         * @param name 股票名称
         */
        public Share(String ID, String name) {
            this.ID = new SimpleStringProperty(ID);
            this.name = new SimpleStringProperty(name);
        }
    }

}
