import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Mascota } from './models/mascota.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {

  miFormulario!: FormGroup;
  listaMascotas: Mascota[] = [];
  private contadorId = 1;
  mensajeExito = '';

  // Especies disponibles para el dropdown
  especies: string[] = ['Perro', 'Gato', 'Ave', 'Conejo', 'Hámster', 'Reptil'];

    // ===== CARRUSEL DE ESPECIES =====
  imagenesEspecies = [
    {
      nombre: 'Perro',
      url: 'https://images.pexels.com/photos/1108099/pexels-photo-1108099.jpeg?auto=compress&cs=tinysrgb&w=900'
    },
    {
      nombre: 'Gato',
      url: 'https://images.pexels.com/photos/45201/kitty-cat-kitten-pet-45201.jpeg?auto=compress&cs=tinysrgb&w=900'
    },
    {
      nombre: 'Ave',
      url: 'https://images.pexels.com/photos/1661179/pexels-photo-1661179.jpeg?auto=compress&cs=tinysrgb&w=900'
    },
    {
      nombre: 'Conejo',
      url: 'https://images.pexels.com/photos/326012/pexels-photo-326012.jpeg?auto=compress&cs=tinysrgb&w=900'
    },
    {
      nombre: 'Hámster',
      url: 'https://clinicavesal.es/wp-content/uploads/2018/06/cuidados-hamster.jpg'
    },
    {
      nombre: 'Reptil',
      url: 'https://images.pexels.com/photos/567540/pexels-photo-567540.jpeg?auto=compress&cs=tinysrgb&w=900'
    }
  ];

  indiceActual = 0;
  private intervalo?: ReturnType<typeof setInterval>;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.miFormulario = this.fb.group({
      nombre:   ['', [Validators.required, Validators.minLength(2)]],
      especie:  ['', [Validators.required]],
      edad:     [null, [Validators.required, Validators.min(0), Validators.max(25)]],
      telefono: ['', [Validators.required, Validators.pattern(/^[0-9]{9}$/)]]
    });

    // Inicia el carrusel automático cada 3 segundos
    this.iniciarCarrusel();
  }

  ngOnDestroy(): void {
    this.detenerCarrusel();
  }

  // ===== LÓGICA DEL CARRUSEL =====
  iniciarCarrusel(): void {
    this.intervalo = setInterval(() => {
      this.siguiente();
    }, 3000);
  }

  detenerCarrusel(): void {
    if (this.intervalo) {
      clearInterval(this.intervalo);
    }
  }

  siguiente(): void {
    this.indiceActual = (this.indiceActual + 1) % this.imagenesEspecies.length;
  }

  anterior(): void {
    this.indiceActual =
      (this.indiceActual - 1 + this.imagenesEspecies.length) % this.imagenesEspecies.length;
  }

  irA(index: number): void {
    this.indiceActual = index;
    this.detenerCarrusel();
    this.iniciarCarrusel();
  }

  // ===== GETTERS DEL FORMULARIO =====
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
    this.miFormulario.reset();

    setTimeout(() => (this.mensajeExito = ''), 3000);
  }

  eliminar(id: number): void {
    this.listaMascotas = this.listaMascotas.filter(m => m.id !== id);
  }

  getClaseEspecie(especie: string): string {
    switch (especie.toLowerCase()) {
      case 'perro':   return 'badge badge-perro';
      case 'gato':    return 'badge badge-gato';
      case 'ave':     return 'badge badge-ave';
      case 'conejo':  return 'badge badge-conejo';
      case 'hámster':
      case 'hamster': return 'badge badge-hamster';
      case 'reptil':  return 'badge badge-reptil';
      default:        return 'badge badge-otro';
    }
  }
}