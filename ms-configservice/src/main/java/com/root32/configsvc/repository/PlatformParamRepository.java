package com.root32.configsvc.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.root32.dto.PlatformParamTypeDto;
import com.root32.entity.PlatformParam;

@Repository
public interface PlatformParamRepository extends JpaRepository<PlatformParam, Long> {
    PlatformParam findByParamKey(String appKey);

    List<PlatformParam> findByType(String type);

    List<PlatformParam> findAllByOrderByType();

    @Query(value = "SELECT type as type, updated_date as updatedDate FROM `platform_param`  GROUP BY type", nativeQuery = true)
    Page<PlatformParamTypeDto> findAllGroupedByType(Pageable pageable);

}
