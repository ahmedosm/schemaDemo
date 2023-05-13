package com.schema.schemaDemo.repository;


import com.schema.schemaDemo.entity.FallBack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FallBackRepository extends JpaRepository<FallBack,String> {
    List<FallBack> findByStatusAndReTryCountLessThanEqual(String status,Integer reTryCount);
}
