import { Injectable } from '@angular/core';
import { Pelicula } from '../models/pelicula.model';

@Injectable({
  providedIn: 'root'
})
export class PeliculaService {
  private peliculas: Pelicula[] = [
    { 
      id: 1, 
      titulo: 'Origen', 
      director: 'Christopher Nolan', 
      anio: 2010, 
      genero: 'Ciencia Ficción', 
      calificacion: 8.8,
      imagen: 'https://blogculturalia.net/wp-content/uploads/2010/09/origen-inception.jpg'
    },
    { 
      id: 2, 
      titulo: 'El Padrino', 
      director: 'Francis Ford Coppola', 
      anio: 1972, 
      genero: 'Drama', 
      calificacion: 9.2,
      imagen: 'https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg'
    },
    { 
      id: 3, 
      titulo: 'El Caballero de la Noche', 
      director: 'Christopher Nolan', 
      anio: 2008, 
      genero: 'Acción', 
      calificacion: 9.0,
      imagen: 'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg'
    },
    { 
      id: 4, 
      titulo: 'Tiempos Violentos', 
      director: 'Quentin Tarantino', 
      anio: 1994, 
      genero: 'Crimen', 
      calificacion: 8.9,
      imagen: 'https://m.media-amazon.com/images/M/MV5BNDZiZDU2MjMtM2Q3ZS00NDY0LWExOWEtZjYzYzE3ZTRkODFlXkEyXkFqcGc@._V1_.jpg'
    },
    { 
      id: 5, 
      titulo: 'Matrix', 
      director: 'Lana Wachowski', 
      anio: 1999, 
      genero: 'Ciencia Ficción', 
      calificacion: 8.7,
      imagen: 'https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg'
    },
    { 
      id: 6, 
      titulo: 'Forrest Gump', 
      director: 'Robert Zemeckis', 
      anio: 1994, 
      genero: 'Drama', 
      calificacion: 8.8,
      imagen: 'https://image.tmdb.org/t/p/w500/arw2vcBveWOVZr6pxd9XTd1TdQa.jpg'
    },
    { 
      id: 7, 
      titulo: 'Sueños de Libertad', 
      director: 'Frank Darabont', 
      anio: 1994, 
      genero: 'Drama', 
      calificacion: 9.3,
      imagen: 'https://static.wikia.nocookie.net/stephenking/images/b/b3/Shawshank_Redemption_Poster.jpg/revision/latest?cb=20260302235353&path-prefix=es'
    },
    { 
      id: 8, 
      titulo: 'El Club de la Lucha', 
      director: 'David Fincher', 
      anio: 1999, 
      genero: 'Drama', 
      calificacion: 8.8,
      imagen: 'https://image.tmdb.org/t/p/w500/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg'
    },
    { 
      id: 9, 
      titulo: 'Interestelar', 
      director: 'Christopher Nolan', 
      anio: 2014, 
      genero: 'Ciencia Ficción', 
      calificacion: 8.6,
      imagen: 'https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg'
    },
    { 
      id: 10, 
      titulo: 'El Señor de los Anillos: La Comunidad del Anillo', 
      director: 'Peter Jackson', 
      anio: 2001, 
      genero: 'Fantasía', 
      calificacion: 8.8,
      imagen: 'https://image.tmdb.org/t/p/w500/6oom5QYQ2yQTMJIbnvbkBL9cHo6.jpg'
    },
    { 
      id: 11, 
      titulo: 'Uno de los Nuestros', 
      director: 'Martin Scorsese', 
      anio: 1990, 
      genero: 'Crimen', 
      calificacion: 8.7,
      imagen: 'https://pics.filmaffinity.com/Uno_de_los_nuestros-152126089-large.jpg'
    },
    { 
      id: 12, 
      titulo: 'El Imperio Contraataca', 
      director: 'Irvin Kershner', 
      anio: 1980, 
      genero: 'Ciencia Ficción', 
      calificacion: 8.7,
      imagen: 'https://hips.hearstapps.com/hmg-prod/images/el-imperio-contraataca-1533208399.jpg?crop=1xw:1xh;center,top&resize=980:*'
    },
    { 
      id: 13, 
      titulo: 'El Silencio de los Inocentes', 
      director: 'Jonathan Demme', 
      anio: 1991, 
      genero: 'Thriller', 
      calificacion: 8.6,
      imagen: 'https://mx.web.img3.acsta.net/pictures/17/08/15/03/36/174874.jpg'
    },
    { 
      id: 14, 
      titulo: 'Salvar al Soldado Ryan', 
      director: 'Steven Spielberg', 
      anio: 1998, 
      genero: 'Bélica', 
      calificacion: 8.6,
      imagen: 'https://es.web.img2.acsta.net/r_1280_720/pictures/14/03/05/09/42/163621.jpg'
    },
    { 
      id: 15, 
      titulo: 'La Milla Verde', 
      director: 'Frank Darabont', 
      anio: 1999, 
      genero: 'Drama', 
      calificacion: 8.6,
      imagen: 'https://image.tmdb.org/t/p/w500/8VG8fDNiy50H4FedGwdSVUPoaJe.jpg'
    },
    { 
      id: 16, 
      titulo: 'El Gran Truco', 
      director: 'Christopher Nolan', 
      anio: 2006, 
      genero: 'Misterio', 
      calificacion: 8.5,
      imagen: 'https://image.tmdb.org/t/p/w500/tRNlZbgNCNOpLpbPEz5L8G8A0JN.jpg'
    },
    { 
      id: 17, 
      titulo: 'Gladiador', 
      director: 'Ridley Scott', 
      anio: 2000, 
      genero: 'Acción', 
      calificacion: 8.5,
      imagen: 'https://image.tmdb.org/t/p/w500/ty8TGRuvJLPUmAR1H1nRIsgwvim.jpg'
    },
    { 
      id: 18, 
      titulo: 'Los Infiltrados', 
      director: 'Martin Scorsese', 
      anio: 2006, 
      genero: 'Crimen', 
      calificacion: 8.5,
      imagen: 'https://m.media-amazon.com/images/M/MV5BNmVhOTZmOTEtM2Y3Ni00MGZiLWFmNzAtMWJmMzhkZWVmYWJhXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg'
    },
    { 
      id: 19, 
      titulo: 'Whiplash', 
      director: 'Damien Chazelle', 
      anio: 2014, 
      genero: 'Drama', 
      calificacion: 8.5,
      imagen: 'https://image.tmdb.org/t/p/w500/7fn624j5lj3xTme2SgiLCeuedmO.jpg'
    },
    { 
      id: 20, 
      titulo: 'Parásitos', 
      director: 'Bong Joon-ho', 
      anio: 2019, 
      genero: 'Thriller', 
      calificacion: 8.5,
      imagen: 'https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg'
    }
  ];

  private nextId: number = 21;

  getPeliculas(): Pelicula[] {
    return [...this.peliculas];
  }

  getPeliculaById(id: number): Pelicula | undefined {
    return this.peliculas.find(p => p.id === id);
  }

  addPelicula(pelicula: Omit<Pelicula, 'id'>): void {
    const nuevaPelicula: Pelicula = {
      ...pelicula,
      id: this.nextId++,
      // Si no se proporciona imagen, se usa una imagen por defecto
      imagen: pelicula.imagen || 'https://image.tmdb.org/t/p/w500/1E5baAaEse26fej7uHcjOgEE2t2.jpg'
    };
    this.peliculas.push(nuevaPelicula);
  }

  updatePelicula(id: number, peliculaActualizada: Omit<Pelicula, 'id'>): void {
    const index = this.peliculas.findIndex(p => p.id === id);
    if (index !== -1) {
      this.peliculas[index] = { 
        ...peliculaActualizada, 
        id,
        // Mantener la imagen existente si no se proporciona una nueva
        imagen: peliculaActualizada.imagen || this.peliculas[index].imagen
      };
    }
  }

  deletePelicula(id: number): void {
    this.peliculas = this.peliculas.filter(p => p.id !== id);
  }
}