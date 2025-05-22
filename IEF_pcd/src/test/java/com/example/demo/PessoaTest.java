package com.example.demo;

import com.example.demo.Model.Pessoa;
import com.example.demo.Model.Deficiencia;
import com.example.demo.Repository.PessoaRepository;
import com.example.demo.Repository.DeficienciaRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PessoaTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private DeficienciaRepository deficienciaRepository;

    @Test
    @DisplayName("Verificar se o endpoint /pessoa responde 200 (OK)")
    void index() throws Exception {
        mockMvc.perform(get("/pessoa"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Adiciona e altera uma pessoa.")
    void createAndUpdate() throws Exception {
        Deficiencia def1 = deficienciaRepository.findById(1L).orElseThrow();
        Deficiencia def2 = deficienciaRepository.findById(2L).orElseThrow();

        String timestamp = String.valueOf(System.currentTimeMillis());
        String nome = "Pessoa Teste " + timestamp;
        String nomeAlterado = "Pessoa Alterada " + timestamp;

        mockMvc.perform(post("/pessoa/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nome", nome)
                        .param("nascimento", "2000-01-01")
                        .param("sexo", "1")
                        .param("deficiencia", String.valueOf(def1.getId()))
                        .param("cep", "12345-678")
                        .param("uf", "SP")
                        .param("cidade", "São Paulo")
                        .param("bairro", "Centro")
                        .param("Logradouro", "Rua A")
                        .param("numero", "100")
                        .param("complemento", "Ap 1"))
                .andExpect(status().is3xxRedirection());

        Pessoa pessoa = pessoaRepository.findAll().get(pessoaRepository.findAll().size() - 1);

        assertNotNull(pessoa);
        assertEquals(nome, pessoa.getNome());
        assertEquals(LocalDate.of(2000, 1, 1), pessoa.getNascimento());
        assertEquals(1, pessoa.getSexo().getCodigo());
        assertEquals(def1.getId(), pessoa.getDeficiencia().getId());
        assertEquals("12345-678", pessoa.getEndereco().getCep());
        assertEquals("SP", pessoa.getEndereco().getEstado().getSigla());
        assertEquals("São Paulo", pessoa.getEndereco().getCidade().getNome());
        assertEquals("Centro", pessoa.getEndereco().getBairro().getNome());
        assertEquals("Rua A", pessoa.getEndereco().getLogradouro());
        assertEquals("100", pessoa.getEndereco().getNumero());
        assertEquals("Ap 1", pessoa.getEndereco().getComplemento());

        // Atualiza a pessoa
        mockMvc.perform(post("/pessoa/update/" + pessoa.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nome", nomeAlterado)
                        .param("nascimento", "1995-12-31")
                        .param("sexo", "2")
                        .param("deficiencia", String.valueOf(def2.getId()))
                        .param("cep", "87654-321")
                        .param("uf", "RJ")
                        .param("cidade", "Rio de Janeiro")
                        .param("bairro", "Copacabana")
                        .param("Logradouro", "Avenida Atlântica")
                        .param("numero", "200")
                        .param("complemento", "Ap 2"))
                .andExpect(status().is3xxRedirection());

        pessoa = pessoaRepository.findById(pessoa.getId()).orElseThrow();

        assertEquals(nomeAlterado, pessoa.getNome());
        assertEquals(LocalDate.of(1995, 12, 31), pessoa.getNascimento());
        assertEquals(2, pessoa.getSexo().getCodigo());
        assertEquals(def2.getId(), pessoa.getDeficiencia().getId());
        assertEquals("87654-321", pessoa.getEndereco().getCep());
        assertEquals("RJ", pessoa.getEndereco().getEstado().getSigla());
        assertEquals("Rio de Janeiro", pessoa.getEndereco().getCidade().getNome());
        assertEquals("Copacabana", pessoa.getEndereco().getBairro().getNome());
        assertEquals("Avenida Atlântica", pessoa.getEndereco().getLogradouro());
        assertEquals("200", pessoa.getEndereco().getNumero());
        assertEquals("Ap 2", pessoa.getEndereco().getComplemento());
    }

    @Test
    @DisplayName("Remove e recupera uma pessoa.")
    void removeAndRecover() throws Exception {
        List<Pessoa> pessoasAtivas = pessoaRepository.findByAtivo(true);
        assertFalse(pessoasAtivas.isEmpty());

        Pessoa pessoa = pessoasAtivas.get(pessoasAtivas.size() - 1);
        Long id = pessoa.getId();

        // Remove
        mockMvc.perform(get("/pessoa/remover/" + id))
                .andExpect(status().is3xxRedirection());

        pessoa = pessoaRepository.findById(id).orElseThrow();
        assertFalse(pessoa.isAtivo());

        // Recupera
        mockMvc.perform(get("/pessoa/remover/" + id))
                .andExpect(status().is3xxRedirection());

        pessoa = pessoaRepository.findById(id).orElseThrow();
        assertTrue(pessoa.isAtivo());
    }
}
