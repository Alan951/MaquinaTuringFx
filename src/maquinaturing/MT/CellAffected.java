/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maquinaturing.MT;

/**
 *
 * @author Ck
 */
public class CellAffected {
    private Cinta cinta;
    private Integer indexCell;

    public CellAffected(Cinta cinta, Integer indexCell) {
        this.cinta = cinta;
        this.indexCell = indexCell;
    }
    
    public Cinta getCinta() {
        return cinta;
    }

    public void setCinta(Cinta cinta) {
        this.cinta = cinta;
    }

    public Integer getIndexCell() {
        return indexCell;
    }

    public void setIndexCell(Integer indexCell) {
        this.indexCell = indexCell;
    }

    @Override
    public String toString() {
        return "CellAffected{" + "cinta=" + cinta + ", indexCell=" + indexCell + '}';
    }
    
}
