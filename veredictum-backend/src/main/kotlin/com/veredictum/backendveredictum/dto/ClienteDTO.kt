package com.veredictum.backendveredictum.dto

import java.time.LocalDate

data class ClienteDTO (
    val idCliente: Int,
    val nome: String,
    val fkIndicador: Int?,
    val email: String,
    val rg: String,
    val cpf: String?,
    val cnpj: String?,
    val dataNascimento: LocalDate?,
    val dataInicio: LocalDate?,
    val endereco: String?,
    val cep: String?,
    val descricao: String?,
    val inscricaoEstadual: String?,
    val isProBono: Boolean?,
    val isAtivo: Boolean,
    val isJuridico: Boolean?,
    )