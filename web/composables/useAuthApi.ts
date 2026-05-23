import type { ResendEmailRequest, SignUpRequest, ValidationErrorResponse } from '~/types/auth'

function getApiErrorMessage(error: unknown): string {
  const data = (error as { data?: ValidationErrorResponse })?.data

  if (data?.messages?.length) {
    return data.messages.map(item => item.message).join(' ')
  }

  if (data?.response?.message) {
    return data.response.message
  }

  return 'Não foi possível concluir a operação. Tente novamente.'
}

export function useAuthApi() {
  const config = useRuntimeConfig()
  const apiBase = config.public.apiBaseUrl as string

  async function signUp(payload: SignUpRequest): Promise<void> {
    await $fetch(`${apiBase}/authentication/signup`, {
      method: 'POST',
      body: payload,
    })
  }

  async function resendEmailVerification(payload: ResendEmailRequest): Promise<void> {
    await $fetch(`${apiBase}/authentication/resend-email-verification`, {
      method: 'POST',
      body: payload,
    })
  }

  return {
    signUp,
    resendEmailVerification,
    getApiErrorMessage,
  }
}
