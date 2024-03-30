package br.com.brunno.yfood.domain.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ExecutaTransacao {

    @Transactional
    public void executa(Runnable runnable) {
        runnable.run();
    }
}
