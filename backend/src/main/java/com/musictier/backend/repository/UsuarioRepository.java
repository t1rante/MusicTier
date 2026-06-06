package com.musictier.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.musictier.backend.model.Usuario;

// @Repository avisa o Spring que este arquivo lida com o banco de dados
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Ao estender o JpaRepository, ganhamos de graça métodos como:
    // save(), findAll(), findById(), deleteById()
}