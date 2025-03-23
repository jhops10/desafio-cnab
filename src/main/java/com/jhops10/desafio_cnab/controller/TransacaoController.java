package com.jhops10.desafio_cnab.controller;

import com.jhops10.desafio_cnab.model.Transacao;
import com.jhops10.desafio_cnab.service.TransacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @GetMapping
    public ResponseEntity<List<Transacao>> listarTransacoes() {
        return ResponseEntity.ok(transacaoService.listarTransacoes());
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("arquivo") MultipartFile arquivo) {
        transacaoService.processarArquivo(arquivo);
        return ResponseEntity.ok("Arquivo processado com sucesso!");
    }


}
