export interface User {
  id: number;
  name: string;
  email: string;
  courseCoins: number;
  createdAt: string;
}

export interface UserRequest {
  name: string;
  email: string;
  password: string;
}

// Made with Bob
