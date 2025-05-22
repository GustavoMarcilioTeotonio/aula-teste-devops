package com.example.demo.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "estado")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome",  nullable = false, length = 100)
    private String nome;

    @Column(name = "sigla", nullable = false, length = 2)
    private String sigla;

    @Column(name = "ativo", nullable = false)
    @ColumnDefault("true")
    private boolean ativo;
}
