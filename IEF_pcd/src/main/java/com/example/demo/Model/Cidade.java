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
@Table(name = "cidade")
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome",  nullable = false, length = 100)
    private String nome;

    @Column(name = "ativo", nullable = false)
    @ColumnDefault("true")
    private boolean ativo;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;
}
