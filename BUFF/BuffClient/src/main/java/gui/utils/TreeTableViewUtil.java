package gui.utils;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;

/**
 * Created by asus-a on 2017/4/12.
 */
public class TreeTableViewUtil {


    /**
     *
     * @param treeTableView 要设置的treeTableView
     * @param list 与treeTableView绑定的ObservableList
     * @param titles 每一列的标题名
     * @param <T> 每一行的数据对象
     * @param propertys T的属性的集合，要求和titles的内容一一对应
     */
    public  <T extends RecursiveTreeObject<T>> void initTreeTableView(JFXTreeTableView<T> treeTableView, ObservableList<T> list, String titles[], StringProperty propertys[]){
        final TreeItem<T> root = new RecursiveTreeItem<T>(list, RecursiveTreeObject::getChildren);
        treeTableView.setRoot(root);

        //创建TreeTableView的列
        for(int index=0;index<titles.length;index++){
            setColumn(treeTableView,titles[index],propertys[index]);
        }
    }

    private <T extends RecursiveTreeObject<T>> void setColumn(JFXTreeTableView<T> treeTableView, String title,StringProperty property){
        JFXTreeTableColumn<T, String> colum=new JFXTreeTableColumn<>(title);
        colum.setPrefWidth(100);
        //设置要显示的值
        colum.setCellValueFactory((TreeTableColumn.CellDataFeatures<T, String> param) ->{
            if(colum.validateValue(param)) return property;
            else return colum.getComputedValue(param);
        });

        treeTableView.getColumns().add(colum);
    }

}
