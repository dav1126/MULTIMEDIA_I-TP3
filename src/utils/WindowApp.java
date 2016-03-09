package utils;
	
import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import views.MusiquePaneCtrl;


public abstract class WindowApp extends Application {
	
	protected abstract String getFxml();
	protected abstract String getTitle();
	protected abstract Parent getRoot();
	protected ContextMenu getTestMenu(){ 
    	return new ContextMenu(
    			new MenuItem("Do nothing"){{ setOnAction(event->{ System.out.println("Nothing to do!"); }); }}
    	);
	}

	private URL fxmlLocation()
	{
		return WindowApp.class.getResource("/views/" + getFxml());
	}
		
	public Parent build() throws IOException{
		return loadView().getRoot();
	}

	public <T extends WindowApp> T loadView(){
		try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(fxmlLocation());
	        loader.load();
	        return loader.getController();
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void start(Stage primaryStage) {
		WindowApp app = loadView();
		MusiquePaneCtrl mainController = (MusiquePaneCtrl)app;
    	System.out.println(app.getClass().getSimpleName() + " started");
		Parent root = app.getRoot();
		
    	root.setOnContextMenuRequested(event->{
    		app.getTestMenu().show(root, event.getScreenX(), event.getScreenY());
    		event.consume();
    	});
    	root.setStyle("-fx-padding: 20px");
    	
    	Image icone =  new Image("/ressources/1456967745_sound_speaker.png");
    	primaryStage.getIcons().add(icone);
    	
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(getTitle());
		mainController.getStage().setOnShown(dontcare ->
		{
			 mainController.chargerPreferences();
			 System.out.println("Chargement des preferences");				
		});
		primaryStage.show();
		mainController.getStage().setOnCloseRequest(dontcare ->
				{
					 mainController.stockerPreferences();
					 System.out.println("Sauvegarde des preferences");				
				});
	}
	
}
