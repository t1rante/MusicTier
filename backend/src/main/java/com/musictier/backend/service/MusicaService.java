package com.musictier.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musictier.backend.model.Avaliacao;
import com.musictier.backend.model.Musica;
import com.musictier.backend.repository.AvaliacaoRepository;
import com.musictier.backend.repository.MusicaRepository;

// A anotação @Service diz ao Spring: "Esta classe contém regras de negócio"
@Service
public class MusicaService {

    @Autowired
    private MusicaRepository musicaRepository;

    // NOVO: Injetamos o repositório de avaliações para buscar as notas direto da tabela
    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    /**
     * Este é o método principal. Ele recebe o ID de uma música, 
     * busca as notas fresquinhas dela, calcula a média ponderada e salva o resultado final.
     */
    public Musica calcularEAtualizarNotaFinal(Long musicaId) {
        
        // 1. Buscar a música no banco de dados. Se não achar, lança um erro.
        Musica musica = musicaRepository.findById(musicaId)
                .orElseThrow(() -> new RuntimeException("Música não encontrada!"));

        // CORREÇÃO AQUI: Em vez de usar musica.getAvaliacoes(), nós vamos no banco de dados 
        // e exigimos todas as notas que acabaram de ser salvas para esta música!
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByMusica_Id(musicaId);

        // Se a música ainda não tem nenhuma nota, a nota final é 0.0
        if (avaliacoes == null || avaliacoes.isEmpty()) {
            musica.setNotaFinal(0.0);
            return musicaRepository.save(musica);
        }

        // Variáveis para a nossa fórmula matemática
        double somaNotasPonderadas = 0.0;
        double somaPesos = 0.0;

        // 2. O Cálculo da Média Ponderada
        for (Avaliacao avaliacao : avaliacoes) {
            // Pegamos o peso da categoria (ex: Peso do 'Beat' é 2.0)
            double peso = avaliacao.getCategoria().getPeso();
            // Pegamos a nota (Integer) e o Java converte para double perfeitamente aqui[cite: 1]
            double nota = avaliacao.getNota();

            // Multiplicamos a nota pelo peso e somamos ao total
            somaNotasPonderadas += (nota * peso);
            
            // Somamos os pesos para dividir no final
            somaPesos += peso;
        }

        // 3. A divisão final (evitando dividir por zero caso os pesos sejam 0)
        double notaFinal = 0.0;
        if (somaPesos > 0) {
            notaFinal = somaNotasPonderadas / somaPesos;
        }

        // Um truque legal: Arredondar a nota para ter apenas 2 casas decimais (ex: 8.75)
        notaFinal = Math.round(notaFinal * 100.0) / 100.0;

        // 4. Salvar a nova nota na música e atualizar o banco de dados
        musica.setNotaFinal(notaFinal);
        return musicaRepository.save(musica);
    }
}