package com.musictier.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musictier.backend.model.Categoria;

// O JpaRepository já traz prontos métodos como salvar(), deletar(), etc.
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    // Aqui nós colamos a nossa "assinatura mágica". 
    // O Spring vai gerar o SQL: SELECT * FROM categoria WHERE sessao_id = ?
    List<Categoria> findBySessao_Id(Long sessaoId);
    
}