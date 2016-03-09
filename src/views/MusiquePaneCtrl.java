package views;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.sun.scenario.effect.impl.prism.ps.PPSBlend_OVERLAYPeer;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Preference;
import utils.PathUtil;
import utils.WindowApp;

public class MusiquePaneCtrl extends WindowApp {

	 public static void main(String[] args) { launch(args); }

    @FXML
    private HBox root;

    @FXML
    private VBox controlPane;

    @FXML
    private Label lblMusique;

    @FXML
    private VBox playPaneSlot;    
    
	@FXML
    private CheckBox checkRejouer;
	
	private AnimationPaneCtrl animationPaneCtrl1 = null;
	private AnimationPaneCtrl animationPaneCtrl2 = null;
	private AnimationPaneCtrl animationPaneCtrl3 = null;
	private AnimationPaneCtrl animationPaneCtrl4 = null;

	private PlayPaneCtrl playPaneCtrl = null;
	
    private Media media = null;
    
    private VBox animLeftVBox;
    
    private VBox animRightVBox;
    
    private static final int CONTROL_PANE_WIDTH = 240;
    
    double largeurActuelle;
	double largeurCible; 
	DoubleProperty stageWidthProxyProperty;

    private void initAnimation()
    {
    	animLeftVBox = new VBox();
    	animRightVBox = new VBox();
    	animationPaneCtrl1 = (new AnimationPaneCtrl()).loadView();
    	animLeftVBox.getChildren().add(animationPaneCtrl1.getRoot());
    	animationPaneCtrl2 = (new AnimationPaneCtrl()).loadView();
    	animLeftVBox.getChildren().add(animationPaneCtrl2.getRoot());
    	animationPaneCtrl3 = (new AnimationPaneCtrl()).loadView();
    	animRightVBox.getChildren().add(animationPaneCtrl3.getRoot());
    	animationPaneCtrl4 = (new AnimationPaneCtrl()).loadView();
    	animRightVBox.getChildren().add(animationPaneCtrl4.getRoot());

    	root.getChildren().add(0, animLeftVBox);
    	root.getChildren().add(animRightVBox);
    	
    	//root.getChildren().add(animationPaneCtrl.getRoot());
    }
    
    private void initDragAndDrop()
    {
    	controlPane.setOnDragDropped( event -> {
	    	Dragboard db = event.getDragboard();
	        String erreur = null;
	        if (db.hasFiles())
	        {
	        	File f = db.getFiles().get(0);
	        	try {
	        		System.out.println("dragdropped: " + f.toURI().toString());
		        	Media newMedia = new Media(f.toURI().toString());
		            
		        	if( newMedia.getError()!= null )
		        	{
		        		erreur = "Impossible de charger le fichier: " + f.getName();
		        	}
		        	else 
		        	{
		        		playPaneCtrl.bindSong(newMedia, f.getName());
		        	};
	        	}
	        	catch(MediaException e)
	        	{
	        		erreur = "Impossible de charger le fichier: " + f.getName() + " ( " + e.getMessage() + " )";
	        	}
	        }
	        if(erreur != null){
	        	Alert alert = new Alert(AlertType.ERROR, erreur);
	        	alert.showAndWait();        		        	
	        }
	        event.setDropCompleted(erreur != null);
			controlPane.getStyleClass().remove("DragOver");
	        event.consume();    	
	    });
    	
    	controlPane.setOnDragEntered( event -> {
	    	if (event.getDragboard().hasFiles()) 
	    	{
	    		controlPane.getStyleClass().add("DragOver");
	            event.consume();    	
	        }
	    });
    	
    	controlPane.setOnDragExited( event -> {
			controlPane.getStyleClass().remove("DragOver");
			event.consume();    	
	    });
    	
    	controlPane.setOnDragOver( event -> {
			if ( event.getDragboard().hasFiles() ) 
			{
	            event.acceptTransferModes(TransferMode.COPY);
	            event.consume();
	        }
	    });
   	
    }

    @FXML
    private void initialize()
    {
    	initAnimation();
    	initPlayPane();
    	initDragAndDrop();
    	initSize();
    	setAnim();
    }
    
    private void setAnim()
    {
    	
    	 playPaneCtrl.timelineAnimButton.setOnAction((e) -> 
    	    {
    	    	largeurActuelle = getStage().getWidth();
    			largeurCible  = largeurActuelle > largeurIdealeDuStageSansAnimation() +40
	    			? largeurIdealeDuStageSansAnimation() +40 : this.largeurIdealeDuStage();
	    	
	    			stageWidthProxyProperty = 
	    					new SimpleDoubleProperty(largeurActuelle);
	    			stageWidthProxyProperty.
	    			addListener(stilldontcare->getStage().setWidth(stageWidthProxyProperty.get()));
    	    	
    	    	if (largeurActuelle > largeurIdealeDuStageSansAnimation() +40)
    	    	{
	    	    	Timeline timeline = new Timeline(
	    	    			new KeyFrame(Duration.millis(0),
	    	    					new KeyValue(lblMusique.translateYProperty(), 0)),
	    	    			new KeyFrame(Duration.millis(1000),
	    	    					new KeyValue(lblMusique.translateYProperty(), 50)),
	    	    			new KeyFrame(Duration.millis(2000),
	    	    					new KeyValue(lblMusique.translateYProperty(), 0)),
	    	    			new KeyFrame(Duration.millis(2000),
	    	    					new KeyValue(lblMusique.scaleXProperty(), 1)),
	    	    			new KeyFrame(Duration.millis(3000),
	    	    					new KeyValue(lblMusique.scaleXProperty(), 1.5)),
	    	    			new KeyFrame(Duration.millis(4000),
	    	    					new KeyValue(lblMusique.scaleXProperty(), 1),
	    	    					new KeyValue(stageWidthProxyProperty, largeurCible),
	    	    					new KeyValue(animationPaneCtrl1.getRoot().opacityProperty(), 0),
	    	    					new KeyValue(animationPaneCtrl2.getRoot().opacityProperty(), 0),
	    	    					new KeyValue(animationPaneCtrl3.getRoot().opacityProperty(), 0),
	    	    					new KeyValue(animationPaneCtrl4.getRoot().opacityProperty(), 0)));
	    	    	timeline.playFromStart();
    	    	}
    	    	else
    	    	{
    	    		Timeline timeline = new Timeline(
	    	    			new KeyFrame(Duration.millis(0),
	    	    					new KeyValue(lblMusique.translateYProperty(), 0)),
	    	    			new KeyFrame(Duration.millis(1000),
	    	    					new KeyValue(lblMusique.translateYProperty(), 50)),
	    	    			new KeyFrame(Duration.millis(2000),
	    	    					new KeyValue(lblMusique.translateYProperty(), 0)),
	    	    			new KeyFrame(Duration.millis(2000),
	    	    					new KeyValue(lblMusique.scaleXProperty(), 1)),
	    	    			new KeyFrame(Duration.millis(3000),
	    	    					new KeyValue(lblMusique.scaleXProperty(), 1.5)),
	    	    			new KeyFrame(Duration.millis(4000),
	    	    					new KeyValue(lblMusique.scaleXProperty(), 1),
	    	    					new KeyValue(stageWidthProxyProperty, largeurCible),
	    	    					new KeyValue(animationPaneCtrl1.getRoot().opacityProperty(), 1),
	    	    					new KeyValue(animationPaneCtrl2.getRoot().opacityProperty(), 1),
	    	    					new KeyValue(animationPaneCtrl3.getRoot().opacityProperty(), 1),
	    	    					new KeyValue(animationPaneCtrl4.getRoot().opacityProperty(), 1)));
	    	    	timeline.playFromStart();
    	    	}
    	    	
    	    });
    }
    
    private void initSize()
    {
    	controlPane.setMinWidth(CONTROL_PANE_WIDTH);
    	controlPane.setMaxWidth(CONTROL_PANE_WIDTH);
    	root.sceneProperty().addListener(dontcare->
    	{
    		root.getScene().windowProperty().addListener(dontcareeither->
    		{
    			Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
    	    	getStage().setHeight(screenSize.getHeight() * 0.9);
    	    	getStage().setWidth(largeurIdealeDuStage());
    		});
    	});  	
    }
    
    private double largeurIdealeDuStage()
    {
    	double largeurIdeale = CONTROL_PANE_WIDTH + getStage().getHeight();
    	double largeurEcran = Screen.getPrimary().getVisualBounds().getWidth();
    	return Math.min(largeurIdeale, largeurEcran*0.9);
    }
    
    private double largeurIdealeDuStageSansAnimation()
    {
    	return CONTROL_PANE_WIDTH + root.getPadding().getLeft()
    			+ root.getPadding().getRight();
    }
     
    public Stage getStage()
    {
    	return (Stage) root.getScene().getWindow();
    }
    
    private void initPlayPane()
    {
    	URL musique = getClass().getResource("/ressources/Kalimba.mp3");
    	media = new Media(musique.toExternalForm());
    	
    	playPaneCtrl = (new PlayPaneCtrl()).loadView();
    	playPaneSlot.getChildren().add(playPaneCtrl.getRoot());
    	VBox.setVgrow(playPaneCtrl.getRoot(), Priority.SOMETIMES);
    	playPaneCtrl.config( 
    			newStatus->onPlayingStatusChange(newStatus), 
    			()->onEndOfMedia());
    	playPaneCtrl.bindSong(media, "Kalimba");
    	animationPaneCtrl1.bindRate(playPaneCtrl.volumeProperty());
    	animationPaneCtrl2.bindRate(playPaneCtrl.volumeProperty());
    	animationPaneCtrl3.bindRate(playPaneCtrl.volumeProperty());
    	animationPaneCtrl4.bindRate(playPaneCtrl.volumeProperty());
    }
    
    private void onEndOfMedia()
    {
		if(checkRejouer.isSelected()) playPaneCtrl.bindAndPlay(media, "Popcorn"); 
    }
    
    private void onPlayingStatusChange(Status newStatus)
    {
		if(newStatus == Status.PLAYING) 
		{
			animationPaneCtrl1.play();
			animationPaneCtrl2.play();
			animationPaneCtrl3.play();
			animationPaneCtrl4.play();
		}
		else 
		{
			animationPaneCtrl1.pause();
			animationPaneCtrl2.pause();
			animationPaneCtrl3.pause();
			animationPaneCtrl4.pause();
		}
		System.out.println(root.getWidth());
    	System.out.println(root.getHeight());
    }

    @Override
	protected String getFxml() { return "MusiquePane.fxml"; }

    @Override
	protected Parent getRoot() { return root; }
    
	@Override
	protected String getTitle() { return "TP3 - Musique - David St-Pierre"; }
	
	public void stockerPreferences()
	{
		Preference pref = new Preference()
		{{
			stageWidth = getStage().getWidth();
			stageHeight = getStage().getHeight();
			 media = playPaneCtrl.getBindedMedia();
			songFile = media == null ? null : media.getSource();
			volume = playPaneCtrl.sldrVolume.valueProperty().get();
			playback = checkRejouer.isSelected();
		}};
		pref.stocker();
	}
	
	public void chargerPreferences() 
	{
		Preference pref = Preference.charger();
		if (pref == null) return;
		if (pref.stageHeight > 0) getStage().setHeight(
				Math.min(pref.stageHeight, Screen.getPrimary().getVisualBounds().getHeight()));
		if (pref.stageWidth > 0) getStage().setWidth(
				Math.min(pref.stageWidth, Screen.getPrimary().getVisualBounds().getWidth()));
		if (pref.volume > 0)
			playPaneCtrl.sldrVolume.valueProperty().set(pref.volume);
		if (pref.playback == true)
			checkRejouer.setSelected(true);
		try
		{
			System.out.println(pref.songFile != null);
			if (pref.songFile != null)
			{
				File f = new File(pref.songFile);
				media = new Media(pref.songFile);
				playPaneCtrl.bindSong(media, f.getName());
			}
			else
			{
				playPaneCtrl.bindNoSong();
			}
		}
		catch (Exception e)
		{	
			System.out.println("Corrupted media file");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur de chargement");
			alert.setHeaderText(null);
			alert.setContentText("Échec du chargement des préférences.");
			alert.showAndWait();
		}
	}
	
	@Override
	protected ContextMenu getTestMenu()
	{
		return new ContextMenu(
				new MenuItem("Sauver preferences") {{ setOnAction(event->stockerPreferences());}},
				new MenuItem("Charger preferences") {{ setOnAction(event->chargerPreferences());}},
				new MenuItem("Unbind Media") {{ setOnAction(event->playPaneCtrl.bindNoSong());}},
				new MenuItem("Quitter") {{ setOnAction(event->{});}}
				);
	}
    
}

