import { Component } from '@angular/core';
import { PeliculaListaComponent } from './components/pelicula-lista/pelicula-lista.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [PeliculaListaComponent], // Importamos el componente standalone
  template: `<app-pelicula-lista></app-pelicula-lista>`,
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'S05-RP-GestordePeliculas';
}