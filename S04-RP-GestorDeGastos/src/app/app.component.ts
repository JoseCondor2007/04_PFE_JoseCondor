import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './components/navbar/navbar.component';
import { GastoCardComponent } from './components/gasto-card/gasto-card.component';

export interface Gasto {
  id: number;
  descripcion: string;
  monto: number;
  categoria: string;
  fecha: string;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, NavbarComponent, GastoCardComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  // ===== TÍTULO DE LA APP =====
  tituloApp = '💰 Gestor de Gastos';

  // ===== LISTA DE GASTOS (en memoria) =====
  gastos: Gasto[] = [
    {
      id: 1,
      descripcion: 'Compra de supermercado',
      monto: 150.50,
      categoria: 'Alimentación',
      fecha: '2026-08-28'
    },
    {
      id: 2,
      descripcion: 'Pasaje de bus',
      monto: 5.00,
      categoria: 'Transporte',
      fecha: '2026-08-27'
    },
    {
      id: 3,
      descripcion: 'Cena con amigos',
      monto: 80.00,
      categoria: 'Entretenimiento',
      fecha: '2026-08-26'
    }
  ];

  // ===== NUEVO GASTO (formulario) =====
  nuevoGasto: Omit<Gasto, 'id'> = {
    descripcion: '',
    monto: 0,
    categoria: '',
    fecha: ''
  };

  // ===== CATEGORÍAS =====
  categorias: string[] = ['Alimentación', 'Transporte', 'Entretenimiento', 'Otros'];

  // ===== ID INCREMENTAL =====
  private nextId: number = 4;

  // ===== AGREGAR GASTO =====
  agregarGasto(): void {
    if (this.formularioValido) {
      const gasto: Gasto = {
        id: this.nextId++,
        ...this.nuevoGasto
      };
      this.gastos.push(gasto);
      this.resetearFormulario();
    }
  }

  // ===== ELIMINAR GASTO =====
  eliminarGasto(id: number): void {
    this.gastos = this.gastos.filter(g => g.id !== id);
  }

  // ===== RESETEAR FORMULARIO =====
  resetearFormulario(): void {
    this.nuevoGasto = {
      descripcion: '',
      monto: 0,
      categoria: '',
      fecha: ''
    };
  }

  // ===== VALIDACIÓN DEL FORMULARIO =====
  get formularioValido(): boolean {
    return (
      this.nuevoGasto.descripcion.trim() !== '' &&
      this.nuevoGasto.monto > 0 &&
      this.nuevoGasto.categoria !== '' &&
      this.nuevoGasto.fecha !== ''
    );
  }

  // ===== TOTAL DE GASTOS =====
  get totalGastos(): number {
    return this.gastos.reduce((sum, g) => sum + g.monto, 0);
  }
}