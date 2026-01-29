import { Component } from '@angular/core';
import { GlassmorphismLoginComponent } from './glassmorphism-login.component';

@Component({
  selector: 'app-example-usage',
  standalone: true,
  imports: [GlassmorphismLoginComponent],
  template: `
    <div class="example-container">
      <h1>Component Usage Example</h1>
      <app-glassmorphism-login></app-glassmorphism-login>
    </div>
  `,
  styles: [`
    .example-container {
      min-height: 100vh;
    }
    h1 {
      position: absolute;
      top: 20px;
      left: 20px;
      color: white;
      z-index: 10;
    }
  `]
})
export class ExampleUsageComponent {
  constructor() { }
}
