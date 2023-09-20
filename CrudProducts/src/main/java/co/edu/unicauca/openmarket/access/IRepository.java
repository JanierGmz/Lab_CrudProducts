/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.edu.unicauca.openmarket.access;

/**
 *
 * @author Janier
 */
public interface IRepository extends IProductRepository,ICategoryRepository{
    
    public void connect();
    
    public void disconnect();
    
    public  void initDatabase();
    
}
