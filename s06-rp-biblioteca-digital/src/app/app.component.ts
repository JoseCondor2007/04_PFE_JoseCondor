import { Component } from '@angular/core';
import { LibroListaComponent } from './components/libro-lista/libro-lista.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [LibroListaComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'S06 - RP - BibliotecaDigital';
}