<script setup lang="ts">
import type { SignUpFormModel } from '~/types/auth'
import { formatCpf, stripCpf } from '~/utils/cpf'
import {
  validateCpf,
  validateEmail,
  validatePassword,
  validatePasswordConfirmation,
  validateRequired,
  validateTransactionPassword,
} from '~/utils/validation'

const emit = defineEmits<{
  switch: []
  success: [email: string]
}>()

const { signUp, getApiErrorMessage } = useAuthApi()
const toast = useToast()

const step = ref(1)
const loading = ref(false)
const showSuccessDialog = ref(false)

const form = reactive<SignUpFormModel>({
  cpf: '',
  firstName: '',
  lastName: '',
  email: '',
  password: '',
  confirmPassword: '',
  transactionPassword: '',
  confirmTransactionPassword: '',
})

const errors = reactive<Record<keyof SignUpFormModel, string>>({
  cpf: '',
  firstName: '',
  lastName: '',
  email: '',
  password: '',
  confirmPassword: '',
  transactionPassword: '',
  confirmTransactionPassword: '',
})

const stepLabels = ['Dados pessoais', 'Senha de acesso', 'Confirmação']

function onCpfUpdate(value: string | undefined) {
  form.cpf = formatCpf(value ?? '')
}

function deriveTransactionPassword(): string {
  return form.password.slice(0, 6)
}

function validateStepOne(): boolean {
  errors.cpf = validateCpf(form.cpf) ?? ''
  errors.firstName = validateRequired(form.firstName, 'o primeiro nome') ?? ''
  errors.lastName = validateRequired(form.lastName, 'o sobrenome') ?? ''
  errors.email = validateEmail(form.email) ?? ''

  return !errors.cpf && !errors.firstName && !errors.lastName && !errors.email
}

function validateStepTwo(): boolean {
  errors.password = validatePassword(form.password, form.cpf) ?? ''
  errors.confirmPassword = validatePasswordConfirmation(form.password, form.confirmPassword) ?? ''

  const transactionPassword = deriveTransactionPassword()
  errors.transactionPassword = validateTransactionPassword(transactionPassword, form.cpf) ?? ''

  if (!errors.password && !errors.confirmPassword && errors.transactionPassword) {
    errors.password = 'Escolha outra senha: os 6 primeiros dígitos não atendem aos critérios de senha transacional.'
  }

  return !errors.password && !errors.confirmPassword && !errors.transactionPassword
}

function goToStep(nextStep: number) {
  step.value = nextStep
}

function handleNextStep() {
  if (step.value === 1 && validateStepOne()) {
    goToStep(2)
  }
}

function handleBackStep() {
  if (step.value > 1) {
    goToStep(step.value - 1)
  }
}

async function handleSubmit() {
  if (!validateStepTwo()) {
    return
  }

  loading.value = true

  try {
    await signUp({
      cpf: stripCpf(form.cpf),
      firstName: form.firstName.trim(),
      lastName: form.lastName.trim(),
      email: form.email.trim(),
      credentials: [
        { type: 'PASSWORD', value: form.password },
        { type: 'TRANSACTION_PASSWORD', value: deriveTransactionPassword() },
      ],
    })

    showSuccessDialog.value = true
    step.value = 3
    emit('success', form.email.trim())
  }
  catch (error) {
    toast.add({
      severity: 'error',
      summary: 'Não foi possível criar a conta',
      detail: getApiErrorMessage(error),
      life: 6000,
    })
  }
  finally {
    loading.value = false
  }
}

function handleDialogClose() {
  showSuccessDialog.value = false
  emit('switch')
}
</script>

<template>
  <div class="signup-wizard">
    <header class="signup-wizard__header">
      <h2>Crie sua conta</h2>
      <p>{{ step === 1 ? 'Informe seus dados para começar.' : 'Defina sua senha de acesso.' }}</p>
    </header>

    <div class="signup-wizard__progress" aria-label="Progresso do cadastro">
      <div
        v-for="(label, index) in stepLabels"
        :key="label"
        class="signup-wizard__step"
        :class="{
          'signup-wizard__step--active': step === index + 1,
          'signup-wizard__step--done': step > index + 1,
        }"
      >
        <span class="signup-wizard__step-dot">{{ index + 1 }}</span>
        <span class="signup-wizard__step-label">{{ label }}</span>
      </div>
    </div>

    <Transition name="auth-slide" mode="out-in">
      <form
        v-if="step === 1"
        key="step-1"
        class="signup-wizard__form"
        @submit.prevent="handleNextStep"
      >
        <div class="field">
          <label for="signup-cpf">CPF</label>
          <InputText
            id="signup-cpf"
            :model-value="form.cpf"
            placeholder="000.000.000-00"
            inputmode="numeric"
            autocomplete="off"
            :invalid="!!errors.cpf"
            fluid
            @update:model-value="onCpfUpdate"
          />
          <small v-if="errors.cpf" class="field-error">{{ errors.cpf }}</small>
        </div>

        <div class="field-grid">
          <div class="field">
            <label for="signup-first-name">Primeiro nome</label>
            <InputText
              id="signup-first-name"
              v-model="form.firstName"
              placeholder="João"
              autocomplete="given-name"
              :invalid="!!errors.firstName"
              fluid
            />
            <small v-if="errors.firstName" class="field-error">{{ errors.firstName }}</small>
          </div>

          <div class="field">
            <label for="signup-last-name">Sobrenome</label>
            <InputText
              id="signup-last-name"
              v-model="form.lastName"
              placeholder="Silva"
              autocomplete="family-name"
              :invalid="!!errors.lastName"
              fluid
            />
            <small v-if="errors.lastName" class="field-error">{{ errors.lastName }}</small>
          </div>
        </div>

        <div class="field">
          <label for="signup-email">E-mail</label>
          <InputText
            id="signup-email"
            v-model="form.email"
            type="email"
            placeholder="seu@email.com"
            autocomplete="email"
            :invalid="!!errors.email"
            fluid
          />
          <small v-if="errors.email" class="field-error">{{ errors.email }}</small>
        </div>

        <Button
          type="submit"
          label="Continuar"
          icon="pi pi-arrow-right"
          icon-pos="right"
          fluid
        />
      </form>

      <form
        v-else
        key="step-2"
        class="signup-wizard__form"
        @submit.prevent="handleSubmit"
      >
        <div class="pin-section">
          <label>Senha de acesso</label>
          <p class="pin-section__hint">8 dígitos numéricos</p>
          <AuthPinInput
            v-model="form.password"
            :length="8"
            :invalid="!!errors.password"
          />
          <small v-if="errors.password" class="field-error">{{ errors.password }}</small>
        </div>

        <div class="pin-section">
          <label>Confirme sua senha</label>
          <AuthPinInput
            v-model="form.confirmPassword"
            :length="8"
            :invalid="!!errors.confirmPassword"
          />
          <small v-if="errors.confirmPassword" class="field-error">{{ errors.confirmPassword }}</small>
        </div>

        <div class="signup-wizard__actions">
          <Button
            type="button"
            label="Voltar"
            icon="pi pi-arrow-left"
            severity="secondary"
            outlined
            @click="handleBackStep"
          />
          <Button
            type="submit"
            label="Criar conta"
            icon="pi pi-check"
            :loading="loading"
          />
        </div>
      </form>
    </Transition>

    <p class="signup-wizard__switch">
      Já possui conta?
      <button type="button" class="link-button" @click="emit('switch')">
        Entrar
      </button>
    </p>

    <AuthSignUpSuccess
      :visible="showSuccessDialog"
      :email="form.email.trim()"
      @close="handleDialogClose"
    />
  </div>
</template>

<style scoped>
.signup-wizard {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.signup-wizard__header h2 {
  margin: 0 0 0.35rem;
  font-size: 1.75rem;
  font-weight: 700;
}

.signup-wizard__header p {
  margin: 0;
  color: var(--p-text-muted-color);
  line-height: 1.6;
}

.signup-wizard__progress {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 0.5rem;
}

.signup-wizard__step {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.45rem;
  opacity: 0.45;
  transition: opacity 0.25s ease, transform 0.25s ease;
}

.signup-wizard__step--active,
.signup-wizard__step--done {
  opacity: 1;
}

.signup-wizard__step-dot {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 1.75rem;
  height: 1.75rem;
  border-radius: 999px;
  border: 1.5px solid var(--p-surface-300);
  font-size: 0.75rem;
  font-weight: 600;
  transition: all 0.25s ease;
}

.signup-wizard__step--active .signup-wizard__step-dot {
  border-color: var(--p-primary-500);
  background: var(--p-primary-500);
  color: var(--p-primary-contrast-color);
  transform: scale(1.05);
}

.signup-wizard__step--done .signup-wizard__step-dot {
  border-color: var(--p-primary-300);
  background: var(--p-primary-50);
  color: var(--p-primary-700);
}

.signup-wizard__step-label {
  font-size: 0.72rem;
  text-align: center;
  color: var(--p-text-muted-color);
}

.signup-wizard__form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.field-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 1rem;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 0.45rem;
}

.field label,
.pin-section label {
  font-size: 0.875rem;
  font-weight: 600;
}

.field-error {
  color: var(--p-red-500);
}

.pin-section {
  display: flex;
  flex-direction: column;
  gap: 0.65rem;
}

.pin-section__hint {
  margin: 0;
  font-size: 0.8125rem;
  color: var(--p-text-muted-color);
}

.signup-wizard__actions {
  display: grid;
  grid-template-columns: 1fr 1.4fr;
  gap: 0.75rem;
  margin-top: 0.5rem;
}

.signup-wizard__switch {
  margin: 0;
  text-align: center;
  color: var(--p-text-muted-color);
}

.link-button {
  border: 0;
  background: transparent;
  color: var(--p-primary-500);
  font-weight: 600;
  cursor: pointer;
  padding: 0;
}

.link-button:hover {
  text-decoration: underline;
}

@media (max-width: 640px) {
  .field-grid {
    grid-template-columns: 1fr;
  }

  .signup-wizard__step-label {
    display: none;
  }
}
</style>
