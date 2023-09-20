package co.edu.unicauca.openmarket.domain.service;


import co.edu.unicauca.openmarket.access.IProductRepository;
import co.edu.unicauca.openmarket.domain.Category;
import co.edu.unicauca.openmarket.domain.Product;
import java.util.List;

/**
 *
 * @author Libardo, Julio
 */
public class ProductService {

     // Ahora hay una dependencia de una abstracción, no es algo concreto,
    // no sabe cómo está implementado.
    private IProductRepository repository;

    /**
     * Inyección de dependencias en el constructor. Ya no conviene que el mismo
     * servicio cree un repositorio concreto
     *
     * @param repository una clase hija de IProductRepository
     */
    public ProductService(IProductRepository repository) {
        this.repository = repository;
    }


    public boolean saveProduct(String name, String description,Long categoryId) {
        
        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setDescription(description);
        Category ctg = new Category();
        ctg.setCategoryId(categoryId);
        newProduct.setCategory(ctg);
        //Validate product
        if (newProduct.getName().isEmpty() ) {
            return false;
        }

        return repository.saveProduct(newProduct);

    }

    public boolean editProduct(Long productId, Product prod) {
        
        //Validate product
        if (prod == null || prod.getName().isEmpty() ) {
            return false;
        }
        return repository.editProduct(productId, prod);
    }
    public boolean deleteProduct(Long id){
        return repository.deleteProduct(id);
    }
    
    public Product findProductById(Long id){
        return repository.findProductById(id);
    }
    public List<Product> findProductByName(String name) {
        return repository.findProductByName(name);
    }
    public List<Product> findAllProducts() {
        return repository.findAllProducts();
    }
    public List<Product> findProductByCategory(String category){
        return repository.findProductByCategory(category);
    }
}
