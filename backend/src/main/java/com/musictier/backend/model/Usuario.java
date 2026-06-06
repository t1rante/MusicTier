package com.musictier.backend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

// A anotação @Entity avisa o Spring: "Transforme essa classe em uma tabela no banco de dados"
@Entity
public class Usuario {

    // @Id e @GeneratedValue dizem que o 'id' é a Chave Primária e será gerado automaticamente (1, 2, 3...)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;

    // @ManyToMany: Um usuário pode ter várias sessões, e uma sessão vários usuários.
    // O 'mappedBy' indica que a classe Sessao é quem vai gerenciar essa relação através do campo 'donos'.
    @ManyToMany(mappedBy = "donos")
    @JsonIgnore
    private List<Sessao> sessoes;

    // Construtor vazio (obrigatório para o Spring funcionar corretamente)
    public Usuario() {}

    // Daqui para baixo são os Getters e Setters (Para pegarmos e alterarmos as informações com segurança)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<Sessao> getSessoes() { return sessoes; }
    public void setSessoes(List<Sessao> sessoes) { this.sessoes = sessoes; }
}