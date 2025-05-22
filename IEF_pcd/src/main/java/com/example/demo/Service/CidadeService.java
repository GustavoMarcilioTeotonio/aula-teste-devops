package com.example.demo.Service;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class CidadeService {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    public CidadeService(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void inicializarCidades() {
        // Verifica se a tabela está vazia
        String verificaTabelaVazia = "SELECT COUNT(*) FROM cidade";
        Integer count = jdbcTemplate.queryForObject(verificaTabelaVazia, Integer.class);

        if (count != null && count == 0) {
            try {
                ScriptUtils.executeSqlScript(dataSource.getConnection(),
                        new ClassPathResource("sql/cidades.sql"));
                System.out.println("Cidades inseridas!");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Erro ao executar script SQL: " + e.getMessage());
            }
        } else {
            System.out.println("Tabela de cidades já está populada.");
        }
    }

}
