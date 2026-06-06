package com.musictier.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musictier.backend.model.Sessao;

public interface SessaoRepository extends JpaRepository<Sessao, Long> {
    
    // O Spring lê esse nome e entende: "Busque todas as Sessões onde 
    // o ID (Id) da lista de donos (Donos) seja igual ao parâmetro passado".
    List<Sessao> findByDonos_Id(Long usuarioId);
    
}