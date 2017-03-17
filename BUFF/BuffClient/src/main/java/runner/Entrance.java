package runner;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by slow_time on 2017/3/17.
 */
public class Entrance extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnotherEntrance anotherEntrance = new AnotherEntrance();
        anotherEntrance.setStage(primaryStage);
        ProgressFrom progressFrom = new ProgressFrom(anotherEntrance, primaryStage);
        progressFrom.activateProgressBar();
//        anotherEntrance.showContent();
    }
}
