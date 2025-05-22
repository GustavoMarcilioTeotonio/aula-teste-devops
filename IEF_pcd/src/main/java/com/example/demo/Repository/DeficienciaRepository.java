package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.Model.Deficiencia;

import java.util.List;

@Repository
public interface DeficienciaRepository extends JpaRepository<Deficiencia, Long> {

    List<Deficiencia> findByAtivo(Boolean ativo);
}
