import { Injectable } from '@angular/core';


export interface Pelicula {
  id: number;
  titulo: string;
  genero: string;
  anio: number;
  calificacion: number;
  poster: string; 
}

@Injectable({
  providedIn: 'root' // Instancia única global (Singleton)
})
export class PeliculaService {
  
  
  private peliculas: Pelicula[] = [
    { id: 1, titulo: 'The Batman', genero: 'Acción', anio: 2022, calificacion: 8.5, poster: 'https://pics.filmaffinity.com/The_Batman-301109776-large.jpg' },
    { id: 2, titulo: 'Dune: Part Two', genero: 'Ciencia Ficción', anio: 2024, calificacion: 9.0, poster: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHtBGsnxOoEQAsboEspaPoElQT-Z4wtTikQ45Hrb3x_6NvjOw607ZrPxk&s=10' },
    { id: 3, titulo: 'Oppenheimer', genero: 'Drama', anio: 2023, calificacion: 8.8, poster: 'https://m.media-amazon.com/images/M/MV5BNTFlZDI1YWQtMTVjNy00YWU1LTg2YjktMTlhYmRiYzQ3NTVhXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg' },
    { id: 4, titulo: 'Spider-Man: No Way Home', genero: 'Acción', anio: 2021, calificacion: 8.2, poster: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTlD6yBKA4v6-R6_JRD-7cAa8QOCPhGGQdHDbftW8wKO_sf26AbrWC2WRM&s=10' },
    { id: 5, titulo: 'Interstellar', genero: 'Ciencia Ficción', anio: 2014, calificacion: 9.5, poster: 'https://m.media-amazon.com/images/M/MV5BYzdjMDAxZGItMjI2My00ODA1LTlkNzItOWFjMDU5ZDJlYWY3XkEyXkFqcGc@._V1_.jpg' },
    { id: 6, titulo: 'The Godfather', genero: 'Drama', anio: 1972, calificacion: 9.8, poster: 'https://m.media-amazon.com/images/M/MV5BNGEwYjgwOGQtYjg5ZS00Njc1LTk2ZGEtM2QwZWQ2NjdhZTE5XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg' }
  ];

  
  obtenerPeliculas(): Pelicula[] {
    return this.peliculas;
  }
}