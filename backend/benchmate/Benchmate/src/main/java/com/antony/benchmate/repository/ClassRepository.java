package com.antony.benchmate.repository;

import com.antony.benchmate.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<ClassEntity,Integer> {
}
