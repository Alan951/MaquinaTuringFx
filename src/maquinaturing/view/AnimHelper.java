/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maquinaturing.view;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author Ck
 */
class AnimNode{
    public Node node;
    public Transition transition;
}

public class AnimHelper {
    
    private final static List<AnimNode> animatedNodes = new ArrayList<AnimNode>();
    
    public static void animLabelBackground(Color color, Label node){
        animLabelBackground(color, node, 1000);
    }
    
    public static void animLabelBackground(Color color, Label node, double duration){    
        AnimNode nTemp = null;
        for(AnimNode n : animatedNodes){
            if(n.node.equals(node)){
                n.transition.stop();
                nTemp = n;
                break;
            }
        }
        
        if(nTemp != null) animatedNodes.remove(nTemp);
        
        final Animation anim = new Transition(){
            {
                setCycleDuration(Duration.millis(duration));
                setInterpolator(Interpolator.EASE_OUT);
            }
            
            @Override
            protected void interpolate(double frac){
                Color vColor = Color.rgb(convert(color.getRed()), convert(color.getGreen()), convert(color.getBlue()), 1 - frac);
                node.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        };
        
        nTemp = new AnimNode();
        nTemp.node = node;
        nTemp.transition = (Transition)anim;
        
        anim.play();
        animatedNodes.add(nTemp);
    }
    
    private static int convert(double val){
        return (int)(255 * val);
    }
    
}
