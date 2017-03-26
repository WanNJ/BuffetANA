package runner;

import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyphLoader;
import gui.main.MainController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AnotherEntrance extends Task<Integer> {

	@FXMLViewFlowContext private ViewFlowContext flowContext;

	private Stage stage;


	public void setStage(Stage stage) {
		this.stage = stage;
	}


	@Override
	protected Integer call() throws Exception {
		//启动RMI
		//ClientRunner cr = new ClientRunner();

		Platform.runLater(() -> {
            try {
                SVGGlyphLoader.loadGlyphsFont(AnotherEntrance.class.getResourceAsStream("/resources/fonts/icomoon.svg"),"icomoon.svg");
                Flow flow1 = new Flow(MainController.class);//这个地方不要动，好不容易才搞好的
                DefaultFlowContainer container = new DefaultFlowContainer();
                flowContext = new ViewFlowContext();
                flowContext.register("Stage", stage);
                flow1.createHandler(flowContext).start(container);
                JFXDecorator decorator = new JFXDecorator(stage, container.getView());
                decorator.setCustomMaximize(true);
                Scene scene = new Scene(decorator, 1200, 800);
                scene.getStylesheets().add(AnotherEntrance.class.getResource("/resources/css/jfoenix-fonts.css").toExternalForm());
                scene.getStylesheets().add(AnotherEntrance.class.getResource("/resources/css/jfoenix-design.css").toExternalForm());
                scene.getStylesheets().add(AnotherEntrance.class.getResource("/resources/css/jfoenix-main-demo.css").toExternalForm());
                //		stage.initStyle(StageStyle.UNDECORATED);
                //		stage.setFullScreen(true);
                stage.setMinWidth(700);
                stage.setMinHeight(800);


                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

		return 1;
	}
}