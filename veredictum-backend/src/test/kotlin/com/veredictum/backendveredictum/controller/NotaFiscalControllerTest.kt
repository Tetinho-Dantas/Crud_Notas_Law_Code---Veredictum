package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.dto.NotaFiscalDTO
import com.veredictum.backendveredictum.entity.Cliente
import com.veredictum.backendveredictum.entity.NotaFiscal
import com.veredictum.backendveredictum.services.ClienteService
import com.veredictum.backendveredictum.services.NotaFiscalService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class NotaFiscalControllerTest {

    @Mock
    private lateinit var notaFiscalService: NotaFiscalService

    @Mock
    private lateinit var clienteService: ClienteService

    @InjectMocks
    private lateinit var controller: NotaFiscalController

    // --- Testes para listarNotasFiscais ---
    @Test
    fun `listarNotasFiscais deve retornar lista de notas fiscais com status 200`() {
        val nota1 = NotaFiscal(
            idNotaFiscal = 1,
            cliente = Cliente(idCliente = 1, nome = "João"),
            dataCriacao = LocalDate.of(2024, 6, 1),
            etiqueta = "NF001",
            valor = 100.0,
            dataVencimento = LocalDate.of(2024, 6, 30),
            descricao = "Primeira nota",
            urlNuvem = "https://link1.com",
            isEmitida = true
        )

        val nota2 = NotaFiscal(
            idNotaFiscal = 2,
            cliente = Cliente(idCliente = 2, nome = "Maria"),
            dataCriacao = LocalDate.of(2024, 6, 5),
            etiqueta = "NF002",
            valor = 250.0,
            dataVencimento = LocalDate.of(2024, 7, 5),
            descricao = "Segunda nota",
            urlNuvem = "https://link2.com",
            isEmitida = false
        )

        `when`(notaFiscalService.findAll()).thenReturn(listOf(nota1, nota2))

        val response = controller.listarNotasFiscais()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(2, response.body?.size)
        assertEquals("NF001", response.body?.get(0)?.etiqueta)
        assertEquals("NF002", response.body?.get(1)?.etiqueta)
        verify(notaFiscalService).findAll()
    }

    // --- Testes para buscarNotaFiscalPorId ---
    @Test
    fun `buscarNotaFiscalPorId deve retornar nota fiscal quando encontrada`() {
        val notaFiscal = NotaFiscal(
            idNotaFiscal = 1,
            cliente = Cliente(idCliente = 1),
            dataCriacao = LocalDate.now(),
            etiqueta = "Teste",
            valor = 100.0,
            dataVencimento = LocalDate.now().plusDays(10),
            descricao = "Descrição",
            urlNuvem = "http://teste.com",
            isEmitida = false
        )
        `when`(notaFiscalService.findById(1)).thenReturn(Optional.of(notaFiscal))

        val response = controller.buscarNotaFiscalPorId(1)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.idNotaFiscal)
        verify(notaFiscalService).findById(1)
    }

    @Test
    fun `buscarNotaFiscalPorId deve retornar 404 quando nota fiscal nao encontrada`() {
        `when`(notaFiscalService.findById(999)).thenReturn(Optional.empty())

        val response = controller.buscarNotaFiscalPorId(999)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals(null, response.body)
        verify(notaFiscalService).findById(999)
    }

    // --- Testes para atualizarNotaFiscal ---
    @Test
    fun `atualizarNotaFiscal deve retornar 404 Not Found quando nota fiscal nao existe`() {
        val notaFiscalDTO = NotaFiscalDTO(
            idNotaFiscal = 999,
            fkCliente = 1,
            etiqueta = "Inexistente",
            valor = 10.0,
            dataVencimento = LocalDate.now(),
            descricao = "",
            urlNuvem = "",
            isEmitida = false
        )
        `when`(notaFiscalService.findById(999)).thenReturn(Optional.empty())

        val response = controller.atualizarNotaFiscal(999, notaFiscalDTO)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        verify(notaFiscalService).findById(999)
        verifyNoInteractions(clienteService)
        verifyNoMoreInteractions(notaFiscalService)
    }

    @Test
    fun `atualizarNotaFiscal deve retornar 400 Bad Request quando IDs do path e DTO sao diferentes`() {
        val notaFiscalDTO = NotaFiscalDTO(
            idNotaFiscal = 2, // ID no DTO é diferente do ID no path
            fkCliente = 1,
            etiqueta = "Erro ID",
            valor = 10.0,
            dataVencimento = LocalDate.now(),
            descricao = "",
            urlNuvem = "",
            isEmitida = false
        )

        val response = controller.atualizarNotaFiscal(1, notaFiscalDTO) // Path ID é 1, DTO ID é 2

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        verifyNoInteractions(notaFiscalService)
        verifyNoInteractions(clienteService)
    }

    // --- Testes para excluirNotaFiscal ---
    @Test
    fun `excluirNotaFiscal deve retornar 204 No Content quando nota fiscal eh excluida com sucesso`() {
        `when`(notaFiscalService.existsById(1)).thenReturn(true)
        doNothing().`when`(notaFiscalService).deleteById(1)

        val response = controller.excluirNotaFiscal(1)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(notaFiscalService).existsById(1)
        verify(notaFiscalService).deleteById(1)
    }

    @Test
    fun `excluirNotaFiscal deve retornar 404 Not Found quando nota fiscal nao existe`() {
        `when`(notaFiscalService.existsById(999)).thenReturn(false)

        val response = controller.excluirNotaFiscal(999)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        verify(notaFiscalService).existsById(999)
        verify(notaFiscalService, never()).deleteById(anyInt())
    }

    // --- Testes para buscarPorNomeCliente ---
    @Test
    fun `buscarPorNomeCliente deve retornar lista de notas fiscais`() {
        val nomeCliente = "João"
        val nota = NotaFiscal(
            idNotaFiscal = 1,
            etiqueta = "NF123",
            valor = 100.0,
            dataVencimento = LocalDate.now().plusDays(10),
            descricao = "Teste",
            urlNuvem = "http://teste.com",
            isEmitida = true,
            cliente = Cliente(nome = nomeCliente)
        )
        `when`(notaFiscalService.findByClienteNome(nomeCliente)).thenReturn(listOf(nota))

        val response = controller.buscarPorNomeCliente(nomeCliente)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size)
        assertEquals("NF123", response.body?.first()?.etiqueta)
        verify(notaFiscalService).findByClienteNome(nomeCliente)
    }

    // --- Testes para buscarPorDataCriacao ---
    @Test
    fun `buscarPorDataCriacao deve retornar lista de notas fiscais`() {
        val data = LocalDate.of(2024, 5, 20)
        val nota = NotaFiscal(
            idNotaFiscal = 1,
            dataCriacao = data,
            etiqueta = "NF124",
            valor = 200.0,
            dataVencimento = LocalDate.now().plusDays(5),
            descricao = "Descrição",
            urlNuvem = "http://exemplo.com",
            isEmitida = false,
            cliente = Cliente(nome = "Maria")
        )
        `when`(notaFiscalService.findByDataCriacao(data)).thenReturn(listOf(nota))

        val response = controller.buscarPorDataCriacao("2024-05-20")

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size)
        assertEquals("NF124", response.body?.first()?.etiqueta)
        verify(notaFiscalService).findByDataCriacao(data)
    }

    @Test
    fun `buscarPorDataCriacao deve retornar 400 Bad Request para formato de data invalido`() {
        val dataInvalida = "2024/05/20" // Formato inválido

        val response = controller.buscarPorDataCriacao(dataInvalida)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        verifyNoInteractions(notaFiscalService)
    }


}