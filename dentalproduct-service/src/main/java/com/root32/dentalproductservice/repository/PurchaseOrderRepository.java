package com.root32.dentalproductservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.PurchaseOrder;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

	PurchaseOrder findTopByOrderByIdDesc();

	List<PurchaseOrder> findAllByisRecieved(boolean isRecieved);

	Optional<PurchaseOrder> findByPoCode(String poCode);

	List<PurchaseOrder> findAllByIsRecieved(boolean b);

//	PurchaseOrder findByPoCode(String poCode);

}
