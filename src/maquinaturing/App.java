/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maquinaturing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import maquinaturing.animtests.AnimTestController;
import maquinaturing.view.MainViewController;

/**
 *
 * @author Ck
 */
public class App extends Application {
    
    private MainViewController controller;
    
    @Override
    public void start(Stage stage) {
        if(false){
            try{
                AnchorPane p = null;
                
                FXMLLoader loader = new FXMLLoader();
                
                loader.setLocation(this.getClass().getResource("animtests/AnimTest.fxml"));
                
                p = (AnchorPane)loader.load();
                
                Scene scene = new Scene(p);
                
                ((AnimTestController)loader.getController()).init();
                
                stage.setScene(scene);
                
                stage.show();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            try {
                AnchorPane anchorP = null;

                FXMLLoader loader = new FXMLLoader();

                loader.setLocation(this.getClass().getResource("view/MainView.fxml"));

                anchorP = (AnchorPane)loader.load();

                Scene scene = new Scene(anchorP);

                stage.setScene(scene);

                stage.setTitle("Maquina de Turing");
                controller = loader.getController();
                controller.init(scene, anchorP);
                stage.show();
                controller.postShow();
            } catch(Exception e) {
                    e.printStackTrace();
            }
        }
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*//MaquinaTuring mt = new MaquinaTuring(2);
        
        do{
            System.out.println("Cintas: "+mt.getCintas());
            //System.out.println("Estados: "+mt.getEstados());
            //System.out.println("Estado Actual: "+mt.getEstadoActual());
            if(!mt.nextStep()){
                System.out.println("No termino");
                break;
            }
            
            if(mt.termino){
                break;
            }
        }while(true);*/
        
        launch(args);
    }
    
}
