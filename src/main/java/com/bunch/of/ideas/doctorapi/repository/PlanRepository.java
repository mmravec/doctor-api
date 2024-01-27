package com.bunch.of.ideas.doctorapi.repository;


import com.bunch.of.ideas.doctorapi.entity.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends CrudRepository<Plan, String> {

    Optional<Plan> findPlanByEmail(String email);
}
