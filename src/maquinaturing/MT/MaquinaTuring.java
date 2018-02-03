package maquinaturing.MT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MaquinaTuring {
    private List<Cinta> cintas;
    private List<Estado> estados;
    private int estadoActual;
    private int estadoInicial;
    private int estadoFinal;
    private Estado estadoActualObj;
    public boolean termino = false;
    private List<CellAffected> cellAffecteds;
    private List<Cinta> cintasMoved;
    private String inputUser;
    
    public int PHASE_SEARCH = 1;
    public int PHASE_REPLACE = 2;
    public int PHASE_MOVE = 3;
    
    private int status;
    
    public MaquinaTuring(int numCintas, int estadoInicial, int estadoFinal, String cadena){
        cintas = new ArrayList<>();
        cellAffecteds = new ArrayList<>();
        cintasMoved = new ArrayList<>();
        inputUser = cadena;
        
        this.estados = new ArrayList<Estado>();
        this.estadoActual = estadoInicial;
        this.estadoInicial = estadoInicial;
        this.estadoFinal = estadoFinal;
        
        for(int x = 0 ; x < numCintas ; x++){
            cintas.add(new Cinta());
        }
        
        cintas.get(0).createCinta(cadena);
    }
    
    public Estado futureState(){
        System.out.println("futureState invoked");
        List<Estado> estados = searchEstadosByNumber(estadoActual);
        boolean eval = false;
        
        for(Estado estado : estados){

            for(int mIt = 0 ; mIt < cintas.size() ; mIt++){
                if(estado.getInParm().get(mIt).equals(cintas.get(mIt).getChar())){
                    //estadoActual = estado.getNextEstado();
                    
                    eval = true;
                    return estado;
                }else{
                    eval = false;
                    break;
                }
            }

            if(!eval){
                continue;
            }
            
            break;
        }
        
        return null;
    }
    
    public boolean nextStep(int phase){
        System.out.println("nextStep invoked");
        cellAffecteds.clear();
        cintasMoved.clear();
        List<Estado> estados = searchEstadosByNumber(estadoActual); //Obtengo el obj estado actual
        boolean eval = false;
        
        Estado estadoChecked = checkInputParms(estados.stream().toArray(Estado[]::new));
        
        if(estadoChecked != null){ //Estado valido
            //Actualizar valor en las cintas
            for(int x = 0 ; x < cintas.size() ; x++){
                cintas.get(x).updateChar(estadoChecked.getOutParm().get(x));
                cellAffecteds.add(new CellAffected(cintas.get(x), cintas.get(x).getCursorValue()));
            }
            
            //Mover cinta
            for(int x = 0 ; x < cintas.size() ; x++){
                if(estadoChecked.getCursorDirection().get(x) != Cinta.NO_MOVE)  cintasMoved.add(cintas.get(x));
                cintas.get(x).next(estadoChecked.getCursorDirection().get(x));
                
            }
            
            estadoActual = estadoChecked.getNextEstado();
            estadoActualObj = estadoChecked;
            eval = true;
        }else{ //No encontro estado
            
        }
        
        if(estadoActual == estadoFinal){
            System.out.println("Llego al estado de aceptación");
            termino = true;
        }
        
        return eval;
    }
    
    /*
        Esta función busca el candidato al estado cuyos parametros de entrada
        coincidan con los simbolos que hay en las cintas
        |a|b|c|a|b|c|
        (q1, (a)) = (q2, (b), (S))
              ^
            inParm
        Recibe por parametro una lista de estados que coinciden con el estado
        actual.
        Regresa un objeto de tipo Estado en caso de que haya estado valido y
            un null en caso de que no lo haya, por lo tanto se entiende que
            la maquina de turing y a no puede seguir.
    */
    public Estado checkInputParms(Estado... estados){
        boolean valid = false;
        
        for(Estado estado : estados){
            for(int x = 0 ; x < cintas.size() ; x++){ //El numero de estados a validar es exactamente el mismo a la cantidad de cintas que hay en la maquina de turing.
                //Compruebo que la entrada de caracteres sea el mismo que el valor que hay en la cinta.
                if(estado.getInParm().get(x).equals(cintas.get(x).getChar())){
                    valid = true;
                }else{
                    valid = false;
                    //En cuanto una parametro de entrada sea diferente al 
                    //  simbolo
                    //  que hay en la cinta, el estado se descarta y se busca 
                    //  el siguiente estado, por lo tanto, el for se estara
                    //  ciclando hasta que encuentre un estado valido y ya no haya mas estados que verificar.
                    break;
                }
            }
            
            if(valid){//regresa el estado valido
                return estado;
            }
        }
        
        return null;
    }
    
    public List<CellAffected> getCellAffecteds(){
        return cellAffecteds;
    }
    
    public void reset(){
        termino = false;
        estadoActual = estadoInicial;
        cellAffecteds.clear();
        
        cintas.get(0).createCinta(inputUser);
        
        for(int x = 1 ; x < cintas.size() ; x++){
            cintas.get(x).reset();
        }
    }
    
    public void setInputUser(String cadena){
        this.inputUser = cadena;
    }
    
    public List<Estado> searchEstadosByNumber(int number){
        List<Estado> estados = new ArrayList<Estado>();
        
        for(Estado estado : this.estados){
            if(estado.getEstado() == number){
                estados.add(estado);
            }
        }
        
        return estados;
    }

    public List<Cinta> getCintas() {
        return cintas;
    }
    
    public List<Cinta> getCintasMoved(){
        return cintasMoved;
    }

    public List<Estado> getEstados() {
        return estados;
    }
    
    public void setEstados(List<Estado> estados){
        this.estados = estados;
    }
    
    public Estado getEstadoActualObj(){
        return estadoActualObj;
    }

    public int getEstadoActual() {
        return estadoActual;
    }

    public int getEstadoFinal() {
        return estadoFinal;
    }
    
    public int getStatus(){
        return status;
    }

    @Override
    public String toString() {        
        return "MaquinaTuring{" + "cintas=" + cintas + ", estados=" + estados + ", estadoActual=" + estadoActual + ", estadoFinal=" + estadoFinal + ", termino=" + termino + '}';
    }
    
    
    
}
