/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maquinaturing.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import maquinaturing.MT.Cinta;

/**
 * FXML Controller class
 *
 * @author Ck
 */
public class CintaViewController {
    
    @FXML private Label lblCursorPos;
    @FXML private HBox lblsContainer;
    @FXML private Button btnCentrar;
    @FXML private ToggleButton btnToggleCentrar;
    @FXML private ScrollPane sp;
    
    private boolean autoCentrar;
    
    private Cinta cinta;
    
    private Pane pane;
    private MainViewController mainView;
    
    
    public void init(MainViewController main, Cinta cinta){
        this.mainView = main;
        this.cinta = cinta;
        this.autoCentrar = false;
    }
    
    @FXML
    protected void initialize(){
        
    }
    
    public void setView(Pane p){
        this.pane = p;
    }
    
    public Pane getView(){
        return this.pane;
    }
    
    public void clearLabels(){
        lblsContainer.getChildren().clear();
    }
    
    public void addLabel(Character nw){
        lblsContainer.getChildren().add(new Label(nw.toString()));
    }
    
    public void setCursorLabelPos(String st){
        Platform.runLater(() -> this.lblCursorPos.setText(st));
    }
    
    public void updateLabel(int pos, Character nw){
        Label l = (Label)lblsContainer.getChildren().get(pos);
        Platform.runLater(() -> {
            l.setText(nw.toString());
            
        });
        
    }
    
    public void toggleCursor(int pos){
        setCursorLabelPos(Integer.toString(pos));
        Label cursor = searchLabelCursor();
        
        
        Platform.runLater(() -> {
            Node newCursor = lblsContainer.getChildren().get(pos);
            if(cursor != null){
                cursor.getStyleClass().remove("label-cursor");
            }

            newCursor.getStyleClass().add("label-cursor");
            Platform.runLater(() -> {if(autoCentrar) centerNodeInScrollPane(sp, newCursor);});
        });
    }
    
    public Cinta getCinta(){
        return cinta;
    }
    
    public Label searchLabelCursor(){
        Node n = null;
        
        for(Node node : lblsContainer.getChildren()){
            if(node.getStyleClass() != null && node.getStyleClass().contains("label-cursor")){
                n = node;
            }
        }
        
        return (Label)n;
    }

    @FXML
    protected void onCentrarToggle(){
        System.out.println(btnToggleCentrar.isSelected());
        if(btnToggleCentrar.isSelected()){
            
            this.autoCentrar = true;
            centerNodeInScrollPane(sp, searchLabelCursor());
        }else{
            this.autoCentrar = false;
        }
    }
    
    private void centerNodeInScrollPane(ScrollPane scrollPane, Node node) {
            node.getParent().requestLayout();
            node.getParent().layout();
            double h = scrollPane.getContent().getBoundsInLocal().getWidth();
            double x = (node.getBoundsInParent().getMaxX() + 
                        node.getBoundsInParent().getMinX()) / 2.0;

            double v = scrollPane.getViewportBounds().getWidth();
            double hValue = scrollPane.getHmax() * ((x - 0.5 * v) / (h - v));
            scrollPane.setHvalue(hValue);
            
            /*System.out.println("h: "+h+" x: "+x+" v: "+v+" hValue: "+ hValue+"\n"
                    + "boundsinparent = " + node.getBoundsInParent()
                    + "-----");*/
    }

    public Label getLabelByPos(Integer indexCell) {
        return (Label)lblsContainer.getChildren().get(indexCell);
    }
    
    public ToggleButton getToggleButtonCenter(){
        return btnToggleCentrar;
    }
    
}
