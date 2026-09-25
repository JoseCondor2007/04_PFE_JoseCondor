import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LibroService {

  private http = inject(HttpClient);

  // URL de la API de Open Library para buscar "Don Quijote"
  private apiUrl = 'https://openlibrary.org/search.json?q=don+quijote';

  // MÉTODO GET: Consultar libros
  obtenerLibros() {
    // La API devuelve un objeto con una propiedad 'docs' que contiene el array de libros
    return this.http.get<any>(this.apiUrl);
  }

  // MÉTODO POST: Registrar un libro .
  registrarLibro(libro: any) {
    return this.http.post<any>('https://jsonplaceholder.typicode.com/posts', libro);
  }
}