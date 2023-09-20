/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.edu.unicauca.openmarket.access;

import co.edu.unicauca.openmarket.domain.Category;
import java.util.List;
/**
 *
 * @author Janier
 */
public interface ICategoryRepository {
    boolean saveCategory(Category newCategory);
    
    boolean editCategory(Long id, Category category);
    
    boolean deleteCategory(Long id);

    Category findCategoryById(Long id);
    
    Category findCategoryByName(String name);
    
    List<Category> findAllCategories();
}
