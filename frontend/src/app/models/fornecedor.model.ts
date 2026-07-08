export interface FornecedorSimples {
  id: number;
  nome: string;
}

/**
 * Espelha o FornecedorResponseDTO do backend.
 * Usado na tela de cadastro/edição de produto, para popular o select.
 */
export interface Fornecedor {
  id: number;
  nome: string;
  cnpj?: string;
  telefone?: string;
  email?: string;
  endereco?: string;
}
