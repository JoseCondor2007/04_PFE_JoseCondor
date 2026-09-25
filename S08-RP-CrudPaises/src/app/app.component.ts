import { Component } from '@angular/core';
import { PaisListaComponent } from './components/pais-lista/pais-lista.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [PaisListaComponent],  
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'S08_RP_CrudPaises';
}