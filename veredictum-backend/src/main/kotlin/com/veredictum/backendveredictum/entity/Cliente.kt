package com.veredictum.backendveredictum.entity

import com.fasterxml.jackson.annotation.JsonProperty
import com.veredictum.backendveredictum.dto.ClienteDTO
import jakarta.persistence.*
import jakarta.validation.constraints.*
import org.hibernate.validator.constraints.br.CNPJ
import org.hibernate.validator.constraints.br.CPF
import java.time.LocalDate

@Entity
@Table(name = "cliente")
data class Cliente(

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idCliente: Int? = null,

    @ManyToOne
    @JoinColumn(name = "fk_indicador")
    var indicador: Cliente? = null,

    @field:NotBlank(message = "O nome é obrigatório")
    @field:Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
    @field:Size(min = 2, message = "O nome deve ter pelo menos 2 caracteres")
    var nome: String? = null,

    @field:NotBlank(message = "O e-mail é obrigatório")
    @field:Email(message = "Formato de e-mail inválido")
    @field:Size(max = 255, message = "O e-mail deve ter no máximo 255 caracteres")
    var email: String? = null,

    @field:Size( max = 10, message = "O RG deve ter no máximo 10 caracteres")
    var rg: String? = null,

    @field:Size( max = 11, message = "O CPF deve ter no máximo 11 dígitos")
    var cpf: String? = null,

    @field:Size( max = 14, message = "O CNPJ deve ter no máximo 14 dígitos")
    var cnpj: String? = null,

    @field:NotNull(message = "A data de nascimento é obrigatória")
    @field:Past(message = "A data de nascimento deve estar no passado")
    var dataNascimento: LocalDate? = null,

    @field:NotNull(message = "A data de início é obrigatória")
    var dataInicio: LocalDate? = null,

    @field:Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres")
    var endereco: String? = null,

    @field:Size(min = 8, max = 8, message = "O CEP deve ter exatamente 8 dígitos")
    @field:Pattern(regexp = "\\d{8}", message = "O CEP deve conter apenas 8 dígitos numéricos")
    var cep: String? = null,

    var descricao: String? = null,

    @field:Size(min = 9, max = 9, message = "A inscrição estadual deve ter exatamente 9 dígitos")
    var inscricaoEstadual: String? = null,

    var isProBono: Boolean? = false,

    var isAtivo: Boolean = true,

    var isJuridico: Boolean? = cnpj != null,

    ) {
    constructor() : this(
        null,
        Cliente(
            0,
            null,
            "",
            "",
            "",
            "",
            null,
            LocalDate.now(),
            LocalDate.now(),
            "",
            "",
            null,
            null,
            false
        ),
        "",
        "",
        "",
        "",
        null,
        LocalDate.now(),
        LocalDate.now(),
        "",
        "",
        null,
        "",
        true,
        false
    )


        // Função para converter a entidade Cliente em um ClienteDTO
        fun toDTO(): ClienteDTO {
            return ClienteDTO(
                idCliente = this.idCliente?:0,
                nome = this.nome?:"",
                fkIndicador = this.indicador?.idCliente,
                email = this.email?:"",
                rg = this.rg?:"",
                cpf = this.cpf?:"",
                cnpj = this.cnpj?:"",
                dataNascimento = this.dataNascimento,
                dataInicio = this.dataInicio,
                endereco = this.endereco?:"",
                cep = this.cep?:"",
                descricao = this.descricao?:"",
                inscricaoEstadual = this.inscricaoEstadual?:"",
                isProBono = this.isProBono,
                isAtivo = this.isAtivo,
                isJuridico = !this.cnpj.isNullOrBlank()
            )
        }


}
