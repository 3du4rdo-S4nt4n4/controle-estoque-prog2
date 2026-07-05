import { CategoriaSimples } from './categoria.model';
import { FornecedorSimples } from './fornecedor.model';


 //Espelha o ProdutoResponseDTO do backend.

export interface Produto {
  id: number;
  nome: string;
  descricao?: string;
  codigoBarras?: string;
  categoria?: CategoriaSimples;
  fornecedor?: FornecedorSimples;
  precoCusto: number;
  precoVenda: number;
  quantidadeEstoque: number;
  quantidadeMinima: number;
  unidadeMedida: string;
  dataCadastro: string;
  ativo: boolean;
  estoqueBaixo: boolean;
}

  //Espelha o ProdutoRequestDTO do backend.
 
export interface ProdutoRequest {
  nome: string;
  descricao?: string;
  codigoBarras?: string;
  categoriaId?: number | null;
  fornecedorId?: number | null;
  precoCusto: number;
  precoVenda: number;
  quantidadeEstoque: number;
  quantidadeMinima: number;
  unidadeMedida: string;
  ativo?: boolean;
}
