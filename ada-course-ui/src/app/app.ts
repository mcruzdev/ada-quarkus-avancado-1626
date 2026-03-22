import { Component, signal, inject } from '@angular/core';
import { RouterOutlet, RouterModule } from '@angular/router';
import { ThemeService } from './services/theme.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly themeService = inject(ThemeService);
  protected readonly title = signal('ada-course-ui');
}
