package com.root32.dentalproductservice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.AdminStockInward;

@Repository
public interface AdminStockInwardRepository extends JpaRepository<AdminStockInward, Long> {

	List<AdminStockInward> findAll();

//	playListRepository.findByPlaylistItemsVideoIsDeleted(false);

	Page<AdminStockInward> findAllByProductNameContaining(Pageable pageable, String search);

	Page<AdminStockInward> findAllByBatchBatchCodeContaining(Pageable pageable, String search);

	Page<AdminStockInward> findAllByManufacturerBusinessNameContaining(Pageable pageable, String search);

	Page<AdminStockInward> findAllByQuantity(Pageable pageable, Long quantity);

}
