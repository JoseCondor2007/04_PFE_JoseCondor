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
  // Datos del sistema corporativo
  nombreSistema: string = 'DirectivaCorp';
  tituloPrincipal: string = 'Directorio Institucional Ejecutivo';
  descripcionSistema: string = 'Plataforma centralizada de gestión, control y consulta rápida del personal y áreas corporativas.';

  // Contadores y métricas dinámicas
  totalContactos: number = 4;
  contactosActivos: number = 3;
  porcentajeDisponibilidad: number = 75; // Métrica calculada (%)
  eficienciaOperativa: string = '98.4%';   // Métrica de rendimiento

  // Listado de contactos corporativos
  listaContactos = [
    { nombre: 'Ana María Gómez', cargo: 'Gerente de Proyectos', correo: 'ana.gomez@vallegrande.edu.pe', area: 'Gestión', estado: 'Disponible' },
    { nombre: 'Carlos Ruiz Tapia', cargo: 'Desarrollador Front-end', correo: 'carlos.ruiz@vallegrande.edu.pe', area: 'Sistemas', estado: 'En Reunión' },
    { nombre: 'Lucía Mendoza', cargo: 'Especialista en Marketing', correo: 'lucia.mendoza@vallegrande.edu.pe', area: 'Comercial', estado: 'Disponible' },
    { nombre: 'David Torres', cargo: 'Soporte Técnico IT', correo: 'david.torres@vallegrande.edu.pe', area: 'Sistemas', estado: 'Disponible' }
  ];

  // Estado interactivo para retroalimentación visual
  estadoMensaje: string = 'Sistema operando con normalidad y métricas sincronizadas.';
  tipoAlerta: string = 'success';

  // Botón Interactivo 1: Sincronizar y actualizar contadores/métricas
  sincronizarBase(): void {
    this.totalContactos = this.listaContactos.length;
    this.contactosActivos = this.listaContactos.filter(c => c.estado === 'Disponible').length;
    this.porcentajeDisponibilidad = Math.round((this.contactosActivos / this.totalContactos) * 100);
    this.eficienciaOperativa = '99.1%';
    this.estadoMensaje = '¡Sincronización exitosa! Contadores y métricas recalculados.';
    this.tipoAlerta = 'success';
  }

  // Botón Interactivo 2: Alternar simulación de rendimiento
  alternarMetricas(): void {
    this.eficienciaOperativa = this.eficienciaOperativa === '98.4%' ? '95.8%' : '98.4%';
    this.estadoMensaje = 'Métrica de eficiencia actualizada temporalmente por el sistema.';
    this.tipoAlerta = 'info';
  }
}