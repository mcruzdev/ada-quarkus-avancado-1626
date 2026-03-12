import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { TransactionService } from '../../services/transaction.service';
import { User } from '../../models/user.model';
import { Transaction } from '../../models/transaction.model';

@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './transfer.component.html',
  styleUrl: './transfer.component.css'
})
export class TransferComponent implements OnInit {
  private fb = inject(FormBuilder);
  private userService = inject(UserService);
  private transactionService = inject(TransactionService);

  transferForm: FormGroup;
  users: User[] = [];
  transactions: Transaction[] = [];
  isSubmitting = false;
  errorMessage = '';
  successMessage = '';
  isLoadingUsers = true;
  isLoadingTransactions = true;

  constructor() {
    this.transferForm = this.fb.group({
      fromUserId: ['', Validators.required],
      toUserId: ['', Validators.required],
      amount: ['', [Validators.required, Validators.min(0.01)]],
      description: ['']
    });
  }

  ngOnInit(): void {
    this.loadUsers();
    this.loadTransactions();
  }

  loadUsers(): void {
    this.isLoadingUsers = true;
    this.userService.getAllUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.isLoadingUsers = false;
      },
      error: (error) => {
        console.error('Error loading users:', error);
        this.errorMessage = 'Erro ao carregar usuários';
        this.isLoadingUsers = false;
      }
    });
  }

  loadTransactions(): void {
    this.isLoadingTransactions = true;
    this.transactionService.getAllTransactions().subscribe({
      next: (transactions) => {
        this.transactions = transactions;
        this.isLoadingTransactions = false;
      },
      error: (error) => {
        console.error('Error loading transactions:', error);
        this.isLoadingTransactions = false;
      }
    });
  }

  onSubmit(): void {
    if (this.transferForm.valid && !this.isSubmitting) {
      this.isSubmitting = true;
      this.errorMessage = '';
      this.successMessage = '';

      const request = {
        ...this.transferForm.value,
        amount: parseFloat(this.transferForm.value.amount)
      };

      this.transactionService.transfer(request).subscribe({
        next: (transaction) => {
          this.successMessage = `Transferência de ${transaction.amount} Quarkus Coins realizada com sucesso!`;
          this.transferForm.reset();
          this.isSubmitting = false;
          
          // Recarregar dados
          this.loadUsers();
          this.loadTransactions();

          // Limpar mensagem de sucesso após 5 segundos
          setTimeout(() => {
            this.successMessage = '';
          }, 5000);
        },
        error: (error) => {
          this.isSubmitting = false;
          if (error.error?.error) {
            this.errorMessage = error.error.error;
          } else {
            this.errorMessage = 'Erro ao realizar transferência. Tente novamente.';
          }
          console.error('Error transferring:', error);
        }
      });
    }
  }

  getUserById(id: number): User | undefined {
    return this.users.find(u => u.id === id);
  }

  getFromUserBalance(): number {
    const fromUserId = this.transferForm.get('fromUserId')?.value;
    if (fromUserId) {
      const user = this.getUserById(parseInt(fromUserId));
      return user?.courseCoins || 0;
    }
    return 0;
  }

  get fromUserId() {
    return this.transferForm.get('fromUserId');
  }

  get toUserId() {
    return this.transferForm.get('toUserId');
  }

  get amount() {
    return this.transferForm.get('amount');
  }
}

// Made with Bob
