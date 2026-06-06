package com.musictier.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome; // Ex: Vocal, Beat, Letra
    
    // Usamos Double para permitir pesos quebrados, como 1.5
    private Double peso; 

    // @ManyToOne: Muitas Categorias podem pertencer a Uma única Sessão.
    // @JoinColumn cria uma coluna na tabela 'categoria' chamada 'sessao_id' para fazer essa ligação.
    @ManyToOne
    @JoinColumn(name = "sessao_id")
    @JsonIgnore
    private Sessao sessao;

    public Categoria() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public Sessao getSessao() { return sessao; }
    public void setSessao(Sessao sessao) { this.sessao = sessao; }
}