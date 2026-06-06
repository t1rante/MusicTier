package com.musictier.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.musictier.backend.model.Avaliacao;
import com.musictier.backend.model.Categoria;
import com.musictier.backend.model.Musica;
import com.musictier.backend.repository.AvaliacaoRepository;
import com.musictier.backend.repository.CategoriaRepository;
import com.musictier.backend.repository.MusicaRepository;

@RestController
@RequestMapping("/api/avaliacoes")
@CrossOrigin(origins = "*")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    // Trazemos os repositórios para o Controller conseguir buscar os objetos no banco
    @Autowired
    private MusicaRepository musicaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Avaliacao> listarAvaliacoes() {
        return avaliacaoRepository.findAll();
    }

    // Atualizamos a rota para exigir os IDs diretamente na URL
    @PostMapping
    public Avaliacao adicionarAvaliacao(
            @RequestBody Avaliacao novaAvaliacao, 
            @RequestParam Long musicaId, 
            @RequestParam Long categoriaId) {
        
        // 1. Buscamos a Música e a Categoria reais no banco de dados
        Musica musica = musicaRepository.findById(musicaId)
                .orElseThrow(() -> new RuntimeException("Música não encontrada"));
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        // 2. Amarramos esses objetos à nossa nova nota (Bypass no @JsonIgnore)
        novaAvaliacao.setMusica(musica);
        novaAvaliacao.setCategoria(categoria);

        // 3. Salvamos a nota. Agora ela nunca mais será órfã!
        return avaliacaoRepository.save(novaAvaliacao);
    }
}