import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Gasto } from '../../app.component';

@Component({
  selector: 'app-gasto-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './gasto-card.component.html',
  styleUrls: ['./gasto-card.component.css']
})
export class GastoCardComponent {
  @Input() gasto!: Gasto;
  @Output() eliminar = new EventEmitter<number>();

  // ===== MENSAJE DINÁMICO (M) =====
  get esGastoElevado(): boolean {
    return this.gasto.monto > 100;
  }

  get mensajeGasto(): string {
    return this.esGastoElevado ? '⚠️ Gasto elevado' : '✅ Gasto controlado';
  }

  get claseGasto(): string {
    return this.esGastoElevado ? 'gasto-elevado' : 'gasto-controlado';
  }
}