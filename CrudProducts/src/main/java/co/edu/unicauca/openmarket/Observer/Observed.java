/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.openmarket.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Janier
 */
public class Observed {
    
    private final List<IObserver> observadores = new ArrayList<>();

    public void addObserver(IObserver observer) {
        this.observadores.add(observer);
    }

    public void notificar() {
        for (IObserver observer : observadores) {
            observer.update();
        }
    }
}
