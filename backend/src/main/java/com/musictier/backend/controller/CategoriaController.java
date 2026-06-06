package com.musictier.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.musictier.backend.model.Categoria;
import com.musictier.backend.model.Sessao;
import com.musictier.backend.repository.CategoriaRepository;
import com.musictier.backend.repository.SessaoRepository;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private SessaoRepository sessaoRepository;

    // Rota para CRIAR um Tier amarrado à Sessão atual
    @PostMapping
    public Categoria adicionarCategoria(@RequestBody Categoria novaCategoria, @RequestParam Long sessaoId) {
        Sessao sessao = sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada!"));
        novaCategoria.setSessao(sessao);
        return categoriaRepository.save(novaCategoria);
    }

    // Você precisará adicionar este método no seu CategoriaRepository: 
    // List<Categoria> findBySessao_Id(Long sessaoId);
    @GetMapping("/sessao/{sessaoId}")
    public List<Categoria> listarCategoriasDaSessao(@PathVariable Long sessaoId) {
        return categoriaRepository.findBySessao_Id(sessaoId);
    }

    @PutMapping("/{id}")
    public Categoria editarCategoria(@PathVariable Long id, @RequestBody Categoria categoriaAtualizada) {
        Categoria catExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        
        catExistente.setNome(categoriaAtualizada.getNome());
        catExistente.setPeso(categoriaAtualizada.getPeso());
        return categoriaRepository.save(catExistente);
    }
}