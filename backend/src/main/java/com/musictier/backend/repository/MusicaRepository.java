package com.musictier.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musictier.backend.model.Musica;

public interface MusicaRepository extends JpaRepository<Musica, Long> {
    
    // A mesma lógica se aplica aqui para as músicas daquela sessão!
    // SQL gerado: SELECT * FROM musica WHERE sessao_id = ?
    List<Musica> findBySessao_Id(Long sessaoId);
    
}