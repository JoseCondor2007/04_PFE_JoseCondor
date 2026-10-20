import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors, ValidatorFn, AsyncValidatorFn } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { map, delay } from 'rxjs/operators';
import { Mascota } from './models/mascota.model';

// =========================================================================
// 1. VALIDADOR PERSONALIZADO SÍNCRONO
// Evita que el campo contenga solo espacios en blanco
// =========================================================================
export function sinEspaciosBlancos(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const esEspacio = (control.value || '').trim().length === 0;
    return esEspacio ? { sinEspacios: true } : null;
  };
}

// =========================================================================
// 2. VALIDADOR CRUZADO (Cross-field)
// Se aplica a nivel de FormGroup para comparar campos hermanos
// =========================================================================
export function validarPesoEdad(): ValidatorFn {
  return (group: AbstractControl): ValidationErrors | null => {
    const edad = group.get('edad')?.value;
    const peso = group.get('peso')?.value;
    
    // Regla de negocio: Si es menor a 1 año, el peso no debería superar los 10kg
    if (edad !== null && peso !== null && edad < 1 && peso > 10) {
      return { pesoIncoherente: true };
    }
    return null;
  };
}

// =========================================================================
// 3. VALIDADOR ASÍNCRONO
// Simula una consulta a una API para verificar si el nombre ya existe
// =========================================================================
export function nombreUnicoValidator(): AsyncValidatorFn {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    if (!control.value) return of(null);
    
    // Simulamos latencia de red de 800ms
    return of(control.value).pipe(
      delay(800),
      map((nombre: string) => {
        const nombresRegistrados = ['firulais', 'michi', 'rocky'];
        const existe = nombresRegistrados.includes(nombre.toLowerCase().trim());
        return existe ? { nombreOcupado: true } : null;
      })
    );
  };
}

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

  especies: string[] = ['Perro', 'Gato', 'Ave', 'Conejo', 'Hámster', 'Reptil'];

  imagenesEspecies = [
    { nombre: 'Perro', url: 'https://images.pexels.com/photos/1108099/pexels-photo-1108099.jpeg?auto=compress&cs=tinysrgb&w=900' },
    { nombre: 'Gato', url: 'https://images.pexels.com/photos/45201/kitty-cat-kitten-pet-45201.jpeg?auto=compress&cs=tinysrgb&w=900' },
    { nombre: 'Ave', url: 'https://images.pexels.com/photos/1661179/pexels-photo-1661179.jpeg?auto=compress&cs=tinysrgb&w=900' },
    { nombre: 'Conejo', url: 'https://images.pexels.com/photos/326012/pexels-photo-326012.jpeg?auto=compress&cs=tinysrgb&w=900' },
    { nombre: 'Hámster', url: 'https://clinicavesal.es/wp-content/uploads/2018/06/cuidados-hamster.jpg' },
    { nombre: 'Reptil', url: 'https://images.pexels.com/photos/567540/pexels-photo-567540.jpeg?auto=compress&cs=tinysrgb&w=900' }
  ];

  indiceActual = 0;
  private intervalo?: ReturnType<typeof setInterval>;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.miFormulario = this.fb.group({
      nombre: ['', {
        validators: [
          Validators.required, 
          Validators.minLength(3), 
          Validators.maxLength(50),
          Validators.pattern('^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$'), // Solo letras
          sinEspaciosBlancos()
        ],
        asyncValidators: [nombreUnicoValidator()],
        updateOn: 'blur' // Se ejecuta al perder el foco
      }],
      especie: ['', [Validators.required]],
      edad: [null, [Validators.required, Validators.min(0), Validators.max(25)]],
      peso: [null, [Validators.required, Validators.min(0.1)]],
      telefono: ['', [Validators.required, Validators.pattern('^[0-9]{9}$')]],
      confirmarAdopcion: ['', [Validators.required]]
    }, { 
      validators: [validarPesoEdad()] 
    });

    this.iniciarCarrusel();
  }

  ngOnDestroy(): void {
    this.detenerCarrusel();
  }

  // ===== LÓGICA DEL CARRUSEL =====
  iniciarCarrusel(): void {
    this.intervalo = setInterval(() => this.siguiente(), 3000);
  }
  detenerCarrusel(): void {
    if (this.intervalo) clearInterval(this.intervalo);
  }
  siguiente(): void {
    this.indiceActual = (this.indiceActual + 1) % this.imagenesEspecies.length;
  }
  anterior(): void {
    this.indiceActual = (this.indiceActual - 1 + this.imagenesEspecies.length) % this.imagenesEspecies.length;
  }
  irA(index: number): void {
    this.indiceActual = index;
    this.detenerCarrusel();
    this.iniciarCarrusel();
  }

  // ===== GETTERS =====
  get nombre()   { return this.miFormulario.get('nombre')!; }
  get especie()  { return this.miFormulario.get('especie')!; }
  get edad()     { return this.miFormulario.get('edad')!; }
  get peso()     { return this.miFormulario.get('peso')!; }
  get telefono() { return this.miFormulario.get('telefono')!; }
  get confirmarAdopcion() { return this.miFormulario.get('confirmarAdopcion')!; }

  // ===== ACTUALIZACIÓN DINÁMICA DE VALIDADORES =====
  onEspecieChange(): void {
    if (this.especie.value === 'Reptil') {
      this.peso.clearValidators();
      this.peso.setValidators([Validators.min(0.1)]);
    } else {
      this.peso.setValidators([Validators.required, Validators.min(0.1)]);
    }
    this.peso.updateValueAndValidity(); // CRÍTICO para aplicar cambios
  }

  // ===== GUARDADO =====
  guardar(): void {
    if (this.miFormulario.invalid) {
      this.miFormulario.markAllAsTouched(); // Fuerza la visualización de errores
      return;
    }

    const nuevaMascota: Mascota = {
      id: this.contadorId++,
      nombre:   this.miFormulario.value.nombre,
      especie:  this.miFormulario.value.especie,
      edad:     Number(this.miFormulario.value.edad),
      peso:     Number(this.miFormulario.value.peso),
      telefono: this.miFormulario.value.telefono,
      fechaRegistro: new Date()
    };

    this.listaMascotas.push(nuevaMascota);
    this.mensajeExito = `✅ Paciente "${nuevaMascota.nombre}" registrado correctamente.`;
    this.miFormulario.reset(); // Devuelve a estado pristine y limpia campos

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