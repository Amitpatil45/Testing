package com.root32.dentalproductservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.ProductBarcode;

@Repository
public interface ProductBarcodeRepository extends JpaRepository<ProductBarcode, Long> {

	Optional<ProductBarcode> findByBelongsToBatchId(Long id);

	Optional<ProductBarcode> findByProductBarcode(String productBarcode);

}
