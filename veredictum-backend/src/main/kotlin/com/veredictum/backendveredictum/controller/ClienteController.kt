package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.entity.Cliente
import com.veredictum.backendveredictum.repository.ClienteRepository
import com.veredictum.backendveredictum.dto.ClienteDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

@Tag(name = "Clientes", description = "Endpoints para gerenciar clientes")
@RestController
@RequestMapping("/clientes")
class ClienteController(
    val repository: ClienteRepository
) {

    @Operation(
        summary = "Buscar todos os clientes",
        description = "Retorna todos os clientes ativos ordenado por isAtivo. NotFound se nenhum cliente for encontrado."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Clientes retornados com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhum cliente encontrado")
        ]
    )
    @GetMapping
    fun buscarTodos(): ResponseEntity<List<ClienteDTO>> {
        val clientes = repository.findAllOrderByIsAtivo()
        return if (clientes.isEmpty()) {
            ResponseEntity.noContent().build() // 204 No Content
        } else {
            // Converte a lista de clientes para DTOs
            ResponseEntity.ok(clientes.map { it.toDTO() }) // 200 OK com lista de clientes
        }
    }

    @Operation(
        summary = "Buscar cliente por ID",
        description = "Retorna o cliente com o ID fornecido."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            ApiResponse(responseCode = "404", description = "Cliente não encontrado")
        ]
    )
    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<ClienteDTO> {
        return repository.findById(id)
            .map { cliente -> ResponseEntity.ok(cliente.toDTO()) }
            .orElse(ResponseEntity.notFound().build())
    }

    @Operation(
        summary = "Cadastrar novo cliente",
        description = "Cria um novo cliente e retorna o cliente cadastrado."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados inválidos"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    @PostMapping("/cadastrar-inativo")
    fun cadastrarInativo(@RequestBody @Valid novoClienteDTO: ClienteDTO): ResponseEntity<ClienteDTO> {
        val indicador = novoClienteDTO.fkIndicador?.let { id ->
            repository.findById(id).orElseThrow {
                ResponseStatusException(HttpStatus.BAD_REQUEST, "Indicador com ID $id não encontrado")
            }
        }

        val novoCliente = Cliente(
            nome = novoClienteDTO.nome,
            indicador = indicador,
            email = novoClienteDTO.email,
            rg = novoClienteDTO.rg,
            cpf = novoClienteDTO.cpf,
            cnpj = novoClienteDTO.cnpj,
            dataNascimento = novoClienteDTO.dataNascimento,
            dataInicio = novoClienteDTO.dataInicio,
            endereco = novoClienteDTO.endereco,
            cep = novoClienteDTO.cep,
            descricao = novoClienteDTO.descricao,
            inscricaoEstadual = novoClienteDTO.inscricaoEstadual,
            isProBono = novoClienteDTO.isProBono,
            isAtivo = false, // Valor padrão
            isJuridico = novoClienteDTO.isJuridico,
        )

        val clienteSalvo = repository.save(novoCliente)
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo.toDTO())
    }

    @Operation(
        summary = "Atualizar cliente",
        description = "Atualiza os dados do cliente com o ID informado. Se o corpo contiver um ID diferente, é ignorado."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            ApiResponse(responseCode = "400", description = "ID inconsistente entre a URL e o corpo"),
            ApiResponse(responseCode = "404", description = "Cliente não encontrado")
        ]
    )
    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody @Valid clienteAtualizado: Cliente): ResponseEntity<ClienteDTO> {
        if (clienteAtualizado.idCliente != null && clienteAtualizado.idCliente != id) {
            return ResponseEntity.badRequest().build()
        }
        return repository.findById(id).map { clienteExistente ->
            clienteAtualizado.idCliente = id
            val clienteSalvo = repository.save(clienteAtualizado)
            ResponseEntity.ok(clienteSalvo.toDTO())
        }.orElseGet {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(
        summary = "Atualização parcial do cliente",
        description = "Atualiza parcialmente os dados do cliente utilizando um mapa de atualizações."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Cliente atualizado parcialmente com sucesso"),
            ApiResponse(responseCode = "404", description = "Cliente não encontrado")
        ]
    )
    @PatchMapping("/{id}")
    fun atualizarParcial(
        @PathVariable id: Int,
        @RequestBody atualizacoes: Map<String, Any>
    ): ResponseEntity<ClienteDTO> {
        return repository.findById(id).map { cliente ->
            atualizacoes.forEach { (chave, valor) ->
                val valorStr = valor.toString()
                if (valorStr.isNotBlank()) {
                    when (chave) {
                        "idCliente" -> cliente.idCliente = id
                        "nome" -> cliente.nome = valorStr
                        "email" -> cliente.email = valorStr
                        "rg" -> cliente.rg = valorStr
                        "cpf" -> cliente.cpf = valorStr
                        "cnpj" -> cliente.cnpj = valorStr
                        "dataNascimento" -> cliente.dataNascimento = LocalDate.parse(valorStr)
                        "dataInicio" -> cliente.dataInicio = LocalDate.parse(valorStr)
                        "endereco" -> cliente.endereco = valorStr
                        "cep" -> cliente.cep = valorStr
                        "descricao" -> cliente.descricao = valorStr
                        "inscricaoEstadual" -> cliente.inscricaoEstadual = valorStr
                        "proBono" -> cliente.isProBono = valorStr.toBoolean()
                        "ativo" -> cliente.isAtivo = valorStr.toBoolean()
                        "juridico" -> cliente.isJuridico = valorStr.toBoolean()
                        else -> {} // Ignora campos não reconhecidos
                    }
                }
            }
            val clienteAtualizado = repository.save(cliente)
            ResponseEntity.ok(clienteAtualizado.toDTO())
        }.orElseGet {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(
        summary = "Inativar cliente",
        description = "Marca o cliente como inativo (ativo=false)."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Cliente inativado com sucesso"),
            ApiResponse(responseCode = "404", description = "Cliente não encontrado")
        ]
    )
    @PatchMapping("/{id}/inativar")
    fun inativar(@PathVariable id: Int): ResponseEntity<Void> {
        val clienteOptional = repository.findById(id)
        return if (clienteOptional.isPresent) {
            val cliente = clienteOptional.get()
            cliente.isAtivo = false
            repository.save(cliente)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @Operation(
        summary = "Ativar cliente",
        description = "Marca o cliente como ativo (ativo=true)."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Cliente ativado com sucesso"),
            ApiResponse(responseCode = "404", description = "Cliente não encontrado")
        ]
    )
    @PatchMapping("/{id}/ativar")
    fun ativar(@PathVariable id: Int): ResponseEntity<Void> {
        val clienteOptional = repository.findById(id)
        return if (clienteOptional.isPresent) {
            val cliente = clienteOptional.get()
            cliente.isAtivo = true
            repository.save(cliente)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.status(404).build()
        }
    }
}

