
package co.edu.unicauca.openmarket.main;

import co.edu.unicauca.openmarket.access.Factory;

import co.edu.unicauca.openmarket.access.IRepository;
import co.edu.unicauca.openmarket.domain.service.CategoryService;
import co.edu.unicauca.openmarket.domain.service.ProductService;
import co.edu.unicauca.openmarket.presentation.GUIProducts;

/**
 *
 * @author Libardo Pantoja
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        IRepository repository = Factory.getInstance().getRepository("default");
        ProductService productService = new ProductService(repository);
        CategoryService categoryService = new CategoryService(repository);
        
        GUIProducts instance = new GUIProducts(productService,categoryService);
        instance.setVisible(true);
    }
    
}
