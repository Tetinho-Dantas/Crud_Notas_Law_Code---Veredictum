package com.veredictum.backendveredictum.services

import com.veredictum.backendveredictum.entity.Cliente // Importe a entidade Cliente
import com.veredictum.backendveredictum.repository.ClienteRepository // Importe o ClienteRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class ClienteService(
    private val clienteRepository: ClienteRepository
) {

    fun findById(id: Int): Optional<Cliente> {
        return clienteRepository.findById(id)
    }


    fun save(cliente: Cliente): Cliente {
        return clienteRepository.save(cliente)
    }

    fun findAll(): List<Cliente> {
        return clienteRepository.findAll()
    }

    fun existsById(id: Int): Boolean {
        return clienteRepository.existsById(id)
    }

    fun deleteById(id: Int) {
        clienteRepository.deleteById(id)
    }
}
