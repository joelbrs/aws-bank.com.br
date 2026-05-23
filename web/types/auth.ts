export type CredentialType = 'PASSWORD' | 'TRANSACTION_PASSWORD'

export interface Credential {
  type: CredentialType
  value: string
}

export interface SignUpRequest {
  cpf: string
  firstName: string
  lastName: string
  email: string
  credentials: Credential[]
}

export interface ResendEmailRequest {
  email: string
}

export interface ApiErrorResponse {
  timestamp?: string
  status?: number
  message?: string
  path?: string
}

export interface ValidationErrorResponse {
  response?: ApiErrorResponse
  messages?: Array<{ field: string, message: string }>
}

export interface SignUpFormModel {
  cpf: string
  firstName: string
  lastName: string
  email: string
  password: string
  confirmPassword: string
  transactionPassword: string
  confirmTransactionPassword: string
}

export interface SignInFormModel {
  email: string
  password: string
}
