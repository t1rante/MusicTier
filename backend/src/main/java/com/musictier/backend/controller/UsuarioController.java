package com.musictier.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musictier.backend.model.Usuario;
import com.musictier.backend.repository.UsuarioRepository;
//import java.util.Optional;

// @RestController: Define que esta classe responde a requisições web
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600) // Permite que o React (porta 5173) converse com o Spring (porta 8080)
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Rota de Pseudo-Login
     * Recebe um objeto com o "nome", verifica se existe. Se não, cria um novo.
     */
    @PostMapping("/entrar")
    public Usuario entrar(@RequestBody Usuario usuarioTentandoEntrar) {
        
        // Busca todos os usuários (uma forma simples para o nosso MVP)
        List<Usuario> todosUsuarios = usuarioRepository.findAll();
        
        // Verifica se já existe alguém com esse nome exato
        for (Usuario u : todosUsuarios) {
            if (u.getNome().equalsIgnoreCase(usuarioTentandoEntrar.getNome())) {
                return u; // Retorna o usuário que já existe no banco
            }
        }
        
        // Se o loop terminar e não achar ninguém, salva como um usuário novo
        return usuarioRepository.save(usuarioTentandoEntrar);
    }
}