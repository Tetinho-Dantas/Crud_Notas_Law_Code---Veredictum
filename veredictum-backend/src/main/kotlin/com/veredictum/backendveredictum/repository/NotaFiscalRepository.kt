package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.entity.NotaFiscal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface NotaFiscalRepository : JpaRepository<NotaFiscal, Int> {
    fun findByCliente_NomeContainingIgnoreCase(nome: String): List<NotaFiscal>
    fun findByDataCriacao(dataCriacao: LocalDate): List<NotaFiscal>
}
