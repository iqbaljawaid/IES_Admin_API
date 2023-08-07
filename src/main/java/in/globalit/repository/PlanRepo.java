package in.globalit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.globalit.entity.PlanEntity;

public interface PlanRepo extends JpaRepository<PlanEntity, Integer> {

}
