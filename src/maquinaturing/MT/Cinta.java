
package maquinaturing.MT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import maquinaturing.view.CintaViewController;

class CellHistory{
    private List<Character> lista;
    
    public CellHistory(Character first){
        lista = new ArrayList<>();
        lista.add(first);
    }
    
    public List<Character> getLastTree(){
        List<Character> lista = new ArrayList<>();
        
        for(int x = lista.size() ; x > 0 ; x--){
            lista.add(this.lista.get(x));
        }
        
        return lista;
    }
    
    public void add(Character c){
        lista.add(c);
    }
    
    public Character getLast(){
        return lista.get(lista.size()-1);
    }
    
    public Character getPenultimo(){
        if(lista.size() == 1)
            return getLast();
        else
            return lista.get(lista.size()-2);
    }
}

public class Cinta {
    private List<Character> cinta;
    private Map<Integer, CellHistory> historial;
    private int cursor;
    
    public final static int RIGHT = 1;
    public final static int LEFT = -1;
    public final static int NO_MOVE = 0;
    public final static Character WHITE_SPACE = (char)254;
    private final static int CINTA_LIMITE = 102;
    
    private CintaViewController controller;
    
    public Cinta(){
        cinta = new ArrayList<>();
        historial = new HashMap<>();
        
        reset();
        
        cursor = CINTA_LIMITE / 2;
    }
    
    public void reset(){
        cinta.clear();
        historial.clear();
        if(controller != null)  controller.clearLabels();
        
        for(int x = 0 ; x < CINTA_LIMITE ; x++){
            cinta.add(WHITE_SPACE);
            
            if(controller != null)  controller.addLabel(cinta.get(x));
            
            historial.put(x, new CellHistory(WHITE_SPACE));
        }        
        cursor = CINTA_LIMITE / 2;
        
        if(controller != null){
            controller.setCursorLabelPos(Integer.toString(cursor)); //Actualiza el label que muestra la posición del cursor
            controller.toggleCursor(cursor); //Actualiza el label de la celda cursor
        }
    }
    
    public void setController(CintaViewController controller){
        this.controller = controller;
        
        for(int x = 0 ; x < CINTA_LIMITE ; x++){
            controller.addLabel(cinta.get(x));
        }
        
        controller.toggleCursor(cursor);
    }
    
    public Cinta createCinta(String chars){
        cinta.clear();
        historial.clear();
        if(controller != null)  controller.clearLabels();
        
        for(int x = 0 ; x < CINTA_LIMITE ; x ++){
            cinta.add(WHITE_SPACE);
            if(controller != null)  controller.addLabel(WHITE_SPACE);
            historial.put(x, new CellHistory(WHITE_SPACE));
        }
        
        cursor = (int)((CINTA_LIMITE / 2) - (chars.length() / 2));
        
        if(controller != null)  controller.setCursorLabelPos(Integer.toString(cursor));
        
        int tempCursor = cursor;
        
        for(int x = 0 ; x < chars.length() ; x++){
            cinta.set(tempCursor, chars.charAt(x));
            
            if(controller != null)  controller.updateLabel(tempCursor, chars.charAt(x));
            
            historial.get(tempCursor).add(chars.charAt(x));
            tempCursor++;
        }
        
        if(controller != null){
            controller.toggleCursor(cursor);
        }
        
        
        return this;
    }
    
    public Character nextRight(){
        Character charObj = null;
        
        if(cursor + 1 <= cinta.size()){
            charObj = cinta.get(++cursor);
            if(controller != null)  controller.toggleCursor(cursor);
            
        }
        
        return cinta.get(cursor);
    }
    
    public Character nextLeft(){
        Character charObj = null;
        
        if(cursor - 1 > 0){
            charObj = cinta.get(--cursor);
            if(controller != null)  controller.toggleCursor(cursor);
        }
        
        return charObj;
    }
    
    public Character next(int direction){
        if(direction == RIGHT){
            return nextRight();
        }else if(direction == LEFT){
            return nextLeft();
        }else if(direction == NO_MOVE){
            return getChar();
        }
        
        return null;
    }
    
    public Character getChar(){
        return cinta.get(cursor);
    }
    
    public void updateChar(Character charObj){
        cinta.set(cursor, charObj);
        
        if(controller != null)  controller.updateLabel(cursor, charObj);
        
        historial.get(cursor).add(charObj);
    }
    
    public List<Character> getCinta(){
        List<Character> lista = new ArrayList<>();
        
        int topCursor = CINTA_LIMITE - 1;
        int botCursor = 0;
        
        while(true){
            if(cinta.get(topCursor) != WHITE_SPACE && cinta.get(botCursor) != WHITE_SPACE){
                break;
            }
            
            if(cinta.get(topCursor) == WHITE_SPACE){
                topCursor--;
            }
            
            if(cinta.get(botCursor) == WHITE_SPACE){
                botCursor++;
            }
            
            if(topCursor == 0 || botCursor == CINTA_LIMITE - 1){
                break;
            }
        }
        
        topCursor = (topCursor == CINTA_LIMITE - 1) ? CINTA_LIMITE - 1 : topCursor + 1;
        botCursor = (botCursor == 0) ? 0 : botCursor - 1;
        
        for(int x = botCursor ; x <= topCursor ; x++){
            lista.add(cinta.get(x));
        }
        
        return lista;
    }
    
    public int getCursorValue(){
        return cursor;
    }
    
    public CellHistory getHistoryOf(int pos){
        if(pos < 0 || pos > CINTA_LIMITE){
            return null;
        }else{
            return historial.get(pos);
        }
    }
    
    public List<Character> getFullCinta(){
        return cinta;
    }

    @Override
    public String toString() {
        String c = "[";
        
        int topCursor = CINTA_LIMITE - 1;
        int botCursor = 0;
        
        while(true){
            if(cinta.get(topCursor) != WHITE_SPACE && cinta.get(botCursor) != WHITE_SPACE){
            
                break;
            }
            
            if(cinta.get(topCursor) == WHITE_SPACE){
                topCursor--;
            }
            
            if(cinta.get(botCursor) == WHITE_SPACE){
                botCursor++;
            }
            
            if(topCursor == 0 || botCursor == CINTA_LIMITE - 1){
                c+="]";
                break;
            }
        }
        
        topCursor = (topCursor == CINTA_LIMITE - 1) ? CINTA_LIMITE - 1 : topCursor + 1;
        botCursor = (botCursor == 0) ? 0 : botCursor - 1;
        
        for(int x = botCursor ; x <= topCursor ; x++){
            if(x >= topCursor){
                c += cinta.get(x)+"]";
            }else{
                c += cinta.get(x)+ ", ";
            }
        }
        
        return "Cinta{" + "cinta=" + c + ", cursor=" + cursor + '}';
    }    
}
