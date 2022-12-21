package com.example.kpi.TimeRepository;

import com.example.kpi.TimeEntity.TimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TimeRepository extends JpaRepository<TimeEntity,Integer> {
    List<TimeEntity> findByDate(String date);

    List<TimeEntity>findByUserName(String name);

    @Query(value = "select * from time_entity order by id", nativeQuery = true)
    List<TimeEntity> findAllSorted();

}
