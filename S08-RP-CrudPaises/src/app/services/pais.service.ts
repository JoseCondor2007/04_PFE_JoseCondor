import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PaisService {
  private apiUrl = 'https://6ab68b20c4c7bb67b918ee9e.mockapi.io/paises';

  constructor(private http: HttpClient) { }

  // GET
  obtenerPaises(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  // POST: nombre "registrarPais" — pero la URL es solo `apiUrl`
  registrarPais(pais: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, pais);
  }

  // PUT: nombre "actualizarPais" — URL `apiUrl/{id}`
  actualizarPais(id: number | string, pais: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, pais);
  }

  // DELETE: nombre "eliminarPais" — URL `apiUrl/{id}`
  eliminarPais(id: number | string): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`);
  }
}