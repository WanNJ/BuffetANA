import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyphLoader;
import gui.functions.LinesPanelController;
import gui.main.MainController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@FXMLViewFlowContext private ViewFlowContext flowContext;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) throws Exception {

		new Thread(()->{
			try {
				SVGGlyphLoader.loadGlyphsFont(Main.class.getResourceAsStream("/resources/fonts/icomoon.svg"),"icomoon.svg");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}).start();

		Flow flow = new Flow(LinesPanelController.class);
		DefaultFlowContainer container = new DefaultFlowContainer();
		flowContext = new ViewFlowContext();
		flowContext.register("Stage", stage);
		flow.createHandler(flowContext).start(container);
		
		JFXDecorator decorator = new JFXDecorator(stage, container.getView());
		decorator.setCustomMaximize(true);
		Scene scene = new Scene(decorator, 1200, 800);
		scene.getStylesheets().add(Main.class.getResource("/resources/css/jfoenix-fonts.css").toExternalForm());
		scene.getStylesheets().add(Main.class.getResource("/resources/css/jfoenix-design.css").toExternalForm());
		scene.getStylesheets().add(Main.class.getResource("/resources/css/jfoenix-main-demo.css").toExternalForm());
		//		stage.initStyle(StageStyle.UNDECORATED);
		//		stage.setFullScreen(true);
		stage.setMinWidth(700);
		stage.setMinHeight(800);
		stage.setScene(scene);
		stage.show();
	}

}