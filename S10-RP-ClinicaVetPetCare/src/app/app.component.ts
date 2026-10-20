import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors, ValidatorFn, AsyncValidatorFn } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { map, delay } from 'rxjs/operators';
import { Mascota } from './models/mascota.model';

// =========================================================================
// CONFIGURACIÓN DE PESOS MÁXIMOS PARA CACHORROS (Menores a 1 año)
// =========================================================================
export const PESOS_MAXIMOS_CACHORRO: { [key: string]: number } = {
  'Perro': 15,
  'Gato': 5,
  'Ave': 1,
  'Conejo': 2.5,
  'Hámster': 0.2,
  'Reptil': 3
};

// =========================================================================
// 1. VALIDADOR PERSONALIZADO SÍNCRONO (Evitar solo espacios)
// =========================================================================
export function sinEspaciosBlancos(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const esEspacio = (control.value || '').trim().length === 0;
    return esEspacio ? { sinEspacios: true } : null;
  };
}

// =========================================================================
// 2. VALIDADOR PERSONALIZADO (Obligar a seleccionar "sí")
// =========================================================================
export function debeConfirmar(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    return control.value === 'si' ? null : { debeConfirmar: true };
  };
}

// =========================================================================
// 3. VALIDADOR CRUZADO INTELIGENTE (Peso, Edad y Especie)
// =========================================================================
export function validarPesoEdad(): ValidatorFn {
  return (group: AbstractControl): ValidationErrors | null => {
    const edad = group.get('edad')?.value;
    const peso = group.get('peso')?.value;
    const especie = group.get('especie')?.value;
    
    if (edad !== null && peso !== null && especie && edad < 1) {
      const pesoMaximo = PESOS_MAXIMOS_CACHORRO[especie];
      if (pesoMaximo && peso > pesoMaximo) {
        // Devolvemos el mensaje ya armado para evitar problemas de renderizado
        return { 
          pesoIncoherente: true, 
          mensajeAlerta: `Un cachorro de ${especie.toLowerCase()} (menor a 1 año) no puede pesar más de ${pesoMaximo} kg.` 
        };
      }
    }
    return null;
  };
}

// =========================================================================
// 4. VALIDADOR ASÍNCRONO (Nombre único simulado)
// =========================================================================
export function nombreUnicoValidator(): AsyncValidatorFn {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    if (!control.value) return of(null);
    
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
          Validators.pattern('^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$'),
          sinEspaciosBlancos()
        ],
        asyncValidators: [nombreUnicoValidator()],
        updateOn: 'blur'
      }],
      especie: ['', [Validators.required]],
      edad: [null, [Validators.required, Validators.min(0), Validators.max(25)]],
      peso: [null, [Validators.required, Validators.min(0.1)]],
      telefono: ['', [Validators.required, Validators.pattern('^[0-9]{9}$')]],
      confirmarAdopcion: ['', [Validators.required, debeConfirmar()]]
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

  // Getter corregido para la alerta cruzada
  get mensajeAlertaCruzada(): string {
    const error = this.miFormulario.getError('pesoIncoherente');
    if (!error) return '';
    // Buscamos el mensaje en la raíz o dentro de 'validators' (comportamiento interno de Angular)
    const mensaje = error.mensajeAlerta || error.validators?.mensajeAlerta;
    return mensaje ? mensaje : 'Verifica los datos ingresados.';
  }

  // ===== ACTUALIZACIÓN DINÁMICA =====
  onEspecieChange(): void {
    if (this.especie.value === 'Reptil') {
      this.peso.clearValidators();
      this.peso.setValidators([Validators.min(0.1)]);
    } else {
      this.peso.setValidators([Validators.required, Validators.min(0.1)]);
    }
    this.peso.updateValueAndValidity();
    
    // Re-evaluar el formulario completo para actualizar la alerta cruzada
    this.miFormulario.updateValueAndValidity();
  }

  // ===== GUARDADO =====
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
      peso:     Number(this.miFormulario.value.peso),
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