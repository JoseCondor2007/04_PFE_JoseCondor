import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PaisService } from '../../services/pais.service';

@Component({
  selector: 'app-pais-lista',
  standalone: true,
  imports: [CommonModule, FormsModule],  // Necesarios para *ngFor, *ngIf, ngModel
  templateUrl: './pais-lista.component.html',
  styleUrls: ['./pais-lista.component.css']
})
export class PaisListaComponent implements OnInit {
  paises: any[] = [];

  // Objeto para el formulario
  paisForm: any = {
    name: '',        // La API usa "name" en lugar de "nombre"
    capital: '',
    region: ''
  };

  // Variables para el modo edición
  editando: boolean = false;
  idEditando: number | string | null = null;

  constructor(private paisService: PaisService) { }

  ngOnInit(): void {
    this.cargarPaises(); // Carga inicial (GET)
  }

  // GET: Cargar la lista
  cargarPaises(): void {
    this.paisService.obtenerPaises().subscribe({
      next: (data: any) => {
        // La API puede devolver un array directo o un objeto con propiedad "data"
        this.paises = Array.isArray(data) ? data : (data.data || []);
      },
      error: (err) => console.error('Error al cargar países:', err)
    });
  }

  // POST o PUT: Guardar cambios
  guardarPais(): void {
    if (this.editando && this.idEditando !== null) {
      // Modo Editar (PUT)
      this.paisService.actualizarPais(this.idEditando, this.paisForm).subscribe(() => {
        this.cargarPaises(); // Sincronizar lista
        this.resetFormulario();
      });
    } else {
      // Modo Registrar (POST)
      this.paisService.registrarPais(this.paisForm).subscribe(() => {
        this.cargarPaises(); // Sincronizar lista
        this.resetFormulario();
      });
    }
  }

  // Preparar edición
  seleccionarParaEditar(pais: any): void {
    this.editando = true;
    this.idEditando = pais.id;
    // Clonamos el objeto para no modificar la tabla hasta guardar
    this.paisForm = { ...pais };
  }

  // DELETE: Eliminar con confirmación
  eliminarPais(id: number | string): void {
    if (confirm('¿Estás seguro de que deseas eliminar este país?')) {
      this.paisService.eliminarPais(id).subscribe(() => {
        this.cargarPaises(); // Sincronizar lista
      });
    }
  }

  // Resetear el formulario
  resetFormulario(): void {
    this.paisForm = { name: '', capital: '', region: '' };
    this.editando = false;
    this.idEditando = null;
  }
}