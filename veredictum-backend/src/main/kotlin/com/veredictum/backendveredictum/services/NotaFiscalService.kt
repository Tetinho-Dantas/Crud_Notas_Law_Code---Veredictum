package com.veredictum.backendveredictum.services

import com.veredictum.backendveredictum.entity.NotaFiscal
import com.veredictum.backendveredictum.repository.NotaFiscalRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.Optional

@Service
class NotaFiscalService(
    private val notaFiscalRepository: NotaFiscalRepository
) {

    fun save(notaFiscal: NotaFiscal): NotaFiscal {
        return notaFiscalRepository.save(notaFiscal)
    }

    fun findById(id: Int): Optional<NotaFiscal> {
        return notaFiscalRepository.findById(id)
    }

    fun findByClienteNome(nome: String): List<NotaFiscal> {
        return notaFiscalRepository.findByCliente_NomeContainingIgnoreCase(nome)
    }

    fun findByDataCriacao(data: LocalDate): List<NotaFiscal> {
        return notaFiscalRepository.findByDataCriacao(data)
    }

    fun findAll(): List<NotaFiscal> {
        return notaFiscalRepository.findAll()
    }

    fun existsById(id: Int): Boolean {
        return notaFiscalRepository.existsById(id)
    }

    fun deleteById(id: Int) {
        notaFiscalRepository.deleteById(id)
    }
}
