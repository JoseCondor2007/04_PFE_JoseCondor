import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms'; // ← Módulo clave
import { CommonModule } from '@angular/common';

import { AppComponent } from './app.component';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    CommonModule,
    ReactiveFormsModule // Habilita formGroup, formControlName, ngSubmit
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}