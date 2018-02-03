/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maquinaturing.MT;

import java.util.Map;

/**
 *
 * @author Ck
 */
public class Step {
    private int currentStep;
    private int totalSteps;
    private Map<Integer, String> stepName;
    
    public Step(){
        
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public String getStepName(int step) {
        return stepName.get(step);
    }
    
    public Map<Integer, String> getMapStepNames(){
        return stepName;
    }
    
    public void setMapStepNames(Map<Integer, String> map){
        this.stepName = map;
    }

    @Override
    public String toString() {
        return "Step{" + "currentStep=" + currentStep + ", totalSteps=" + totalSteps + ", stepName=" + stepName + '}';
    }
}
