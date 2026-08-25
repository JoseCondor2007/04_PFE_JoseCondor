import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Pelicula } from '../models/pelicula.model';

@Injectable({
  providedIn: 'root'
})
export class FormularioService {
  // BehaviorSubject para compartir la película a editar
  private peliculaEditarSubject = new BehaviorSubject<Pelicula | null>(null);
  peliculaEditar$ = this.peliculaEditarSubject.asObservable();

  // BehaviorSubject para controlar la visibilidad del formulario
  private mostrarFormularioSubject = new BehaviorSubject<boolean>(false);
  mostrarFormulario$ = this.mostrarFormularioSubject.asObservable();

  /**
   * Establece la película a editar y muestra el formulario
   */
  editarPelicula(pelicula: Pelicula): void {
    console.log('📝 FormularioService - editarPelicula:', pelicula);
    this.peliculaEditarSubject.next({ ...pelicula }); // Enviamos una copia
    this.mostrarFormularioSubject.next(true);
  }

  /**
   * Muestra el formulario para agregar una nueva película
   */
  mostrarFormularioAgregar(): void {
    console.log('📝 FormularioService - mostrarFormularioAgregar');
    this.peliculaEditarSubject.next(null); // Limpiamos la edición
    this.mostrarFormularioSubject.next(true);
  }

  /**
   * Oculta el formulario y limpia la edición
   */
  cancelarEdicion(): void {
    console.log('📝 FormularioService - cancelarEdicion');
    this.peliculaEditarSubject.next(null);
    this.mostrarFormularioSubject.next(false);
  }

  /**
   * Obtiene el valor actual de la película a editar (para uso síncrono)
   */
  getPeliculaEditar(): Pelicula | null {
    return this.peliculaEditarSubject.value;
  }

  /**
   * Obtiene el valor actual de la visibilidad del formulario
   */
  getMostrarFormulario(): boolean {
    return this.mostrarFormularioSubject.value;
  }
}