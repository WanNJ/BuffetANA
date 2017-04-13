package gui.utils;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Hyperlink;

import java.util.ArrayList;
import java.util.List;

/**
 * 用作TreeTableView的每一行的值对象
 *
 * @author zjy
 */
public class TreeTableViewValue<T> extends RecursiveTreeObject<T> {
    /**
     * 每一列的值对象，要求按顺序加入
     */
    public List<StringProperty> values=new ArrayList<>();
}
