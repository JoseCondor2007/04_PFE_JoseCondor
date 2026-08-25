import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Pelicula } from './models/pelicula.model';
import { PeliculaService } from './services/pelicula.service';
import { FormularioService } from './services/formulario.service';
import { NavbarComponent } from './components/navbar/navbar.component';
import { ListaComponent } from './components/lista/lista.component';
import { FormularioComponent } from './components/formulario/formulario.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, NavbarComponent, ListaComponent, FormularioComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  peliculas: Pelicula[] = [];
  peliculaEditar: Pelicula | null = null;
  mostrarFormulario = false;

  constructor(
    private peliculaService: PeliculaService,
    private formularioService: FormularioService
  ) {}

  ngOnInit(): void {
    this.cargarPeliculas();

    this.formularioService.mostrarFormulario$.subscribe(
      mostrar => this.mostrarFormulario = mostrar
    );

    this.formularioService.peliculaEditar$.subscribe(
      pelicula => this.peliculaEditar = pelicula
    );
  }

  cargarPeliculas(): void {
    this.peliculas = this.peliculaService.getPeliculas();
    // Actualizar el contador en el navbar
    this.actualizarContador();
  }

  actualizarContador(): void {
    // El contador se actualiza automáticamente porque `peliculas.length` cambia
    // y Angular detecta el cambio en el template
  }

  mostrarFormularioAgregar(): void {
    this.formularioService.mostrarFormularioAgregar();
  }

  guardarPelicula(pelicula: Omit<Pelicula, 'id'>): void {
    if (this.peliculaEditar) {
      this.peliculaService.updatePelicula(this.peliculaEditar.id, pelicula);
    } else {
      this.peliculaService.addPelicula(pelicula);
    }
    this.cargarPeliculas();
    this.formularioService.cancelarEdicion();
  }

  editarPelicula(pelicula: Pelicula): void {
    this.formularioService.editarPelicula(pelicula);
  }

  eliminarPelicula(id: number): void {
    if (confirm('¿Estás seguro de eliminar esta película?')) {
      this.peliculaService.deletePelicula(id);
      this.cargarPeliculas();
      if (this.peliculaEditar?.id === id) {
        this.formularioService.cancelarEdicion();
      }
    }
  }

  cancelarEdicion(): void {
    this.formularioService.cancelarEdicion();
  }
}