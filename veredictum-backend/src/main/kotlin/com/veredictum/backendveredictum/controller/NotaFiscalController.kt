package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.entity.NotaFiscal
import com.veredictum.backendveredictum.dto.NotaFiscalDTO
import com.veredictum.backendveredictum.services.ClienteService
import com.veredictum.backendveredictum.services.NotaFiscalService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import jakarta.validation.Valid
import java.time.LocalDate


@Tag(name = "Notas Fiscais", description = "Endpoints para gerenciamento de notas fiscais")
@RestController
@RequestMapping("/notas-fiscais")
class NotaFiscalController(

    private val notaFiscalService: NotaFiscalService,
    private val clienteService: ClienteService

) {

    @Operation(
        summary = "Listar todas as notas fiscais",
        description = "Retorna uma lista de todas as notas fiscais cadastradas no sistema."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista de notas fiscais retornada com sucesso"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    @GetMapping
    fun listarNotasFiscais(): ResponseEntity<List<NotaFiscalDTO>> {
        val notasFiscais = notaFiscalService.findAll().map { it.toDTO() }
        return ResponseEntity.ok(notasFiscais)
    }

    @Operation(
        summary = "Buscar nota fiscal por ID",
        description = "Retorna os detalhes de uma nota fiscal específica com base no seu ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Nota fiscal encontrada"),
            ApiResponse(responseCode = "404", description = "Nota fiscal não encontrada"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    @GetMapping("/{id}")
    fun buscarNotaFiscalPorId(@PathVariable id: Int): ResponseEntity<NotaFiscalDTO> {
        val notaFiscalOptional = notaFiscalService.findById(id)
        return if (notaFiscalOptional.isPresent) {
            ResponseEntity.ok(notaFiscalOptional.get().toDTO())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(
        summary = "Criar nova nota fiscal",
        description = "Cria uma nova nota fiscal no sistema. Requer um ID de cliente válido."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Nota fiscal criada com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados inválidos ou cliente não informado/encontrado"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    @PostMapping
    fun criarNotaFiscal(@RequestBody @Valid notaFiscalDTO: NotaFiscalDTO): ResponseEntity<NotaFiscalDTO> {
        val clienteId = notaFiscalDTO.fkCliente
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente deve ser informado para criar uma nota fiscal")
        val cliente = clienteService.findById(clienteId).orElseThrow {
            ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente com ID $clienteId não encontrado")
        }
        val novaNotaFiscal = NotaFiscal(
            cliente = cliente,
            dataCriacao = LocalDate.now(),
            etiqueta = notaFiscalDTO.etiqueta,
            valor = notaFiscalDTO.valor,
             dataVencimento = notaFiscalDTO.dataVencimento,
            descricao = notaFiscalDTO.descricao,
            urlNuvem = notaFiscalDTO.urlNuvem,
            isEmitida = notaFiscalDTO.isEmitida
        )
        val notaFiscalSalva = notaFiscalService.save(novaNotaFiscal)
        return ResponseEntity.status(HttpStatus.CREATED).body(notaFiscalSalva.toDTO())
    }

    @Operation(
        summary = "Atualizar nota fiscal existente",
        description = "Atualiza os dados de uma nota fiscal existente com base no seu ID. O ID do cliente associado pode ser atualizado, mas deve ser válido."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Nota fiscal atualizada com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados inválidos ou cliente não encontrado"),
            ApiResponse(responseCode = "404", description = "Nota fiscal não encontrada"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    @PutMapping("/{id}")
    fun atualizarNotaFiscal(
        @PathVariable id: Int,
        @RequestBody @Valid notaFiscalDTO: NotaFiscalDTO
    ): ResponseEntity<NotaFiscalDTO> {
        if (notaFiscalDTO.idNotaFiscal != null && notaFiscalDTO.idNotaFiscal != id) {
            return ResponseEntity.badRequest().build()
        }

        return notaFiscalService.findById(id)
            .map { notaFiscalExistente ->
                val cliente = notaFiscalDTO.fkCliente?.let { clienteId ->
                    clienteService.findById(clienteId).orElseThrow {
                        ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente com ID $clienteId não encontrado para atualização")
                    }
                } ?: notaFiscalExistente.cliente

                val notaFiscalAtualizada = notaFiscalExistente.copy(
                    cliente = cliente,
                    dataCriacao = notaFiscalDTO.dataCriacao ?: notaFiscalExistente.dataCriacao, // Usa o valor existente como padrão
                    etiqueta = notaFiscalDTO.etiqueta,
                    valor = notaFiscalDTO.valor,
                    dataVencimento = notaFiscalDTO.dataVencimento,
                    descricao = notaFiscalDTO.descricao,
                    urlNuvem = notaFiscalDTO.urlNuvem,
                    isEmitida = notaFiscalDTO.isEmitida
                )
                notaFiscalService.save(notaFiscalAtualizada).toDTO() // Retorna o DTO
            }
            .map { updatedNotaFiscalDTO -> // Este map recebe o DTO e o encapsula no ResponseEntity
                ResponseEntity.ok(updatedNotaFiscalDTO)
            }
            .orElse(ResponseEntity.notFound().build())
    }

    @Operation(
        summary = "Excluir nota fiscal por ID",
        description = "Exclui uma nota fiscal do sistema com base no seu ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Nota fiscal excluída com sucesso"),
            ApiResponse(responseCode = "404", description = "Nota fiscal não encontrada"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    @DeleteMapping("/{id}")
    fun excluirNotaFiscal(@PathVariable id: Int): ResponseEntity<Void> {
        return if (notaFiscalService.existsById(id)) {
            notaFiscalService.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(
        summary = "Buscar notas fiscais por nome do cliente",
        description = "Retorna uma lista de notas fiscais cujo nome do cliente contenha o valor informado."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Notas fiscais encontradas com sucesso"),
            ApiResponse(responseCode = "400", description = "Requisição inválida"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    @GetMapping("/buscar-por-cliente")
    fun buscarPorNomeCliente(@RequestParam nome: String): ResponseEntity<List<NotaFiscalDTO>> {
        val notas = notaFiscalService.findByClienteNome(nome).map { it.toDTO() }
        return ResponseEntity.ok(notas)
    }

    @Operation(
        summary = "Buscar notas fiscais por data de criação",
        description = "Retorna uma lista de notas fiscais criadas na data informada (formato: yyyy-MM-dd)."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Notas fiscais encontradas com sucesso"),
            ApiResponse(responseCode = "400", description = "Data no formato inválido"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    @GetMapping("/buscar-por-data")
    fun buscarPorDataCriacao(@RequestParam data: String): ResponseEntity<List<NotaFiscalDTO>> {
        return try {
            val dataCriacao = LocalDate.parse(data)
            val notas = notaFiscalService.findByDataCriacao(dataCriacao).map { it.toDTO() }
            ResponseEntity.ok(notas)
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }


}
