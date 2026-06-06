package com.musictier.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.musictier.backend.model.Musica;
import com.musictier.backend.model.Sessao;
import com.musictier.backend.repository.MusicaRepository;
import com.musictier.backend.repository.SessaoRepository;
import com.musictier.backend.service.MusicaService;

@RestController
@RequestMapping("/api/musicas")
@CrossOrigin(origins = "*") 
public class MusicaController {

    @Autowired
    private MusicaRepository musicaRepository;

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private MusicaService musicaService;

    // Rota para adicionar música AMARRADA à Sessão
    @PostMapping
    public Musica adicionarMusica(@RequestBody Musica novaMusica, @RequestParam Long sessaoId) {
        Sessao sessao = sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada!"));
        
        novaMusica.setSessao(sessao);
        novaMusica.setNotaFinal(0.0);
        return musicaRepository.save(novaMusica);
    }

    // Você precisará adicionar este método no seu MusicaRepository: 
    // List<Musica> findBySessao_Id(Long sessaoId);
    @GetMapping("/sessao/{sessaoId}")
    public List<Musica> listarMusicasDaSessao(@PathVariable Long sessaoId) {
        return musicaRepository.findBySessao_Id(sessaoId);
    }

    @PutMapping("/{id}/calcular-nota")
    public Musica calcularNota(@PathVariable Long id) {
        return musicaService.calcularEAtualizarNotaFinal(id);
    }
}