# Sistema de Controle de Estoque — Varejo Tradicional

Trabalho avaliativo da disciplina Programação II (2026/01),
Sistemas de Informação, UEMG — Unidade Passos.

Sistema web completo de controle de estoque para um varejo tradicional,
com cadastro de produtos, categorias, fornecedores e registro de
movimentações (entradas e saídas) de estoque.


## Tecnologias utilizadas

 Camada

 Front-end: Angular, TypeScript, Bootstrap 5 
 Back-end: Java 17, Spring Boot 3.3.4 (Spring Web, Spring Data JPA, Bean Validation)
 Banco de dados: PostgreSQL 
 Build back-end: Maven 
 Build front-end: Angular CLI / npm 


## Estrutura do projeto

SistemaEstoque/
├── backend/                # API REST em Spring Boot
│   ├── database/
│   │   └── dump_estoque.sql   # script de criação e população do banco
│   ├── src/main/java/com/uemg/estoque/
│   │   ├── config/          # configuração de CORS
│   │   ├── controller/      # endpoints REST
│   │   ├── dto/             # objetos de requisição/resposta
│   │   ├── exception/       # tratamento global de erros
│   │   ├── model/           # entidades JPA
│   │   ├── repository/      # repositórios Spring Data JPA
│   │   └── service/         # regras de negócio
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
└── frontend/                # SPA em Angular
    └── src/app/
        ├── components/      # produto-lista, produto-form
        ├── models/          # interfaces TypeScript
        └── services/        # consumo da API REST




## Como executar a aplicação

### Dependências necessárias

- **Java 17** ou superior ([JDK](https://adoptium.net/))
- **Maven** (ou use o `mvnw` incluso, se aplicável)
- **Node.js** (versão LTS recomendada) e **npm**
- **Angular CLI**: `npm install -g @angular/cli`
- **PostgreSQL** (versão 13 ou superior)

### 1. Configuração do banco de dados

Crie o banco e rode o script de criação/população:

```bash
psql -U postgres -c "CREATE DATABASE estoque_db;"
psql -U postgres -d estoque_db -f backend/database/dump_estoque.sql
```

Isso cria as 4 tabelas (`categoria`, `fornecedor`, `produto`,
`movimentacao_estoque`) e já popula com dados de exemplo para teste.

Se o usuário/senha do seu PostgreSQL local forem diferentes de
`postgres`/`postgres`, ajuste em
`backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/estoque_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 2. Como executar o back-end

```bash
cd backend
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`.

### 3. Como executar o front-end

Em outro terminal:

```bash
cd frontend
npm install
ng serve
```

A aplicação abre em `http://localhost:4200`.

### URLs do sistema

| Serviço | URL |
|---|---|
| Front-end (interface) | http://localhost:4200 |
| Back-end (API REST) | http://localhost:8080 |


## Principais funcionalidades

- **Gestão de Produtos** (CRUD completo): cadastro, listagem, edição e
  exclusão de produtos, com nome, descrição, código de barras, categoria,
  fornecedor, preços de custo/venda, quantidade em estoque, quantidade
  mínima e unidade de medida.
- **Alerta de estoque baixo**: produtos com quantidade em estoque igual ou
  abaixo da quantidade mínima configurada são destacados automaticamente na
  listagem.
- **Categorias e Fornecedores**: CRUD via API, usados para organizar e
  filtrar os produtos (consumidos pelos selects do formulário de produto).
- **Movimentação de Estoque**: registro de entradas e saídas de estoque,
  com atualização automática da quantidade disponível do produto e bloqueio
  de saídas que deixariam o estoque negativo.
- **Validação de dados**: tanto no front-end (Angular Reactive Forms) quanto
  no back-end (Bean Validation), com mensagens de erro claras.
- **Tratamento de erros centralizado**: todas as respostas de erro da API
  seguem um formato JSON padronizado, incluindo casos de recurso não
  encontrado (404), regra de negócio violada (400) e conflito de integridade
  referencial (409).
- **Interface responsiva**: construída com Bootstrap 5, se adapta a telas de
  desktop, tablet e celular.


## Endpoints disponíveis

Todas as respostas são em **JSON**. Todos os endpoints estão sob
`http://localhost:8080`.

### Produtos

| Método | Rota | Finalidade |
|---|---|---|
| GET | `/produtos` | Lista todos os produtos |
| GET | `/produtos/{id}` | Busca um produto específico |
| POST | `/produtos` | Cadastra um novo produto |
| PUT | `/produtos/{id}` | Atualiza um produto existente |
| DELETE | `/produtos/{id}` | Remove um produto |

**Exemplo de requisição — `POST /produtos`:**
```json
{
  "nome": "Feijão Carioca 1kg",
  "descricao": "Feijão tipo 1, pacote de 1kg",
  "codigoBarras": "7891000500107",
  "categoriaId": 1,
  "fornecedorId": 1,
  "precoCusto": 6.50,
  "precoVenda": 9.90,
  "quantidadeEstoque": 25,
  "quantidadeMinima": 8,
  "unidadeMedida": "UN",
  "ativo": true
}
```

**Exemplo de resposta — `GET /produtos/1`:**
```json
{
  "id": 1,
  "nome": "Arroz Branco 5kg",
  "descricao": "Arroz tipo 1, pacote de 5kg",
  "codigoBarras": "7891000100103",
  "categoria": { "id": 1, "nome": "Alimentos" },
  "fornecedor": { "id": 1, "nome": "Distribuidora Passos Ltda" },
  "precoCusto": 18.50,
  "precoVenda": 24.90,
  "quantidadeEstoque": 40,
  "quantidadeMinima": 10,
  "unidadeMedida": "UN",
  "dataCadastro": "2026-07-02T10:00:00",
  "ativo": true,
  "estoqueBaixo": false
}
```

### Categorias

| Método | Rota | Finalidade |
|---|---|---|
| GET | `/categorias` | Lista todas as categorias |
| GET | `/categorias/{id}` | Busca uma categoria específica |
| POST | `/categorias` | Cadastra uma nova categoria |
| PUT | `/categorias/{id}` | Atualiza uma categoria existente |
| DELETE | `/categorias/{id}` | Remove uma categoria |

**Exemplo de requisição — `POST /categorias`:**
```json
{
  "nome": "Congelados",
  "descricao": "Produtos que precisam de refrigeração"
}
```

### Fornecedores

| Método | Rota | Finalidade |
|---|---|---|
| GET | `/fornecedores` | Lista todos os fornecedores |
| GET | `/fornecedores/{id}` | Busca um fornecedor específico |
| POST | `/fornecedores` | Cadastra um novo fornecedor |
| PUT | `/fornecedores/{id}` | Atualiza um fornecedor existente |
| DELETE | `/fornecedores/{id}` | Remove um fornecedor |

**Exemplo de requisição — `POST /fornecedores`:**
```json
{
  "nome": "Novo Fornecedor Ltda",
  "cnpj": "11.222.333/0001-44",
  "telefone": "(35) 3999-0000",
  "email": "contato@novofornecedor.com.br",
  "endereco": "Rua Exemplo, 100 - Passos/MG"
}
```

### Movimentação de Estoque

| Método | Rota | Finalidade |
|---|---|---|
| POST | `/movimentacoes` | Registra uma entrada ou saída de estoque |
| GET | `/movimentacoes/produto/{produtoId}` | Lista o histórico de movimentações de um produto |

**Exemplo de requisição — `POST /movimentacoes`:**
```json
{
  "produtoId": 1,
  "tipo": "ENTRADA",
  "quantidade": 20,
  "motivo": "Reposição de estoque"
}
```
`tipo` aceita os valores `"ENTRADA"` ou `"SAIDA"`. Uma saída que deixaria o
estoque negativo é rejeitada com erro 400.

### Formato de erro padrão

Todos os erros da API retornam nesse formato:
```json
{
  "timestamp": "2026-07-08T14:30:00",
  "status": 404,
  "error": "Recurso não encontrado",
  "message": "Produto com id 999 não encontrado",
  "detalhes": null
}
```

| Status | Situação |
|---|---|
| 400 | Dados inválidos ou regra de negócio violada (ex: código de barras duplicado, estoque insuficiente) |
| 404 | Recurso não encontrado (id inexistente) |
| 409 | Conflito de integridade referencial (ex: excluir categoria/fornecedor em uso por algum produto) |
| 500 | Erro interno não esperado |

---

## Decisões técnicas relevantes

- **`ddl-auto=validate`**: o Hibernate confere se as entidades JPA batem com
  o schema criado pelo `dump_estoque.sql`, mas nunca altera o banco
  automaticamente — o dump é sempre a fonte da verdade da estrutura do
  banco de dados.
- **DTOs separados de request/response**: a API nunca expõe as entidades
  JPA diretamente, evitando vazamento de estrutura interna do banco e
  problemas de serialização com relacionamentos `LAZY`.
- **Regras de negócio centralizadas na camada de Service**: os Controllers
  apenas traduzem HTTP ⇄ chamadas de método; toda validação de negócio
  (duplicidade, estoque insuficiente, integridade referencial) vive nos
  Services.
- **Quantidade em estoque desnormalizada** no próprio Produto (em vez de
  sempre somar o histórico de movimentações): decisão de performance de
  leitura comum em sistemas de PDV/estoque reais, com a responsabilidade de
  manter a consistência sendo do `MovimentacaoEstoqueService`.
