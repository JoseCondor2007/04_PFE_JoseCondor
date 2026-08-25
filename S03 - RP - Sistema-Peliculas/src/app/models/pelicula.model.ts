// src/app/models/pelicula.model.ts
export interface Pelicula {
  id: number;
  titulo: string;
  director: string;
  anio: number;
  genero: string;
  calificacion: number;
  imagen?: string; 
}