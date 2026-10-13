import { Component } from '@angular/core';

interface Inscripcion {
  id: number;
  nombre: string;
  correo: string;
  especialidad: string;
  turno: string;
  modalidad: string;
  nivel: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  /** Mínimo de inscripciones requeridas */
  readonly MINIMO_INSCRIPCIONES = 6;

  /** Cantidad de referencia para el ancho visual de la barra (12 = 100%) */
  readonly REFERENCIA_BARRA = 12;

  nuevoParticipante: Inscripcion = this.getParticipanteVacio();
  inscripciones: Inscripcion[] = [];
  private contadorId = 1;

  constructor() {
    // Datos de ejemplo precargados (6 inscripciones)
    this.inscripciones = [
      {
        id: this.contadorId++,
        nombre: 'Valery Chumpitaz',
        correo: 'valery@email.com',
        especialidad: 'Desarrollo',
        turno: 'Mañana',
        modalidad: 'Presencial',
        nivel: 'Avanzado'
      },
      {
        id: this.contadorId++,
        nombre: 'Carlos Gómez',
        correo: 'carlos@email.com',
        especialidad: 'Redes',
        turno: 'Noche',
        modalidad: 'Virtual',
        nivel: 'Intermedio'
      },
      {
        id: this.contadorId++,
        nombre: 'Ana Torres',
        correo: 'ana.torres@email.com',
        especialidad: 'Inteligencia Artificial',
        turno: 'Mañana',
        modalidad: 'Presencial',
        nivel: 'Principiante'
      },
      {
        id: this.contadorId++,
        nombre: 'Luis Ramírez',
        correo: 'luis.ramirez@email.com',
        especialidad: 'Desarrollo',
        turno: 'Noche',
        modalidad: 'Virtual',
        nivel: 'Avanzado'
      },
      {
        id: this.contadorId++,
        nombre: 'María Fernández',
        correo: 'maria.f@email.com',
        especialidad: 'Redes',
        turno: 'Mañana',
        modalidad: 'Virtual',
        nivel: 'Intermedio'
      },
      {
        id: this.contadorId++,
        nombre: 'Jorge Mendoza',
        correo: 'jorge.m@email.com',
        especialidad: 'Inteligencia Artificial',
        turno: 'Noche',
        modalidad: 'Presencial',
        nivel: 'Principiante'
      }
    ];
  }

  getParticipanteVacio(): Inscripcion {
    return {
      id: 0,
      nombre: '',
      correo: '',
      especialidad: 'Desarrollo',
      turno: 'Mañana',
      modalidad: 'Presencial',
      nivel: 'Principiante'
    };
  }

  registrarInscripcion(form: any): void {
    if (form.invalid) return;

    this.inscripciones.push({
      ...this.nuevoParticipante,
      id: this.contadorId++
    });

    this.nuevoParticipante = this.getParticipanteVacio();
    form.resetForm(this.nuevoParticipante);
  }

  eliminarInscripcion(id: number): void {
    this.inscripciones = this.inscripciones.filter(i => i.id !== id);
  }

  // ==== Indicadores ====
  get totalInscritos(): number {
    return this.inscripciones.length;
  }

  get totalManana(): number {
    return this.inscripciones.filter(i => i.turno === 'Mañana').length;
  }

  get totalNoche(): number {
    return this.inscripciones.filter(i => i.turno === 'Noche').length;
  }

  // ==== Regla del mínimo ====
  get cumpleMinimo(): boolean {
    return this.inscripciones.length >= this.MINIMO_INSCRIPCIONES;
  }

  get faltantes(): number {
    return Math.max(this.MINIMO_INSCRIPCIONES - this.inscripciones.length, 0);
  }

  /**
   * Porcentaje SIN LÍMITE.
   * - 6 inscripciones  → 50%
   * - 12 inscripciones → 100%
   * - 18 inscripciones → 150% (sigue creciendo)
   */
  get porcentajeProgreso(): number {
    return (this.inscripciones.length / this.REFERENCIA_BARRA) * 100;
  }

  /**
   * Porcentaje acotado solo para el ancho visual de la barra.
   * La barra no puede salirse del contenedor.
   */
  get anchoBarra(): number {
    return Math.min(this.porcentajeProgreso, 100);
  }

  /**
   * Excedente por encima del 100%.
   */
  get excedente(): number {
    return Math.max(this.inscripciones.length - this.REFERENCIA_BARRA, 0);
  }

  finalizarRegistro(): void {
    if (!this.cumpleMinimo) {
      alert(`Debes registrar al menos ${this.MINIMO_INSCRIPCIONES} participantes.`);
      return;
    }
    alert(`✅ Registro finalizado con ${this.totalInscritos} inscripciones.`);
  }
}