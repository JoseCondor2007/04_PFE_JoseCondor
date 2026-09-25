import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Pelicula } from '../../models/pelicula.model';

@Component({
  selector: 'app-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent {
  @Input() pelicula!: Pelicula;
  @Output() editar = new EventEmitter<Pelicula>();
  @Output() eliminar = new EventEmitter<number>();

  // Imagen por defecto si falla la carga
  imagenPorDefecto = 'https://image.tmdb.org/t/p/w500/1E5baAaEse26fej7uHcjOgEE2t2.jpg';

  onEditar(): void {
    console.log('📤 CardComponent - onEditar:', this.pelicula);
    this.editar.emit(this.pelicula);
  }

  onEliminar(): void {
    if (confirm(`¿Seguro que quieres eliminar "${this.pelicula.titulo}"?`)) {
      this.eliminar.emit(this.pelicula.id);
    }
  }

  onImageError(): void {
    // Si la imagen falla, usamos la imagen por defecto
    this.pelicula.imagen = this.imagenPorDefecto;
  }

  getEstrellas(calificacion: number): string {
    const estrellas = Math.round(calificacion / 2);
    return '⭐'.repeat(estrellas) + '☆'.repeat(5 - estrellas);
  }
}