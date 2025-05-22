package com.example.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.Model.Deficiencia;
import com.example.demo.Repository.DeficienciaRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Timestamp;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class DeficienciaTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeficienciaRepository dr;

    @Test
    @DisplayName("Verificar se o endpoint /deficiencia responde 200 (OK)")
    void index() throws Exception {
        mockMvc.perform(
            get("/deficiencia")
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Adiciona e altera uma deficiência.")
    void createAndUpdate() throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        mockMvc.perform(
            post("/deficiencia/create")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("nome", "Teste " + timestamp)
            .param("categoria", "1")
        ).andExpect(status().is3xxRedirection());

        List<Deficiencia> deficiencias = dr.findAll();

        assertNotNull(deficiencias);

        Deficiencia deficiencia = deficiencias
        .get(deficiencias.size() - 1);

        assertEquals(deficiencia.getNome(), "Teste " + timestamp);
        assertEquals(deficiencia.getCategoria().getId(), 1L);

        mockMvc.perform(
            post("/deficiencia/update/" + deficiencia.getId())
            .param("nome", "TESTE ALTERAÇÃO " + timestamp)
            .param("categoria", "2")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        ).andExpect(status().is3xxRedirection());

        deficiencia = dr.findById(deficiencia.getId()).orElseThrow();

        assertNotNull(deficiencia);

        assertEquals(deficiencia.getNome(), "TESTE ALTERAÇÃO " + timestamp);
        assertEquals(deficiencia.getCategoria().getId(), 2L);
    }

    @Test
    @DisplayName("Remove e recupera um registro")
    void removeAndRecovery() throws Exception{
        List<Deficiencia> listaDeficiencias = dr.findByAtivo(true);

        Deficiencia deficiencia = listaDeficiencias
        .get(listaDeficiencias.size() - 1);

        Long id = deficiencia.getId();

        mockMvc.perform(
            get("/deficiencia/remover/" + id)
        ).andExpect(status().is3xxRedirection());

        deficiencia = dr.findById(id).orElseThrow();

        assertNotNull(deficiencia);

        assertFalse(deficiencia.isAtivo());


        //RECUPERAÇÃO
        mockMvc.perform(
            get("/deficiencia/remover/" + id)
        ).andExpect(status().is3xxRedirection());

        deficiencia = dr.findById(id).orElseThrow();

        assertNotNull(deficiencia);
        assertTrue(deficiencia.isAtivo());
    }

}
