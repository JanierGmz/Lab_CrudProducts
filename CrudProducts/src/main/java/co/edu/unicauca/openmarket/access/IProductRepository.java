
package co.edu.unicauca.openmarket.access;

import co.edu.unicauca.openmarket.domain.Product;
import java.util.List;

/**
 *
 * @author Libardo, Julio
 */
public interface IProductRepository {
    boolean saveProduct(Product newProduct);
    
    boolean editProduct(Long id, Product product);
    
    boolean deleteProduct(Long id);

    Product findProductById(Long id);
    
    List<Product> findProductByName(String name);
    
    List<Product> findProductByCategory(String name);
    
    List<Product> findAllProducts();
    
   
}
