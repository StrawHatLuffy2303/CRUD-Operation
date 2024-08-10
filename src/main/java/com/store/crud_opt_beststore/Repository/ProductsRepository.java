package com.store.crud_opt_beststore.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.store.crud_opt_beststore.Model.Product;

@Repository
public interface ProductsRepository extends JpaRepository<Product,Long> {

    
}
