import { Component, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AppComponent {
  nombre: string = 'José Manuel Cóndor Alcalá';
  carrera: string = 'Análisis de Sistemas Empresariales';
  edad: number = 19;
  correo: string = 'jose.condor@vallegrande.edu.pe';
  descripcion: string = 'Estudiante apasionado por el desarrollo de software y las nuevas tecnologías.';
  fraseFavorita: string = '"La creatividad es la inteligencia divirtiéndose." - Albert Einstein';
}