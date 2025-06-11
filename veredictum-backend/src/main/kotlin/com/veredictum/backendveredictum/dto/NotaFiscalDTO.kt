package com.veredictum.backendveredictum.dto

import java.time.LocalDate

data class NotaFiscalDTO(
    val idNotaFiscal: Int? = null,
    val fkCliente: Int?,
    val etiqueta: String,
    val valor: Double,
    val dataVencimento: LocalDate,
    val descricao: String,
    val urlNuvem: String,
    val isEmitida: Boolean,
    val dataCriacao: LocalDate? = null // Adicionado
)
