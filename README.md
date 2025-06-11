# Crud_Notas_Law_Code - Veredictum  

# 📦 API de Notas Fiscais


---

## 🚀 Endpoints


### 📄 Listar todas as notas fiscais
`GET`  
http://localhost:8080/notas-fiscais


---

### 🔍 Buscar nota fiscal por ID  
`GET`  
http://localhost:8080/notas-fiscais/1

---
### 🧾 Buscar notas fiscais por nome do cliente
`GET`
http://localhost:8080/notas-fiscais/buscar-por-cliente?nome=João

---
### 📅 Buscar notas fiscais por data de criação
`GET`
http://localhost:8080/notas-fiscais/buscar-por-data?data=2024-08-10

---

### ➕ Criar nova nota fiscal
`POST`  
http://localhost:8080/notas-fiscais

#### 🔧 Body (JSON)

{
  "fkCliente": 1,
  "etiqueta": "NF-123",
  "valor": 100.00,
  "dataVencimento": "2025-12-31",
  "descricao": "Serviço prestado",
  "urlNuvem": "https://drive.exemplo.com/nf123",
  "isEmitida": true
}

---

### 🆕 Atualizar uma nota fiscal
`PUT`  
http://localhost:8080/notas-fiscais/2

#### 🔧 Body (JSON)

{
  "idNotaFiscal": 2,
  "fkCliente": 1,
  "dataCriacao": "2024-05-15",
  "etiqueta": "NF-456",
  "valor": 250.50,
  "dataVencimento": "2025-12-31",
  "descricao": "Atualização da nota fiscal",
  "urlNuvem": "https://drive.exemplo.com/nf456",
  "isEmitida": false
}
---



### ❌ Excluir nota fiscal
`DELETE`
http://localhost:8080/notas-fiscais/1



