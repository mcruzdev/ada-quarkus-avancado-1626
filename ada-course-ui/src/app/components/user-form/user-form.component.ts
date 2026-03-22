import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.css'
})
export class UserFormComponent {
  private fb = inject(FormBuilder);
  private userService = inject(UserService);
  private router = inject(Router);

  userForm: FormGroup;
  isSubmitting = false;
  errorMessage = '';
  successMessage = '';

  constructor() {
    this.userForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      // password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.userForm.valid && !this.isSubmitting) {
      this.isSubmitting = true;
      this.errorMessage = '';
      this.successMessage = '';

      this.userService.createUser(this.userForm.value).subscribe({
        next: (user) => {
          this.successMessage = `Usuário ${user.name} criado com sucesso! Saldo inicial: ${user.courseCoins} Quarkus Coins`;
          this.userForm.reset();
          this.isSubmitting = false;
          
          // Redirecionar após 2 segundos
          setTimeout(() => {
            this.router.navigate(['/transfer']);
          }, 2000);
        },
        error: (error) => {
          this.isSubmitting = false;
          if (error.status === 409) {
            this.errorMessage = 'Este email já está cadastrado.';
          } else {
            this.errorMessage = 'Erro ao criar usuário. Tente novamente.';
          }
          console.error('Error creating user:', error);
        }
      });
    }
  }

  get name() {
    return this.userForm.get('name');
  }

  get email() {
    return this.userForm.get('email');
  }

  // get password() {
  //   return this.userForm.get('password');
  // }
}

// Made with Bob
