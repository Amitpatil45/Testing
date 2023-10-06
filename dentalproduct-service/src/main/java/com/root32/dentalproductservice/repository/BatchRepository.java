package com.root32.dentalproductservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.root32.entity.Batch;
import com.root32.entity.PurchaseOrder;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {

	List<Batch> findByBelongsToPo(PurchaseOrder purchaseOrderr);

	List<Batch> findAllByBelongsToPoAndIsRecieved(Long id, boolean isRecieved);

	List<Batch> findAllByBelongsToPoIdAndIsRecieved(Long id, boolean b);

	List<Batch> findAllByBatchCodeContainsAndIsRecieved(String poCode, boolean isRecieved);

	@Query("SELECT p FROM PurchaseOrder p WHERE p.poCode LIKE :poCode AND p.isRecieved= :isRecieved")
	List<Batch> fetchBatchesByPoCodeAndIsRecieved(String poCode, boolean isRecieved);

	Optional<Batch> findByBatchCode(String batchCode);

	Long countByIsRecievedAndBelongsToPoId(boolean b, Long id);

	Long countByProductId(Long id);

}
