package runner;

import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by slow_time on 2017/3/17.
 */
public class ProgressForm {

    private Stage dialogStage;
    private ProgressIndicator progressIndicator;

    public ProgressForm(final Task<?> task, Stage primaryStage) {

        dialogStage = new Stage();
        progressIndicator = new ProgressIndicator();

        // 窗口父子关系
        dialogStage.initOwner(primaryStage);
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.initStyle(StageStyle.TRANSPARENT);
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        // progress bar
        Label label = new Label("数据加载中, 请稍后...");
        label.setTextFill(Color.BLUE);
        //label.getStyleClass().add("progress-bar-root");
        progressIndicator.setProgress(-1F);
        //progressIndicator.getStyleClass().add("progress-bar-root");
        progressIndicator.progressProperty().bind(task.progressProperty());

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setBackground(Background.EMPTY);
        vBox.getChildren().addAll(progressIndicator,label);

        Scene scene = new Scene(vBox);
        scene.setFill(null);
        dialogStage.setScene(scene);

        task.setOnSucceeded(event -> dialogStage.close());


        Thread inner = new Thread(task);
        inner.start();
    }

    public void activateProgressBar() {
        dialogStage.show();
    }

    public Stage getDialogStage(){
        return dialogStage;
    }

    public void cancelProgressBar() {
        dialogStage.close();
    }
}
