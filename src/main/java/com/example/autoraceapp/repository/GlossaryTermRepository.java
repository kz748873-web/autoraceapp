package com.example.autoraceapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.autoraceapp.entity.GlossaryTerm;

public interface GlossaryTermRepository extends JpaRepository<GlossaryTerm, Long> {
}
