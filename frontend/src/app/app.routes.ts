import { Routes } from '@angular/router';
import { ProdutoListaComponent } from './components/produto-lista/produto-lista.component';

export const routes: Routes = [
     { path: '', component: ProdutoListaComponent },
  { path: 'produtos', component: ProdutoListaComponent }
];
