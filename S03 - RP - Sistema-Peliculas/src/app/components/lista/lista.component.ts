import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Pelicula } from '../../models/pelicula.model';
import { CardComponent } from '../card/card.component';

@Component({
  selector: 'app-lista',
  standalone: true,
  imports: [CommonModule, CardComponent],
  templateUrl: './lista.component.html',
  styleUrls: ['./lista.component.css']
})
export class ListaComponent {
  @Input() peliculas: Pelicula[] = [];
  @Output() editar = new EventEmitter<Pelicula>();
  @Output() eliminar = new EventEmitter<number>();

  onEditar(pelicula: Pelicula): void {
    console.log('📤 ListaComponent - onEditar:', pelicula);
    this.editar.emit(pelicula);
  }

  onEliminar(id: number): void {
    this.eliminar.emit(id);
  }
}