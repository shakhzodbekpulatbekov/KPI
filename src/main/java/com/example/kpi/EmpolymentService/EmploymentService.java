package com.example.kpi.EmpolymentService;

import com.example.kpi.KpiEntity.KpiEntity;
import com.example.kpi.KpiRepository.KpiRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class EmploymentService {
    final KpiRepository kpiRepository;
    final KpiEntity kpiEntity;

    public EmploymentService(KpiRepository kpiRepository, KpiEntity kpiEntity) {
        this.kpiRepository = kpiRepository;
        this.kpiEntity = kpiEntity;
    }

    public boolean employmentService(String name, String time){
        Optional<KpiEntity> byUserName = kpiRepository.findByUserName(name);
        KpiEntity kpiEntity = new KpiEntity();
        if (byUserName.isPresent()){
            return false;
        }else {
            kpiEntity.setUserName(name);
            kpiEntity.setTimeOf(time);
            kpiRepository.save(kpiEntity);
            return true;
        }
    }
}
