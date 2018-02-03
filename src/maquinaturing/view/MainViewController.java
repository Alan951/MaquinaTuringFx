package maquinaturing.view;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import maquinaturing.MT.Estado;
import maquinaturing.MT.LexAnalyzer;
import maquinaturing.MT.MaquinaTuring;
import maquinaturing.MT.Cinta;

public class MainViewController {
    
    @FXML private AnchorPane mainPane;
    @FXML private Pane cintasContainerRoot;
    @FXML private Pane controlsRoot;
    @FXML private VBox cintasContainer;
    @FXML private VBox mainContainer;
    @FXML private Button btnCompile;
    @FXML private TextArea txtArea;
    @FXML private Label lineaFocused;
    @FXML private TextField txtCadena;
    @FXML private Button btnStart;
    @FXML private Button btnNextStep;
    @FXML private Label lblEstadoActual;
    @FXML private Text textFinal;
    
    private static final String VALID_STRING = "^((a|b|c)*)$";
    
    private MaquinaTuring mt;
    private LexAnalyzer lex;
    
    private static final String CAD_VALIDA = "La cadena es valida";
    private static final String CAD_INVALIDA = "La cadena no es valida";
    private static final Color BG_LABEL_AFFECTED = Color.rgb(18, 183, 32);
    private static final Color BG_LABEL_SELECTED = Color.rgb(255, 190, 66);
    private boolean resultNext;
    //private StyledTextArea textArea;
    
    private Scene scene;
    
    private List<CintaViewController> cintaControllers;
    
    public void init(Scene scene, AnchorPane mPane){
        cintaControllers = new ArrayList<>();
        this.scene = scene;
        this.mainPane = mPane;
        this.textFinal.setVisible(false);
        this.btnNextStep.setVisible(false);
        this.lblEstadoActual.setText("Estado actual: <>\nEstado anterior: <>");
        initViewComponents();
        
        
    }
    
    public void postShow(){
        Platform.runLater(() -> {
            ((Stage)scene.getWindow()).setHeight(cintasContainerRoot.getPrefHeight() + controlsRoot.getPrefHeight() + 45);
            ((Stage)scene.getWindow()).setMinHeight(cintasContainerRoot.getPrefHeight() + controlsRoot.getPrefHeight() + 45);
        });

    }
    
    private void initViewComponents(){
        cintasContainer.prefWidthProperty().bind(cintasContainerRoot.widthProperty());
        cintasContainerRoot.prefHeightProperty().bind(cintasContainer.heightProperty());
        lineaFocused.setVisible(false);
        
        txtArea.caretPositionProperty().addListener((obs, old, nw) -> lineaFocused.setText("Linea: "+searchLinePos(txtArea, nw)));
        txtArea.focusedProperty().addListener((obs, old, nw) -> lineaFocused.setVisible(nw.booleanValue()));
        
    }
    
    private int searchLinePos(TextArea txta, Number cursorPos){
        int line = 1;
        int it = 0;
        
        if(cursorPos.intValue() == 0) return line;
        
        for(Character a : txta.getText().toCharArray()){
            it++;
            
            if(a == '\n'){
                line++;
            }
            if(it == cursorPos.intValue()) return line;
        }
        
        return line;
    }
    
    private void createViewCinta(Cinta cinta){
        try{
            Pane p = null;
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("CintaView.fxml"));
            
            p = (Pane)loader.load();
            
            CintaViewController controlador = loader.getController();
            
            controlador = loader.getController();
            controlador.init(this, cinta);
            cinta.setController(controlador);
            
            controlador.setView(p);
            
            cintaControllers.add(controlador);
            cintasContainer.getChildren().add(p);   
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @FXML
    protected void onStartMTPressed(){
        if(!onCompilePressed()) return;
        
        if(!txtCadena.getText().matches(VALID_STRING)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cadena introducida invalida");
            alert.setHeaderText("La cadena introducida es invalida.");
            String content = "Las cadenas deben de ser valida con la siguiente expresión regular:\n"+VALID_STRING;
            
            alert.setContentText(content);

            alert.showAndWait();
            
            return;
        }
        
        List<Estado> estados = new ArrayList<Estado>();
        
        for(String instruction: txtArea.getText().split("\n")){
            if(instruction.trim().isEmpty()) continue;
            
            Estado e = lex.instructionToState(instruction.replace(" ", ""));
            System.out.println("Estado creado: "+e);
            if(e == null) continue;
            estados.add(e);
            
        }
        this.btnNextStep.setVisible(true);
        this.cintaControllers.clear();
        this.cintasContainer.getChildren().clear();
        
        mt = new MaquinaTuring(lex.getNumCintas(), lex.getStartState(), lex.getEndState(), txtCadena.getText());
        
        mt.getCintas().forEach((cinta) -> createViewCinta(cinta));
        
        mt.setEstados(estados);
        
        postShow();
        
        this.cintaControllers.forEach(controller -> {
            Platform.runLater(() -> {
                controller.getToggleButtonCenter().setSelected(true);
                controller.onCentrarToggle();
            });
            
            
        });
    }
    
    @FXML
    protected boolean onCompilePressed(){        
        if(txtArea.getText().trim().isEmpty()){
            return false;
        }
        
        lex = new LexAnalyzer();
        textFinal.setVisible(false);
        List<Integer> lineErrors = new ArrayList<>();
        int line = 1;
        
        for(String instruction : txtArea.getText().split("\n")){
            if(instruction.trim().isEmpty()) continue;
            if(!lex.validateMTI(instruction.replace(" ", "")))    lineErrors.add(line);
            line++;
        }
        
        if(lineErrors.size() > 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Instrucciones invalidas");
            alert.setHeaderText("Verifica las siguientes instrucciones de la maquina de turing.");
            String content = "";
            
            for(Integer errLine : lineErrors){
                content += "Error en la linea: "+errLine+".\n";
            }
            
            alert.setContentText(content);

            alert.showAndWait();
            
            return false;
        }else{
            
        }
        
        return true;
    }
    
    private CintaViewController searchCintaController(Cinta cinta){
        for(CintaViewController cintaView : cintaControllers){
            if(cinta.equals(cintaView.getCinta())){
                return cintaView;
            }
        }
        
        return null;
    }
    
    @FXML
    protected void onReset(){
        mt.reset();
        textFinal.setVisible(false);
    }
    
    @FXML
    protected boolean onNextStep(){
        boolean next = true;
        
        if(mt != null){

            resultNext = mt.nextStep(1);
            
            if(resultNext){
                if(mt.getCellAffecteds().size() > 0){
                    mt.getCellAffecteds().forEach((cellAff) -> AnimHelper.animLabelBackground(BG_LABEL_AFFECTED, searchCintaController(cellAff.getCinta()).getLabelByPos(cellAff.getIndexCell())));
                }
                
                mt.getCintasMoved().forEach((cinta) -> AnimHelper.animLabelBackground(BG_LABEL_SELECTED, searchCintaController(cinta).getLabelByPos(cinta.getCursorValue()), 4000));
            }else{
                if(!mt.termino){
                    textFinal.setText(CAD_INVALIDA);
                    textFinal.setVisible(true);
                    
                }else{
                    textFinal.setText(CAD_VALIDA);
                }
                next = false;
            }
            System.out.println("Cintas: "+mt.getCintas());
            System.out.println("Estado actual: "+mt.getEstadoActual());
            
            String n = mt.futureState() != null ? mt.futureState().toString() : "Sin estado";
            String n2 = mt.getEstadoActualObj() != null ? mt.getEstadoActualObj().toString() : "Sin estado";
            
            Platform.runLater(() -> this.lblEstadoActual.setText("Estado actual: "+n+"\nEstado anterior: "+n2));
            if(mt.termino){
                textFinal.setText(CAD_VALIDA);
                textFinal.setVisible(true);
            }            
        }
        return next;
    }
    
    @FXML
    protected void onNextStepAutomatic(){
        Task task = new Task<Void>(){
            @Override public Void call(){
                while(onNextStep()){
                    System.out.println("onNextStep executed");
                    try{Thread.sleep(20);}catch(InterruptedException e){e.printStackTrace();}
                }
                
                return null;
            }
        };
        new Thread(task).start();
    }
    
    private List<String> checkInstructions(){
        List<String> instructions = new ArrayList<>();
        
        return instructions;
    }
    
}
