import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { forkJoin } from 'rxjs';

import { Produto, ProdutoRequest } from '../../models/produto.model';
import { Categoria } from '../../models/categoria.model';
import { Fornecedor } from '../../models/fornecedor.model';
import { ProdutoService } from '../../services/produto.service';
import { CategoriaService } from '../../services/categoria.service';
import { FornecedorService } from '../../services/fornecedor.service';

@Component({
  selector: 'app-produto-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './produto-form.component.html',
  styleUrl: './produto-form.component.scss'
})
export class ProdutoFormComponent implements OnInit, OnChanges {

  @Input() produtoEmEdicao: Produto | null = null;

  @Output() salvo = new EventEmitter<void>();
  @Output() cancelado = new EventEmitter<void>();

  private fb = inject(FormBuilder);
  private produtoService = inject(ProdutoService);
  private categoriaService = inject(CategoriaService);
  private fornecedorService = inject(FornecedorService);
  private cdr = inject(ChangeDetectorRef);

  categorias: Categoria[] = [];
  fornecedores: Fornecedor[] = [];
  dadosAuxiliaresCarregados = false;

  salvando = false;
  erroSalvar: string | null = null;

  form = this.fb.group({
    nome: ['', [Validators.required, Validators.maxLength(150)]],
    descricao: [''],
    codigoBarras: [''],
    categoriaId: [null as number | null],
    fornecedorId: [null as number | null],
    precoCusto: [0, [Validators.required, Validators.min(0)]],
    precoVenda: [0, [Validators.required, Validators.min(0)]],
    quantidadeEstoque: [0, [Validators.required, Validators.min(0)]],
    quantidadeMinima: [0, [Validators.required, Validators.min(0)]],
    unidadeMedida: ['UN', [Validators.required, Validators.maxLength(10)]],
    ativo: [true]
  });

  ngOnInit(): void {
    forkJoin({
      categorias: this.categoriaService.listarTodas(),
      fornecedores: this.fornecedorService.listarTodos()
    }).subscribe({
      next: ({ categorias, fornecedores }) => {
        this.categorias = categorias;
        this.fornecedores = fornecedores;
        this.dadosAuxiliaresCarregados = true;
        this.preencherFormularioSeEdicao();
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Erro ao carregar categorias/fornecedores', err);
        this.dadosAuxiliaresCarregados = true;
        this.preencherFormularioSeEdicao();
        this.cdr.markForCheck();
      }
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['produtoEmEdicao'] && this.dadosAuxiliaresCarregados) {
      this.preencherFormularioSeEdicao();
    }
  }

  get modoEdicao(): boolean {
    return this.produtoEmEdicao !== null;
  }

  private preencherFormularioSeEdicao(): void {
    if (this.produtoEmEdicao) {
      this.form.patchValue({
        nome: this.produtoEmEdicao.nome,
        descricao: this.produtoEmEdicao.descricao,
        codigoBarras: this.produtoEmEdicao.codigoBarras,
        categoriaId: this.produtoEmEdicao.categoria?.id ?? null,
        fornecedorId: this.produtoEmEdicao.fornecedor?.id ?? null,
        precoCusto: this.produtoEmEdicao.precoCusto,
        precoVenda: this.produtoEmEdicao.precoVenda,
        quantidadeEstoque: this.produtoEmEdicao.quantidadeEstoque,
        quantidadeMinima: this.produtoEmEdicao.quantidadeMinima,
        unidadeMedida: this.produtoEmEdicao.unidadeMedida,
        ativo: this.produtoEmEdicao.ativo
      });
    } else {
      this.form.reset({
        nome: '',
        descricao: '',
        codigoBarras: '',
        categoriaId: null,
        fornecedorId: null,
        precoCusto: 0,
        precoVenda: 0,
        quantidadeEstoque: 0,
        quantidadeMinima: 0,
        unidadeMedida: 'UN',
        ativo: true
      });
    }
  }

  salvar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.salvando = true;
    this.erroSalvar = null;

    const valores = this.form.value;
    const payload: ProdutoRequest = {
      nome: valores.nome!,
      descricao: valores.descricao || undefined,
      codigoBarras: valores.codigoBarras || undefined,
      categoriaId: valores.categoriaId ?? null,
      fornecedorId: valores.fornecedorId ?? null,
      precoCusto: valores.precoCusto!,
      precoVenda: valores.precoVenda!,
      quantidadeEstoque: valores.quantidadeEstoque!,
      quantidadeMinima: valores.quantidadeMinima!,
      unidadeMedida: valores.unidadeMedida!,
      ativo: valores.ativo ?? true
    };

    const requisicao = this.modoEdicao
      ? this.produtoService.atualizar(this.produtoEmEdicao!.id, payload)
      : this.produtoService.criar(payload);

    requisicao.subscribe({
      next: () => {
        this.salvando = false;
        this.salvo.emit();
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.salvando = false;
        this.erroSalvar = err?.error?.message || 'Erro ao salvar o produto. Verifique os dados e tente novamente.';
        console.error('Erro ao salvar produto', err);
        this.cdr.markForCheck();
      }
    });
  }

  cancelar(): void {
    this.cancelado.emit();
  }
}