package com.musictier.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musictier.backend.model.Avaliacao;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    
    // A mágica do Spring: Busca na tabela de avaliações todas as notas onde a chave estrangeira musica_id for igual ao parâmetro
    List<Avaliacao> findByMusica_Id(Long musicaId);
    
}