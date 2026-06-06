package com.musictier.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A nota de 0 a 10 que o usuário vai dar
    private Integer nota;

    // A qual música essa nota pertence?
    @ManyToOne
    @JoinColumn(name = "musica_id")
    @JsonIgnore
    private Musica musica;

    // A qual tier/categoria essa nota pertence? (Ex: nota 8 para o "Vocal")
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public Avaliacao() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getNota() { return nota; }
    public void setNota(Integer nota) { this.nota = nota; }

    public Musica getMusica() { return musica; }
    public void setMusica(Musica musica) { this.musica = musica; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}