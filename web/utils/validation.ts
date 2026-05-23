import { isValidCpf, stripCpf } from './cpf'

function isSequential(value: string): boolean {
  let isIncreasing = true
  let isDecreasing = true

  for (let i = 1; i < value.length; i++) {
    if (value.charAt(i) !== String.fromCharCode(value.charCodeAt(i - 1) + 1)) {
      isIncreasing = false
    }
    if (value.charAt(i) !== String.fromCharCode(value.charCodeAt(i - 1) - 1)) {
      isDecreasing = false
    }
  }

  return isIncreasing || isDecreasing
}

function isCredentialValid(value: string, cpf: string, pattern: RegExp): boolean {
  if (!pattern.test(value)) {
    return false
  }

  const normalizedCpf = stripCpf(cpf)

  if (normalizedCpf && value.includes(normalizedCpf)) {
    return false
  }

  return !isSequential(value)
}

export function validatePassword(value: string, cpf: string): string | null {
  if (!isCredentialValid(value, cpf, /^\d{8}$/)) {
    return 'A senha deve ter exatamente 8 dígitos numéricos, sem sequências e sem conter o CPF.'
  }

  return null
}

export function validateTransactionPassword(value: string, cpf: string): string | null {
  if (!isCredentialValid(value, cpf, /^\d{6}$/)) {
    return 'A senha transacional deve ter exatamente 6 dígitos numéricos, sem sequências e sem conter o CPF.'
  }

  return null
}

export function validateEmail(value: string): string | null {
  if (!value.trim()) {
    return 'Informe o e-mail.'
  }

  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
    return 'Informe um e-mail válido.'
  }

  return null
}

export function validateRequired(value: string, label: string): string | null {
  if (!value.trim()) {
    return `Informe ${label}.`
  }

  return null
}

export function validateCpf(value: string): string | null {
  if (!value.trim()) {
    return 'Informe o CPF.'
  }

  if (!isValidCpf(value)) {
    return 'CPF inválido.'
  }

  return null
}

export function validatePasswordConfirmation(password: string, confirmation: string, label = 'senha'): string | null {
  if (password !== confirmation) {
    return `A confirmação da ${label} não confere.`
  }

  return null
}
