package com.example.kpi.KpiRepository;

import com.example.kpi.KpiEntity.KpiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KpiRepository extends JpaRepository<KpiEntity,Integer> {
    Optional<KpiEntity> findByUserName(String name);

    void removeByUserName(String name);
}
