package com.musictier.backend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Musica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String artista;
    
    // Aqui vamos salvar o resultado da nossa média ponderada depois
    private Double notaFinal;

    // Assim como a Categoria, a Música também pertence a Uma Sessão
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "sessao_id")
    private Sessao sessao;

    // @OneToMany: Uma Música tem Muitas Avaliações (uma para cada categoria/tier).
    // CascadeType.ALL significa que se deletarmos a música, as notas dela também serão deletadas do banco.
    @OneToMany(mappedBy = "musica", cascade = CascadeType.ALL)
    private List<Avaliacao> avaliacoes;

    public Musica() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getArtista() { return artista; }
    public void setArtista(String artista) { this.artista = artista; }

    public Double getNotaFinal() { return notaFinal; }
    public void setNotaFinal(Double notaFinal) { this.notaFinal = notaFinal; }

    public Sessao getSessao() { return sessao; }
    public void setSessao(Sessao sessao) { this.sessao = sessao; }

    public List<Avaliacao> getAvaliacoes() { return avaliacoes; }
    public void setAvaliacoes(List<Avaliacao> avaliacoes) { this.avaliacoes = avaliacoes; }
}