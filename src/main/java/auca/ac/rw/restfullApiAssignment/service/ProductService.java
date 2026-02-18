package auca.ac.rw.restfullApiAssignment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.ac.rw.restfullApiAssignment.modal.Product;
import auca.ac.rw.restfullApiAssignment.repository.ProductRepository;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepo; 

    public String saveProduct(Product product){

        Optional<Product> checkProduct = productRepo.findById(product.getId());
        
        if(checkProduct.isPresent()){
            return "Product with id "+ product.getId() + " already exists.";
        }else{
                 productRepo.save(product);
                 return "Product saved successfully.";
        }
       

    }

    public List<Product> saveAll(List<Product> products){
        return productRepo.saveAll(products);
    }

    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }

    public Optional<Product> getProductById(Long id){
        return productRepo.findById(id);
    }

    public String updateProduct(Long id, Product product){
        Optional<Product> existingOpt = productRepo.findById(id);
        if(!existingOpt.isPresent()){
            return "Product not found.";
        }

        Product existing = existingOpt.get();
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setCategory(product.getCategory());
        existing.setStockQuantity(product.getStockQuantity());

        productRepo.save(existing);
        return "Product updated successfully.";
    }

    public String deleteProduct(Long id){
        Optional<Product> existingOpt = productRepo.findById(id);
        if(!existingOpt.isPresent()){
            return "Product not found.";
        }
        productRepo.deleteById(id);
        return "Product deleted successfully.";
    }
}
