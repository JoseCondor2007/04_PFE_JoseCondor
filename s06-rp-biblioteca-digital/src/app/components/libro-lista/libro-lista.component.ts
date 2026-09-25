import { Component, inject, OnInit } from '@angular/core';
import { LibroService } from '../../services/libro.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-libro-lista',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './libro-lista.component.html',
  styleUrl: './libro-lista.component.css'
})
export class LibroListaComponent implements OnInit {

  private libroService = inject(LibroService);

  libros: any[] = [];
  cargando = false;
  error = '';

  // Campos del formulario
  titulo = '';
  autor = '';
  anio: number | null = null;
  genero = '';
  imagen = '';

  ngOnInit() {
    this.cargarLibros();
  }

  cargarLibros() {
    this.cargando = true;
    this.error = '';
    console.log('🚀 Iniciando carga de libros...');

    this.libroService.obtenerLibros().subscribe({
      next: (respuesta: any) => {
        console.log('✅ Respuesta recibida:', respuesta);

        if (!respuesta || !respuesta.docs) {
          console.error('❌ La respuesta no tiene la propiedad "docs"');
          this.error = 'La respuesta de la API no tiene el formato esperado.';
          this.cargando = false;
          return;
        }

        console.log(`📖 Libros encontrados: ${respuesta.docs.length}`);

        this.libros = respuesta.docs
          .filter((doc: any) => doc.title && doc.cover_i)
          .map((doc: any) => ({
            title: doc.title,
            author: doc.author_name ? doc.author_name[0] : 'Autor desconocido',
            year: doc.first_publish_year || 'Año desconocido',
            genre: this.obtenerGenero(doc), // <-- Género traducido
            cover: `https://covers.openlibrary.org/b/id/${doc.cover_i}-M.jpg`
          }));

        console.log('📚 Libros mapeados:', this.libros);
        this.cargando = false;
      },
      error: (err) => {
        console.error('❌ Error en la petición HTTP:', err);
        this.error = 'Error al cargar los libros. Verifica tu conexión.';
        this.cargando = false;
      }
    });
  }

  /**
   * Extrae y traduce el género del libro a partir del campo "subject" de la API.
   */
  obtenerGenero(doc: any): string {
    // Si no tiene subject, devolver "No especificado"
    if (!doc.subject || doc.subject.length === 0) {
      return 'No especificado';
    }

    // Tomar el primer subject
    const subject = doc.subject[0].toLowerCase();

    // Mapeo de géneros comunes al español
    const traducciones: { [key: string]: string } = {
      'fiction': 'Ficción',
      'spanish fiction': 'Ficción española',
      'children\'s fiction': 'Infantil',
      'juvenile fiction': 'Juvenil',
      'adventure': 'Aventura',
      'history': 'Historia',
      'poetry': 'Poesía',
      'drama': 'Drama',
      'romance': 'Romance',
      'fantasy': 'Fantasía',
      'science fiction': 'Ciencia Ficción',
      'mystery': 'Misterio',
      'horror': 'Terror',
      'biography': 'Biografía',
      'essays': 'Ensayos',
      'literature': 'Literatura',
      'spanish literature': 'Literatura española',
      'classic': 'Clásico',
      'classic literature': 'Literatura clásica'
    };

    return traducciones[subject] || doc.subject[0];
  }

  registrarLibro() {
    // Validar que todos los campos estén completos
    if (!this.titulo || !this.autor || !this.anio || !this.genero || !this.imagen) {
      alert('Completa todos los campos, incluyendo la URL de la imagen');
      return;
    }

    const nuevoLibro = {
      title: this.titulo,
      author: this.autor,
      year: this.anio,
      genre: this.genero,
      cover: this.imagen
    };

    this.libroService.registrarLibro(nuevoLibro).subscribe({
      next: (respuesta: any) => {
        console.log('✅ Libro registrado:', respuesta);

        // Agregar el libro a la lista local con todos los datos
        this.libros.unshift({
          title: this.titulo,
          author: this.autor,
          year: this.anio,
          genre: this.genero, // <-- Incluir género
          cover: this.imagen
        });

        // Limpiar formulario
        this.titulo = '';
        this.autor = '';
        this.anio = null;
        this.genero = '';
        this.imagen = '';
      },
      error: (err) => {
        console.error('❌ Error al registrar:', err);
        alert('Error al registrar el libro. Intenta de nuevo.');
      }
    });
  }
}