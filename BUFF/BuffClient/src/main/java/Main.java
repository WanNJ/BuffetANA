import javafx.application.Application;
import javafx.stage.Stage;
import runner.AnotherEntrance;
import runner.ProgressForm;

/**
 * Created by slow_time on 2017/3/17.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        AnotherEntrance anotherEntrance = new AnotherEntrance();
        anotherEntrance.setStage(primaryStage);
        ProgressForm progressFrom = new ProgressForm(anotherEntrance, primaryStage);
        progressFrom.activateProgressBar();
    }
}
