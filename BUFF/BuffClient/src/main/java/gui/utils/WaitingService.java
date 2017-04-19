package gui.utils;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * @author zjy
 * 等待0.5秒
 */
public class WaitingService<V> extends Service<V>{
    @Override
    protected Task<V> createTask() {
        return new Task<V>() {
            @Override
            protected V call() throws Exception {
                Thread.sleep(500);
                return null;
            }
        };
    }
}
