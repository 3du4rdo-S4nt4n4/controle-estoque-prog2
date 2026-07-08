import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Produto } from '../../models/produto.model';
import { ProdutoService } from '../../services/produto.service';
import { ProdutoFormComponent } from '../produto-form/produto-form.component';

@Component({
  selector: 'app-produto-lista',
  standalone: true,
  imports: [CommonModule, ProdutoFormComponent],
  templateUrl: './produto-lista.component.html',
  styleUrl: './produto-lista.component.scss'
})
export class ProdutoListaComponent implements OnInit {

  private produtoService = inject(ProdutoService);
  private cdr = inject(ChangeDetectorRef);

  produtos: Produto[] = [];
  carregando = false;
  erro: string | null = null;

  mostrarFormulario = false;
  produtoEmEdicao: Produto | null = null;

  produtoParaExcluir: Produto | null = null;
  excluindo = false;
  erroExcluir: string | null = null;

  ngOnInit(): void {
    this.carregarProdutos();
  }

  carregarProdutos(): void {
    this.carregando = true;
    this.erro = null;

    this.produtoService.listarTodos().subscribe({
      next: (produtos) => {
        this.produtos = produtos;
        this.carregando = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Erro ao carregar produtos', err);
        this.erro = 'Não foi possível carregar os produtos. Verifique se o backend está rodando em ' +
          'http://localhost:8080.';
        this.carregando = false;
        this.cdr.markForCheck();
      }
    });
  }

  // ================= Cadastro / Edicao =================

  abrirNovoProduto(): void {
    this.produtoEmEdicao = null;
    this.mostrarFormulario = true;
  }

  abrirEdicao(produto: Produto): void {
    this.produtoEmEdicao = produto;
    this.mostrarFormulario = true;
  }

  fecharFormulario(): void {
    this.mostrarFormulario = false;
    this.produtoEmEdicao = null;
  }

  aoSalvarProduto(): void {
    this.fecharFormulario();
    this.carregarProdutos();
  }

  // ================= Exclusao =================

  pedirConfirmacaoExclusao(produto: Produto): void {
    this.produtoParaExcluir = produto;
    this.erroExcluir = null;
  }

  cancelarExclusao(): void {
    this.produtoParaExcluir = null;
    this.erroExcluir = null;
  }

  confirmarExclusao(): void {
    if (!this.produtoParaExcluir) {
      return;
    }
    this.excluindo = true;
    this.erroExcluir = null;

    this.produtoService.deletar(this.produtoParaExcluir.id).subscribe({
      next: () => {
        this.excluindo = false;
        this.produtoParaExcluir = null;
        this.carregarProdutos();
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.excluindo = false;
        this.erroExcluir = err?.error?.message || 'Não foi possível excluir este produto.';
        console.error('Erro ao excluir produto', err);
        this.cdr.markForCheck();
      }
    });
  }
}
