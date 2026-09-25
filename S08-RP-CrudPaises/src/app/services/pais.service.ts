import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PaisService {
  // URL base de la API
  private apiUrl = 'https://6ab68b20c4c7bb67b918ee9e.mockapi.io/paises';

  constructor(private http: HttpClient) { }

  // GET: Obtener todos los países
  obtenerPaises(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  // POST: Registrar un nuevo país
  registrarPais(pais: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/save`, pais);
  }

  // PUT: Actualizar un país existente
  actualizarPais(id: number | string, pais: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/update/${id}`, pais);
  }

  // DELETE: Eliminar un país
  eliminarPais(id: number | string): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/delete/${id}`);
  }
}