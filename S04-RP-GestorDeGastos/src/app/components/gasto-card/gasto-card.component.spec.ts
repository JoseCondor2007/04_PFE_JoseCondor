import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GastoCardComponent } from './gasto-card.component';

describe('GastoCardComponent', () => {
  let component: GastoCardComponent;
  let fixture: ComponentFixture<GastoCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GastoCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GastoCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
