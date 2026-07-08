export interface CategoriaSimples {
  id: number;
  nome: string;
}

/**
 * Espelha o CategoriaResponseDTO do backend.
 * Usado na tela de cadastro/edição de produto, para popular o select.
 */
export interface Categoria {
  id: number;
  nome: string;
  descricao?: string;
}
