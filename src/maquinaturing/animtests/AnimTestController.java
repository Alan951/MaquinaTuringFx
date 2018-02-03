/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maquinaturing.animtests;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author Ck
 */
public class AnimTestController {
    
    @FXML private Label lblHello;
    @FXML private Button btnHello;
    @FXML private AnchorPane container;
    @FXML private Button btnInterpole;
    
    private FadeTransition ft;
    
    public AnimTestController(){}
    
    @FXML
    public void initialize(){}
    
    public void init(){
        ft = new FadeTransition(Duration.millis(200), lblHello);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        
        
        
    }
    
    @FXML
    public void onAction(){
        System.out.println("onAction invoked!");
        
        ft.play();
        
    }
    
    @FXML
    public void onInterpole(){
        final Animation animation = new Transition() {

            {
                setCycleDuration(Duration.millis(1000));
                setInterpolator(Interpolator.EASE_OUT);
            }

            @Override
            protected void interpolate(double frac) {
                System.out.println("frac: "+ frac);
                //Color vColor = new Color(0.5, 0, 0, 1 - frac);
                Color vColor = Color.rgb(18, 183,32, 1 - frac);
                lblHello.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
                
            }
        };
        animation.play();
    }
    
}
