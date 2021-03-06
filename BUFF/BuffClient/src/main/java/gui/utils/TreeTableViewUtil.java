package gui.utils;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.InvalidationListener;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjy on 2017/4/12.
 */
public class TreeTableViewUtil {


    /**
     *
     * @param treeTableView 要设置的treeTableView
     * @param list 与treeTableView绑定的ObservableList
     * @param titles 每一列的标题名
     * @param <T> 每一行的数据对象
     */
    public static <T extends TreeTableViewValue<T>> void initTreeTableView(JFXTreeTableView<T> treeTableView, ObservableList<T> list, String titles[]){
        final TreeItem<T> root = new RecursiveTreeItem<T>(list, RecursiveTreeObject::getChildren);
        treeTableView.setRoot(root);

        //创建TreeTableView的列
        for(int index=0;index<titles.length;index++){
            setColumn(treeTableView,titles,index);
        }
    }

    private static <T extends TreeTableViewValue<T>> void setColumn(JFXTreeTableView<T> treeTableView, String titles[],int index){
        JFXTreeTableColumn<T, String> colum=new JFXTreeTableColumn<>(titles[index]);
        colum.setMinWidth(100);

        //设置要显示的值
        colum.setCellValueFactory((TreeTableColumn.CellDataFeatures<T, String> param) ->{
            if(colum.validateValue(param)) return param.getValue().getValue().values.get(index);
            else return colum.getComputedValue(param);
        });

        treeTableView.getColumns().add(colum);
    }

}
