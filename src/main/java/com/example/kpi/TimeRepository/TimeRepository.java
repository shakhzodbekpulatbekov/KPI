package com.example.kpi.TimeRepository;

import com.example.kpi.TimeEntity.TimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeRepository extends JpaRepository<TimeEntity,Integer> {
    List<TimeEntity> findByDate(String date);

    void deleteByUserName(String name);
    List<TimeEntity>findByUserName(String name);

    List<TimeEntity> findByUserName(String name);

}
