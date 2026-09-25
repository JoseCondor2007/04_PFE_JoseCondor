import { Component, Output, EventEmitter, OnInit, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { Pelicula } from '../../models/pelicula.model';
import { FormularioService } from '../../services/formulario.service';

@Component({
  selector: 'app-formulario',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './formulario.component.html',
  styleUrls: ['./formulario.component.css']
})
export class FormularioComponent implements OnInit, OnDestroy {
  @Output() guardar = new EventEmitter<Omit<Pelicula, 'id'>>();
  @Output() cancelar = new EventEmitter<void>();

  pelicula: Omit<Pelicula, 'id'> = {
    titulo: '',
    director: '',
    anio: new Date().getFullYear(),
    genero: '',
    calificacion: 5,
    imagen: ''  // <-- Nuevo campo para la URL de la imagen
  };

  peliculaEditar: Pelicula | null = null;
  private subscription: Subscription = new Subscription();

  generos: string[] = [
    'Acción', 'Aventura', 'Animación', 'Comedia',
    'Crimen', 'Drama', 'Fantasía', 'Ciencia Ficción',
    'Misterio', 'Romance', 'Thriller', 'Bélica', 'Musical'
  ];

  constructor(private formularioService: FormularioService) {}

  ngOnInit(): void {
    this.resetFormulario();

    // Suscribirse a la película a editar
    this.subscription.add(
      this.formularioService.peliculaEditar$.subscribe(pelicula => {
        console.log('🔄 FormularioComponent - peliculaEditar recibida:', pelicula);
        this.peliculaEditar = pelicula;
        if (pelicula) {
          this.cargarPelicula(pelicula);
        } else {
          this.resetFormulario();
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  cargarPelicula(pelicula: Pelicula): void {
    console.log('✅ FormularioComponent - cargarPelicula:', pelicula);
    this.pelicula = {
      titulo: pelicula.titulo,
      director: pelicula.director,
      anio: pelicula.anio,
      genero: pelicula.genero,
      calificacion: pelicula.calificacion,
      imagen: pelicula.imagen || ''  // <-- Cargar la imagen si existe
    };
  }

  resetFormulario(): void {
    this.pelicula = {
      titulo: '',
      director: '',
      anio: new Date().getFullYear(),
      genero: '',
      calificacion: 5,
      imagen: ''  // <-- Resetear el campo imagen
    };
  }

  onSubmit(): void {
    if (this.pelicula.titulo.trim() && this.pelicula.director.trim()) {
      this.guardar.emit(this.pelicula);
      this.resetFormulario();
    }
  }

  onCancelar(): void {
    this.cancelar.emit();
    this.resetFormulario();
  }

  // Validar si la URL de la imagen es válida
  get imagenValida(): boolean {
    if (!this.pelicula.imagen) return true; // Campo opcional
    // Verificar si es una URL válida (empieza con http:// o https://)
    return /^https?:\/\/.+/.test(this.pelicula.imagen);
  }

  get esEdicion(): boolean {
    return !!this.peliculaEditar;
  }

  get tituloBoton(): string {
    return this.esEdicion ? '💾 Actualizar Película' : '➕ Agregar Película';
  }

  get tituloFormulario(): string {
    return this.esEdicion ? '✏️ Editar Película' : '🎬 Agregar Película';
  }
}