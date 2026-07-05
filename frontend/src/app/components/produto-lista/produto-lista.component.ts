import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Produto } from '../../models/produto.model';
import { ProdutoService } from '../../services/produto.service';

@Component({
  selector: 'app-produto-lista',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './produto-lista.component.html',
  styleUrl: './produto-lista.component.scss'
})
export class ProdutoListaComponent implements OnInit {

  produtos: Produto[] = [];
  carregando = false;
  erro: string | null = null;

  constructor(private produtoService: ProdutoService) {}

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
      },
      error: (err) => {
        console.error('Erro ao carregar produtos', err);
        this.erro = 'Não foi possível carregar os produtos. Verifique se o backend está rodando em ' +
          'http://localhost:8080.';
        this.carregando = false;
      }
    });
  }
}
