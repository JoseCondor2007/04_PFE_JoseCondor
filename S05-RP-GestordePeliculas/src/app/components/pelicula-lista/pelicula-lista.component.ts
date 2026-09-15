import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common'; // Necesario para @for y @if
import { PeliculaService } from '../../services/pelicula.service';

@Component({
  selector: 'app-pelicula-lista',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pelicula-lista.component.html',
  styleUrls: ['./pelicula-lista.component.css']
})
export class PeliculaListaComponent {
  
  private peliculaService = inject(PeliculaService);
  
  // Obtenemos los datos del servicio
  peliculas = this.peliculaService.obtenerPeliculas();
}