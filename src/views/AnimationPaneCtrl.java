package views;

import javafx.animation.FillTransition;
import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import utils.WindowApp;

public class AnimationPaneCtrl extends WindowApp {

    public static void main(String[] args) { launch(args); }

    @FXML
    private VBox root;

    @FXML
    private AnchorPane radialPane;

    @FXML
    private Rectangle blendLayer;

	@FXML
    private AnchorPane maskLayer;
	
	private Transition animation = null;
	
	/**
	 * Pauser l'animation
	 */
	public void pause(){ animation.pause();}
	
	/**
	 *	Démarrer l'animation 
	 */
	public void play(){ animation.play();}
	
    /**
     * Lie la vitesse de l'animation à une propriété qui va de 0 à 1
     * @param property
     */
    public void bindRate(DoubleProperty property) { 
		animation.rateProperty().bind( property
				.multiply(4)
				.multiply(property));
    	}
    
    private void fitAnimationInWindow()
    {
    	double size = Math.min(root.getHeight(), root.getWidth());
    	radialPane.setPrefSize(size, size);
    	radialPane.setMaxSize(size, size);
    	radialPane.setMinSize(size, size);
    	blendLayer.setWidth(size);
    	blendLayer.setHeight(size);
    }

    @FXML
    private void initialize()
    {
    	root.widthProperty().addListener(e->fitAnimationInWindow());
    	root.heightProperty().addListener(e->fitAnimationInWindow());
    	
    	blendLayer.setBlendMode(BlendMode.DIFFERENCE);
    	blendLayer.setFill(Color.BLACK);
    	
    	FillTransition ft = new FillTransition(Duration.millis(3000), blendLayer, Color.BLACK, Color.WHITE);
    	ft.setAutoReverse(true);
    	ft.setCycleCount(-1);
    	animation = ft;
    }

    @Override
	protected String getFxml() { return "AnimationPane.fxml"; }

    @Override
	protected Parent getRoot() { return root; }

    @Override
	protected ContextMenu getTestMenu(){
    	return new ContextMenu(
    			new MenuItem("Play"){{ setOnAction(event->{ play(); }); }},
    			new MenuItem("Pause"){{ setOnAction(event->{ pause(); }); }}
    	);
    }
    
	@Override
	protected String getTitle() { return "TP3 - Animation Pane"; }
    
}

