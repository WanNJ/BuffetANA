package gui.utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * 给用户的选择
 * @author zjy
 *
 */
public class Choice{
	public String choiceName;
	public EventHandler<ActionEvent> value;
	
	/**
	 * 给用户的选择
	 * @param choiceName 选项的名字
	 * @param value 监听器
	 */
	public Choice(String choiceName, EventHandler<ActionEvent> value) {
		super();
		this.choiceName = choiceName;
		this.value = value;
	}
}