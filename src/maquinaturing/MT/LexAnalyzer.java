/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maquinaturing.MT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Ck
 */
public class LexAnalyzer {
    
    //private  final String MT_INSTRUCTION = "^(\\(q[0-9]+\\,\\((([0-9]|[a-zA-Z])(,([0-9]|[a-zA-Z]))*)\\)\\)\\=\\(q[0-9]+\\,\\((([0-9]|[a-zA-Z])(,([0-9]|[a-zA-Z]))*)\\),(L|R|S)\\))*$";
    private final String MT_INSTRUCTION_CHECK = "^(\\(q[0-9]+\\,\\((([0-9]|[a-zA-Z]|þ)(,([0-9]|[a-zA-Z]|þ))*)\\)\\)\\=\\(q[0-9]+\\,\\((([0-9]|[a-zA-Z]|þ)(,([0-9]|[a-zA-Z]|þ))*)\\),\\(((L|R|S)(,(L|R|S))*)*\\)\\))*$";
    private final String MT_START_CHECK = "^(S=q[0-9]+)$";
    private final String MT_FINAL_CHECK = "^(F=((q[0-9]+)|\\{q[0-9]+\\}|\\{(q[0-9]+(,(q[0-9]))*)*\\}))$";
    private final String MT_FINAL = "[0-9]+";
    private final String MT_STATES = "q[0-9]";
    private final String MT_PARMS = "\\((([0-9]|[a-zA-Z]|þ)(,)*)*\\)";
    private final String MT_DIRECTION = "\\(((S|L|R)(,)*)*\\)";
    private final String COMMENT = "^(\\/\\/.*)$";
    
    public static final String MT_IN_PARMS = "PARMS_IN";
    public static final String MT_OUT_PARMS = "PARMS_OUT";
    
    private int numParms = -1;
    
    private int startState;
    private int endState;
    
    public LexAnalyzer(){}
    
    public LexAnalyzer(String instructionLine){}
    
    public LexAnalyzer(String... instructionsArray){}
    
    public boolean validateMTI(String instruction){
        Pattern pattern = Pattern.compile(MT_INSTRUCTION_CHECK);
        boolean valid = false;
        
        if(instruction.matches(COMMENT)){
            return true;
        }
        
        Matcher matcher = pattern.matcher(instruction);
        
        valid = matcher.matches();
        
        if(!valid){
            matcher = Pattern.compile(MT_START_CHECK).matcher(instruction);
            
            if(matcher.matches()){
                matcher = Pattern.compile(MT_FINAL).matcher(instruction);
                matcher.find();
                startState = Integer.parseInt(matcher.group());
                
                return true;
            }
            
            matcher = Pattern.compile(MT_FINAL_CHECK).matcher(instruction);
            
            if(matcher.matches()){
                matcher = Pattern.compile(MT_FINAL).matcher(instruction);
                matcher.find();
                endState = Integer.parseInt(matcher.group());
                
                return true;
            }
        }
        
        if(numParms == -1 && valid){
            Map<String, List<Character>> parms = getParms(instruction);
            
            numParms = parms.get(MT_IN_PARMS).size();
            
            valid = (parms.get(MT_IN_PARMS).size() == numParms && parms.get(MT_OUT_PARMS).size() == numParms && getDirections(instruction).size() == numParms);
        }else if(valid){
            Map<String, List<Character>> parms = getParms(instruction);
            
            valid = (parms.get(MT_IN_PARMS).size() == numParms && parms.get(MT_OUT_PARMS).size() == numParms && getDirections(instruction).size() == numParms);
        }
        
        return valid;
    }
    
    public Estado instructionToState(String instruction){
        if(instruction.matches(MT_START_CHECK) || instruction.matches(MT_FINAL_CHECK) || instruction.matches(COMMENT)) return null;
        
        List<Integer> states = getStates(instruction);
        Map<String, List<Character>> parms = getParms(instruction);
        List<Integer> directions = getDirections(instruction);
        
        int state = states.get(0);
        int nextState = states.get(1);
        
        List<Character> inParms = parms.get(MT_IN_PARMS);
        List<Character> outParms = parms.get(MT_OUT_PARMS);
        
        return new Estado(state, nextState, inParms, outParms, directions);
    }
    
    private List<Integer> getStates(String instruction){
        List<Integer> states = new ArrayList<>();
        String fullString = null;
        Pattern pattern = Pattern.compile(MT_STATES);
        
        Matcher matcher = pattern.matcher(instruction);
        
        fullString = matcher.find() ? matcher.group() : null;
        
        states.add(Integer.parseInt(fullString.replace("q", "")));
        
        fullString = matcher.find() ? matcher.group() : null;
        
        states.add(Integer.parseInt(fullString.replace("q", "")));
                
        return states;
    }
    
    private Map<String, List<Character>> getParms(String instruction){
        Map<String, List<Character>> parms = new HashMap<>();
        List<Character> charsIn = new ArrayList<>();
        List<Character> charsOut = new ArrayList<>();
        String fullString = null;
        
        Pattern pattern = Pattern.compile(MT_PARMS);
        
        Matcher matcher = pattern.matcher(instruction);
        
        fullString = matcher.find() ? matcher.group() : null;
        
        for(String strChar : fullString.replaceAll("\\(|\\)", "").split(",")){
            charsIn.add(strChar.charAt(0));
        }
        
        fullString = matcher.find() ? matcher.group() : null;
        
        for(String strChar : fullString.replaceAll("\\(|\\)", "").split(",")){
            charsOut.add(strChar.charAt(0));
        }
        
        parms.put("PARMS_IN", charsIn);
        parms.put("PARMS_OUT", charsOut);
        
        return parms;
    }
    
    public int getNumCintas(){
        return numParms;
    }
    
    public int getStartState(){
        return startState;
    }
    
    public int getEndState(){
        return endState;
    }
    
    private List<Integer> getDirections(String instruction){
        List<Integer> listDirections = new ArrayList<Integer>();
        String fullString = null;
        
        Pattern pattern = Pattern.compile(MT_DIRECTION);
        
        Matcher matcher = pattern.matcher(instruction);
        
        fullString = matcher.find() ? matcher.group() : null;
        
        for(String strChar : fullString.replaceAll("\\(|\\)", "").split(",")){
            int direction = 0;
            
            if(strChar.equals("L")){
                direction = -1;
            }else if(strChar.equals("R")){
                direction = 1;
            }else if(strChar.equals("S")){
                direction = 0;
            }else{
                direction = -13;
            }
            
            listDirections.add(direction);
        }
        
        return listDirections;
    }
}
