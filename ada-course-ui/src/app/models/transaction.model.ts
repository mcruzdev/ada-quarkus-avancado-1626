export interface Transaction {
  id: number;
  fromUserId: number | null;
  fromUserName: string;
  toUserId: number;
  toUserName: string;
  amount: number;
  type: string;
  description: string | null;
  transactionDate: string;
}

export interface TransferRequest {
  fromUserId: number;
  toUserId: number;
  amount: number;
  description?: string;
}

// Made with Bob
