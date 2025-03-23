package com.jhops10.desafio_cnab.service;

import com.jhops10.desafio_cnab.model.TipoTransacao;
import com.jhops10.desafio_cnab.model.Transacao;
import com.jhops10.desafio_cnab.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;

    public TransacaoService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }


    public void processarArquivo(MultipartFile arquivo) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(arquivo.getInputStream()))){
            String linha;
            while ((linha = br.readLine()) != null) {
                Transacao transacao = analisarLinha(linha);
                transacaoRepository.save(transacao);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Transacao analisarLinha(String linha) {
        Transacao transacao = new Transacao();
        int tipoCodigo = Integer.parseInt(linha.substring(0,1));
        TipoTransacao tipo = TipoTransacao.fromCodigo(tipoCodigo);

        transacao.setTipo(tipo.getDescricao());
        transacao.setNatureza(tipo.getNatureza());
        transacao.setSinal(tipo.getSinal());
        transacao.setData(LocalDate.parse(linha.substring(1,9), DateTimeFormatter.BASIC_ISO_DATE));
        transacao.setValor(new BigDecimal(linha.substring(9, 19)).divide(new BigDecimal(100)));
        transacao.setCpf(linha.substring(19,30));
        transacao.setCartao(linha.substring(30,42));
        transacao.setHora(LocalTime.parse(linha.substring(42,48), DateTimeFormatter.ofPattern("HHmmss")));
        transacao.setDonoLoja(linha.substring(48,62).trim());
        transacao.setNomeLoja(linha.substring(62,80));

        return transacao;
    }

    public List<Transacao> listarTransacoes() {
        return transacaoRepository.findAll();
    }
}
