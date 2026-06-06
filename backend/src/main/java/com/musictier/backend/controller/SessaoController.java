package com.musictier.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.musictier.backend.model.Sessao;
import com.musictier.backend.model.Usuario;
import com.musictier.backend.repository.SessaoRepository;
import com.musictier.backend.repository.UsuarioRepository;
@RestController

@RequestMapping("/api/sessoes")
public class SessaoController {

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Rota para criar uma nova Sessão e vinculá-la ao Usuário criador.
     * @RequestBody pega o JSON com o "nome" da sessão.
     * @RequestParam pega o ID do usuário que enviamos na URL.
     */
    @PostMapping
    public Sessao criarSessao(@RequestBody Sessao novaSessao, @RequestParam Long usuarioId) {
        
        // 1. Buscamos o usuário no banco de dados usando o ID que veio do React
        Usuario dono = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        // 2. Inicializamos a lista de donos (caso esteja nula) e adicionamos o criador
        if (novaSessao.getDonos() == null) {
            novaSessao.setDonos(new ArrayList<>());
        }
        novaSessao.getDonos().add(dono);

        // 3. Salvamos a sessão. O Spring Data JPA vai automaticamente preencher 
        // a sua tabela invisível "sessao_usuario" graças ao @ManyToMany!
        return sessaoRepository.save(novaSessao);
    }
    @GetMapping("/usuario/{usuarioId}")
    public List<Sessao> listarSessoesDoUsuario(@PathVariable Long usuarioId) {
        return sessaoRepository.findByDonos_Id(usuarioId);
    }
    @DeleteMapping("/{id}")
    public void deletarSessao(@PathVariable Long id) {
        sessaoRepository.deleteById(id);
    }
}