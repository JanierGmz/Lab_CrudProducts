/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.openmarket.domain.service;

import co.edu.unicauca.openmarket.Observer.Observed;
import co.edu.unicauca.openmarket.access.ICategoryRepository;
import co.edu.unicauca.openmarket.domain.Category;
import java.util.List;

/**
 *
 * @author Janier
 */
public class CategoryService extends Observed{
    private ICategoryRepository repository;
    
    
     /**
     * Inyección de dependencias en el constructor. Ya no conviene que el mismo
     * servicio cree un repositorio concreto
     *
     * @param repository una clase hija de ICategoryRepository
     */
    public CategoryService(ICategoryRepository repository) {
        this.repository = repository;
    }


    public boolean saveCategory(String name) {
        
        Category newCategory = new Category();
        newCategory.setName(name);
        
        //Validate product
        if (newCategory.getName().isEmpty()) {
            return false;
        }
        boolean state=repository.saveCategory(newCategory);
        this.notificar();
        
        return state;

    }
    public boolean editCategory(Long productId, Category cat) {
        //Validate product
        if (cat == null || cat.getName().isEmpty()) {
            return false;
        }
        boolean state=repository.editCategory(productId, cat);
        this.notificar();
        
        return state;
    }
     public boolean deleteCategory(Long id){
        boolean state=repository.deleteCategory(id);
        this.notificar();
        
        return state;
    }

    public Category findCategoryById(Long id){
        return repository.findCategoryById(id);
    }
    
    public Category findCategoryByName(String name){
        
        return repository.findCategoryByName(name);
    }
    public List<Category> findAllCategories() {
        return repository.findAllCategories();
    }
    
}
