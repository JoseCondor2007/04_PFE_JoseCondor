import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Mascota } from './models/mascota.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  miFormulario!: FormGroup;
  listaMascotas: Mascota[] = [];
  private contadorId = 1;
  mensajeExito = '';

  // Especies disponibles para el dropdown
  especies: string[] = ['Perro', 'Gato', 'Ave', 'Conejo', 'Hámster', 'Reptil'];

  // Imagen temática de la portada (perrito)
  imagenPortada = 'https://images.pexels.com/photos/1108099/pexels-photo-1108099.jpeg?auto=compress&cs=tinysrgb&w=800';

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.miFormulario = this.fb.group({
      nombre:   ['', [Validators.required, Validators.minLength(2)]],
      especie:  ['', [Validators.required]],
      edad:     [null, [Validators.required, Validators.min(0), Validators.max(25)]],
      telefono: ['', [Validators.required, Validators.pattern(/^[0-9]{9}$/)]]
    });
  }

  // Getters cómodos para el HTML
  get nombre()   { return this.miFormulario.get('nombre')!; }
  get especie()  { return this.miFormulario.get('especie')!; }
  get edad()     { return this.miFormulario.get('edad')!; }
  get telefono() { return this.miFormulario.get('telefono')!; }

  guardar(): void {
    if (this.miFormulario.invalid) {
      this.miFormulario.markAllAsTouched();
      return;
    }

    const nuevaMascota: Mascota = {
      id: this.contadorId++,
      nombre:   this.miFormulario.value.nombre,
      especie:  this.miFormulario.value.especie,
      edad:     Number(this.miFormulario.value.edad),
      telefono: this.miFormulario.value.telefono,
      fechaRegistro: new Date()
    };

    this.listaMascotas.push(nuevaMascota);
    this.mensajeExito = `✅ Paciente "${nuevaMascota.nombre}" registrado correctamente.`;

    // Reset manual (requisito del reto)
    this.miFormulario.reset();

    setTimeout(() => (this.mensajeExito = ''), 3000);
  }

  eliminar(id: number): void {
    this.listaMascotas = this.listaMascotas.filter(m => m.id !== id);
  }

  // Clase CSS dinámica para el badge de especie
  getClaseEspecie(especie: string): string {
    switch (especie.toLowerCase()) {
      case 'perro':   return 'badge badge-perro';
      case 'gato':    return 'badge badge-gato';
      case 'ave':     return 'badge badge-ave';
      default:        return 'badge badge-otro';
    }
  }
}