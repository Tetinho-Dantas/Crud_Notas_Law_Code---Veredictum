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
            cliente = com.veredictum.backendveredictum.entity.Cliente(nome = nomeCliente)
        )
        `when`(notaFiscalService.findByClienteNome(nomeCliente)).thenReturn(listOf(nota))

        val response = controller.buscarPorNomeCliente(nomeCliente)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size)
        assertEquals("NF123", response.body?.first()?.etiqueta)
    }

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
            cliente = com.veredictum.backendveredictum.entity.Cliente(nome = "Maria")
        )
        `when`(notaFiscalService.findByDataCriacao(data)).thenReturn(listOf(nota))

        val response = controller.buscarPorDataCriacao("2024-05-20")

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size)
        assertEquals("NF124", response.body?.first()?.etiqueta)
    }


}