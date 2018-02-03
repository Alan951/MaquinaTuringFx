
package maquinaturing.MT;

import java.util.List;

public class Estado {
    private int nextEstado;
    private int estado;
    private int numParm;
    private List<Character> inParm;
    private List<Character> outParm;
    private List<Integer> cursorDirection;

    public Estado(int estado, int nextEstado, List<Character> inParm, List<Character> outParm, List<Integer> cursorDirection) {
        this.nextEstado = nextEstado;
        this.estado = estado;
        this.inParm = inParm;
        this.outParm = outParm;
        this.cursorDirection = cursorDirection;
    }
    
    public boolean validate(){
        return cursorDirection.size() != outParm.size() && outParm.size() != inParm.size();
    }
    
    public int getNextEstado() {
        return nextEstado;
    }

    public void setNextEstado(int nextEstado) {
        this.nextEstado = nextEstado;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getNumParm() {
        return numParm;
    }

    public void setNumParm(int numParm) {
        this.numParm = numParm;
    }

    public List<Character> getInParm() {
        return inParm;
    }

    public void setInParm(List<Character> inParm) {
        this.inParm = inParm;
    }

    public List<Character> getOutParm() {
        return outParm;
    }

    public void setOutParm(List<Character> outParm) {
        this.outParm = outParm;
    }

    public List<Integer> getCursorDirection() {
        return cursorDirection;
    }

    public void setCursorDirection(List<Integer> cursorDirection) {
        this.cursorDirection = cursorDirection;
    }

    @Override
    public String toString() {
        //(q1, (þ, þ)) = (q5, (þ, þ), (S, S))
        String fString = null;
        fString = "(q"+this.estado+", ";
        fString += "(";
        for(int x = 0 ; x < this.inParm.size() ; x++){
            if(x < this.inParm.size() - 1){
                fString += inParm.get(x) + ", ";
            }else{
                fString += inParm.get(x) + ")";
            }
        }
        
        fString += ") = (q"+this.nextEstado+", (";
        
        for(int x = 0 ; x < this.outParm.size() ; x++){
            if(x < this.outParm.size() -1 ){
                fString += outParm.get(x) + ", ";
            }else{
                fString += outParm.get(x) +")";
            }
        }
        
        fString +=", (";
        
        for(int x = 0 ; x < this.cursorDirection.size() ; x++){
            if(this.cursorDirection.get(x) == Cinta.LEFT){
                fString += "L";
            }else if(this.cursorDirection.get(x) == Cinta.RIGHT){
                fString += "R";
            }else if(this.cursorDirection.get(x) == Cinta.NO_MOVE){
                fString += "S";
            }
            
            if(x < this.cursorDirection.size() -1){
                fString += ", ";
            }else{
                fString += ")";
            }
        }
        
        fString += ")";
        
        return fString;
    }
}
