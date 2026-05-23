export function stripCpf(value: string): string {
  return value.replace(/\D/g, '')
}

export function formatCpf(value: string): string {
  const digits = stripCpf(value).slice(0, 11)

  return digits
    .replace(/(\d{3})(\d)/, '$1.$2')
    .replace(/(\d{3})(\d)/, '$1.$2')
    .replace(/(\d{3})(\d{1,2})$/, '$1-$2')
}

export function isValidCpf(value: string): boolean {
  const cpf = stripCpf(value)

  if (cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) {
    return false
  }

  const calcDigit = (base: string, factor: number) => {
    let total = 0

    for (let i = 0; i < base.length; i++) {
      total += Number(base[i]) * (factor - i)
    }

    const remainder = (total * 10) % 11
    return remainder === 10 ? 0 : remainder
  }

  const firstDigit = calcDigit(cpf.slice(0, 9), 10)
  const secondDigit = calcDigit(cpf.slice(0, 10), 11)

  return firstDigit === Number(cpf[9]) && secondDigit === Number(cpf[10])
}
