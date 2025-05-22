package com.example.demo.Form.Pessoa;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.Enum.Sexo;
import com.example.demo.Model.Deficiencia;
import com.example.demo.Model.Endereco;
import com.example.demo.Model.Pessoa;
import com.example.demo.Repository.DeficienciaRepository;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Getter
@Setter
@NoArgsConstructor
public class PessoaForm {

    @NotBlank(message = "Preencha o campo nome.")
    private String nome;

    @NotNull(message = "Preencha o campo data de nascimento.")
    @Past(message = "A data de nascimento de ser um data do passado.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate nascimento;

    @NotNull(message = "Preencha o campo sexo.")
    private int sexo;

    @NotNull(message = "Preencha o campo deficiência")
    private Deficiencia deficiencia;
    private List<Deficiencia> listDeficiencias;

    @NotBlank(message = "Preencha o campo CEP.")
    @Size(min=9,max=9,message= "Preencha com um CEP válido.")
    private String cep;

    private String uf;
    private String cidade;
    private String bairro;
    private String Logradouro;
    private String numero;
    private String complemento;



    public PessoaForm(Pessoa pessoa){
        this.nome = pessoa.getNome();
        this.nascimento = pessoa.getNascimento();
        this.sexo = pessoa.getSexo().getCodigo();
        this.deficiencia = pessoa.getDeficiencia();
        this.cep = pessoa.getEndereco().getCep();
        this.numero = pessoa.getEndereco().getNumero();
        this.Logradouro = pessoa.getEndereco().getLogradouro();
        this.complemento = pessoa.getEndereco().getComplemento();
        this.bairro = pessoa.getEndereco().getBairro().getNome();
        this.cidade = pessoa.getEndereco().getCidade().getNome();
        this.uf = pessoa.getEndereco().getEstado().getSigla();
    }


}