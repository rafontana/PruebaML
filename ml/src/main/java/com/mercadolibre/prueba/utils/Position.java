/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mercadolibre.prueba.utils;

/**
 *
 * @author rafon
 */
public class Position {

    private int row;
    private int col;

    public void incrementCol(){
        col++;
    }
 
    public void decrementRow(){
        row--;
    }
    
        public void decrementCol(){
        col--;
    }
 
    public void incrementRow(){
        row++;
    }
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

}
