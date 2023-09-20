/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.openmarket.access;


import co.edu.unicauca.openmarket.domain.Category;
import co.edu.unicauca.openmarket.domain.Product;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Janier
 */
public class Repository implements IRepository{
    private Connection conn;

    public Repository() {
        initDatabase();
    }
    
    //Manejo de la base de datos
    @Override
    public void connect() {
        // SQLite connection string
        //String url = "jdbc:sqlite:./myDatabase.db"; //Para Linux/Mac
        //String url = "jdbc:sqlite:C:/sqlite/db/myDatabase.db"; //Para Windows
        String url = "jdbc:sqlite::memory:";

        try {
            conn = DriverManager.getConnection(url);

        } catch (SQLException ex) {
            Logger.getLogger(Repository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void disconnect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }
    @Override
    public void initDatabase() {
        String createProductsTableSQL = "CREATE TABLE IF NOT EXISTS products (" +
                                        "productId integer PRIMARY KEY AUTOINCREMENT," +
                                        "name text NOT NULL," +
                                        "description text NULL," +
                                        "categoryId integer," +
                                        "FOREIGN KEY (categoryId) REFERENCES category(categoryId)" +
                                        ");";
        
        String createCategoryTableSQL = "CREATE TABLE IF NOT EXISTS category (" +
                                        "categoryId integer PRIMARY KEY AUTOINCREMENT," +
                                        "name text NOT NULL" +
                                        ");";

        try {
            this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(createCategoryTableSQL);
            stmt.execute(createProductsTableSQL);
        } catch (SQLException ex) {
            Logger.getLogger(Repository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    //Manejo de los productos
    @Override
    public boolean saveProduct(Product newProduct) {
        if (newProduct == null || newProduct.getName().isEmpty()) {
            return false;
        }

        try {
            String sql = "INSERT INTO products (name, description, categoryId) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newProduct.getName());
            pstmt.setString(2, newProduct.getDescription());
            pstmt.setLong(3, newProduct.getCategory().getCategoryId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Repository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    @Override
    public boolean editProduct(Long id, Product product) {
        try {
            //Validate product
            if (id <= 0 || product == null) {
                return false;
            }
            //this.connect();

            String sql = "UPDATE  products "
                    + "SET name=?, description=? ,categoryId=?"
                    + "WHERE productId = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setLong(3, product.getCategory().getCategoryId());
            pstmt.setLong(4, id);
            pstmt.executeUpdate();
            //this.disconnect();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Repository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    @Override
    public boolean deleteProduct(Long id) {
        try {
            //Validate product
            if (id <= 0) {
                return false;
            }
            //this.connect();

            String sql = "DELETE FROM products "
                    + "WHERE productId = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            //this.disconnect();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Repository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    @Override
    public Product findProductById(Long id) {
        try {

            String sql = "SELECT p.productId, p.name AS productName, p.description, c.categoryId, c.name AS categoryName "
                   + "FROM products p "
                   + "LEFT JOIN category c ON p.categoryId = c.categoryId "
                   + "WHERE p.productId = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                 Product prod = new Product();
                prod.setProductId(res.getLong("productId"));
                prod.setName(res.getString("productName"));
                prod.setDescription(res.getString("description"));
                
                Category ctg = new Category();
                ctg.setCategoryId(res.getLong("categoryId"));
                ctg.setName(res.getString("categoryName"));
                prod.setCategory(ctg);

                return prod;
            } else {
                return null;
            }
            //this.disconnect();

        } catch (SQLException ex) {
            Logger.getLogger(Repository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
     @Override
    public List<Product> findProductByName(String name) {
        List<Product> products = new ArrayList<>();

        try {
            String sql = "SELECT p.productId, p.name AS productName, p.description, c.categoryId, c.name AS categoryName "
                    + "FROM products p "
                    + "INNER JOIN category c ON p.categoryId = c.categoryId "
                    + "WHERE p.name LIKE ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product newProduct = new Product();
                newProduct.setProductId(rs.getLong("productId"));
                newProduct.setName(rs.getString("productName"));
                newProduct.setDescription(rs.getString("description"));

                Category ctg = new Category();
                ctg.setCategoryId(rs.getLong("categoryId"));
                ctg.setName(rs.getString("categoryName"));

                newProduct.setCategory(ctg);
                products.add(newProduct);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Repository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return products;
    }
    @Override
    public List<Product> findProductByCategory(String name) {
        List<Product> products = new ArrayList<>();
        try {
            String sql = "SELECT p.productId, p.name AS productName, p.description, c.categoryId, c.name AS categoryName "
                    + "FROM products p "
                    + "INNER JOIN category c ON p.categoryId = c.categoryId "
                    + "WHERE c.name LIKE ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product newProduct = new Product();
                newProduct.setProductId(rs.getLong("productId"));
                newProduct.setName(rs.getString("productName"));
                newProduct.setDescription(rs.getString("description"));

                Category ctg = new Category();
                ctg.setCategoryId(rs.getLong("categoryId"));
                ctg.setName(rs.getString("categoryName"));

                newProduct.setCategory(ctg);
                products.add(newProduct);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Repository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return products;
    }

    @Override
    public List<Product> findAllProducts() {
        List<Product> products = new ArrayList<>();
        
        try {
            String sql = "SELECT p.productId, p.name AS productName, p.description, c.categoryId, c.name AS categoryName " +
                         "FROM products p " +
                         "LEFT JOIN category c ON p.categoryId = c.categoryId";
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Product newProduct = new Product();
                newProduct.setProductId(rs.getLong("productId"));
                newProduct.setName(rs.getString("productName"));
                newProduct.setDescription(rs.getString("description"));
                
                Category ctg = new Category();
                ctg.setCategoryId(rs.getLong("categoryId"));
                ctg.setName(rs.getString("categoryName"));
                
                newProduct.setCategory(ctg);
                products.add(newProduct);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Repository.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return products;
    }

    //Manejo de las categorias

    @Override
    public boolean saveCategory(Category newCategory) {
        if (newCategory == null || newCategory.getName().isEmpty()) {
            return false;
        }

        try {
            String sql = "INSERT INTO category (name) VALUES (?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newCategory.getName());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Repository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    @Override
    public boolean editCategory(Long id, Category category) {
        if (id <= 0 || category == null) {
            return false;
        }

        try {
            String sql = "UPDATE category SET name=? WHERE categoryId=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category.getName());
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Repository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    @Override
    public boolean deleteCategory(Long id) {
        if (id <= 0) {
            return false;
        }

        try {
            String sql = "DELETE FROM category WHERE categoryId=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Repository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }


    @Override
    public Category findCategoryById(Long id) {
        if (id <= 0) {
            return null;
        }

        try {
            String sql = "SELECT * FROM category WHERE categoryId=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                Category ctg = new Category();
                ctg.setCategoryId(res.getLong("categoryId"));
                ctg.setName(res.getString("name"));
                return ctg;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Repository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    public Category findCategoryByName(String name) {
        
        Category category = null;

        try {
            String sql = "SELECT categoryId, name FROM category WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                category = new Category();
                category.setCategoryId(rs.getLong("categoryId"));
                category.setName(rs.getString("name"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Repository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return category;
       
    }
    
    @Override
    public List<Category> findAllCategories() {
        List<Category> categories = new ArrayList<>();

        try {
            String sql = "SELECT * FROM category";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Category ctg = new Category();
                ctg.setCategoryId(rs.getLong("categoryId"));
                ctg.setName(rs.getString("name"));

                categories.add(ctg);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Repository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return categories;
    }
    
}
