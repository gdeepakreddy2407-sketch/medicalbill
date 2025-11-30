package com.deepak.BillingService.repository;


import com.deepak.BillingService.Entity;
import com.deepak.BillingService.dto.RequestList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BillingRepository extends MongoRepository<Entity, String> {
    List<Entity> findByNameIn(List<String> names);
}
