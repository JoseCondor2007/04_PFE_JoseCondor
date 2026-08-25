import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  // Información principal del sistema
  nombreSistema: string = 'DirectivaCorp';
  tituloPrincipal: string = 'Directorio Institucional';
  descripcionSistema: string = 'Gestión centralizada de contactos, colaboradores y áreas corporativas.';

  // Contadores y métricas requeridas
  totalContactos: number = 4;
  personalActivo: number = 3;
  tasaDisponibilidad: number = 75; // Métrica (%)
  eficienciaRed: string = '98.5%';     // Métrica de rendimiento

  // Listado de datos corporativos (*ngFor)
  listaContactos = [
    { nombre: 'Ana María Gómez', cargo: 'Gerente de Proyectos', correo: 'ana.gomez@vallegrande.edu.pe', area: 'Gestión', estado: 'Disponible' },
    { nombre: 'Carlos Ruiz Tapia', cargo: 'Desarrollador Front-end', correo: 'carlos.ruiz@vallegrande.edu.pe', area: 'Sistemas', estado: 'En Reunión' },
    { nombre: 'Lucía Mendoza', cargo: 'Especialista en Marketing', correo: 'lucia.mendoza@vallegrande.edu.pe', area: 'Comercial', estado: 'Disponible' },
    { nombre: 'David Torres', cargo: 'Soporte Técnico IT', correo: 'david.torres@vallegrande.edu.pe', area: 'Sistemas', estado: 'Disponible' }
  ];

  // Estado interactivo
  mensajeEstado: string = 'Sistema operando correctamente.';

  // Botón Interactivo 1: Sincronizar datos y contadores
  sincronizarDatos(): void {
    this.totalContactos = this.listaContactos.length;
    this.personalActivo = this.listaContactos.filter(c => c.estado === 'Disponible').length;
    this.tasaDisponibilidad = Math.round((this.personalActivo / this.totalContactos) * 100);
    this.mensajeEstado = '¡Sincronización completada con éxito!';
  }

  // Botón Interactivo 2: Alternar indicador métrico
  alternarMetrica(): void {
    this.eficienciaRed = this.eficienciaRed === '98.5%' ? '99.9%' : '98.5%';
    this.mensajeEstado = 'Métrica de eficiencia del sistema actualizada.';
  }
}