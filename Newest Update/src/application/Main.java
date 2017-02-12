package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import view.MP3Controller;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Main extends Application {
		
	public void start(Stage primaryStage) {
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/Trial.fxml"));
			
			AnchorPane root = (AnchorPane)loader.load();
			
			Scene scene = new Scene(root);

			MP3Controller controller = loader.getController();
			
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);
			controller.start(primaryStage);


		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
