# S10 - RP - VetCare Register: Sistema de Registro Clínico de Mascotas

**Reto Práctico Individual - Formularios Reactivos (Angular)**
**Estudiante 04:** Condor Alcala, Jose Manuel

## 🎯 Descripción
SPA en Angular basada en NgModules y Reactive Forms para el registro clínico
de mascotas. Permite capturar, validar, listar y eliminar pacientes en memoria.

## 🛠️ Stack
- Angular (NgModules, **sin Standalone**)
- TypeScript
- HTML5 + CSS3
- ReactiveFormsModule

## ✅ Funcionalidades
- Formulario reactivo con `FormBuilder` y `Validators`.
- Validaciones específicas:
  - **Teléfono:** exactamente 9 dígitos numéricos (regex).
  - **Edad:** entre 0 y 25 años.
  - **Nombre:** obligatorio (mín. 2 caracteres).
  - **Especie:** selección obligatoria.
- Alertas de error contextuales (solo al `touched`).
- Botón deshabilitado con `[disabled]="miFormulario.invalid"`.
- Tabla dinámica con `*ngFor` y badges por especie.
- Eliminación individual de registros.
- Reset manual tras cada registro exitoso.

## 🚀 Instrucciones de arranque
```bash
npm install
ng serve