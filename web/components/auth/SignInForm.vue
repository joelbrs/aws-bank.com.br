<script setup lang="ts">
const emit = defineEmits<{
  switch: []
}>()

const toast = useToast()
const loading = ref(false)

const form = reactive({
  email: '',
  password: '',
})

const errors = reactive({
  email: '',
  password: '',
})

function validate(): boolean {
  errors.email = form.email.trim() ? '' : 'Informe o e-mail.'
  errors.password = form.password ? '' : 'Informe a senha.'

  if (form.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
    errors.email = 'Informe um e-mail válido.'
  }

  return !errors.email && !errors.password
}

async function onSubmit() {
  if (!validate()) {
    return
  }

  loading.value = true

  try {
    toast.add({
      severity: 'info',
      summary: 'Login em breve',
      detail: 'A autenticação será disponibilizada após a confirmação do e-mail.',
      life: 5000,
    })
  }
  finally {
    loading.value = false
  }
}
</script>

<template>
  <form class="auth-form" @submit.prevent="onSubmit">
    <header class="auth-form__header">
      <h2>Bem-vindo de volta</h2>
      <p>Entre com seu e-mail e senha para acessar sua conta.</p>
    </header>

    <div class="field">
      <label for="signin-email">E-mail</label>
      <InputText
        id="signin-email"
        v-model="form.email"
        type="email"
        placeholder="seu@email.com"
        autocomplete="email"
        :invalid="!!errors.email"
        fluid
      />
      <small v-if="errors.email" class="field-error">{{ errors.email }}</small>
    </div>

    <div class="field">
      <label for="signin-password">Senha</label>
      <Password
        id="signin-password"
        v-model="form.password"
        placeholder="••••••••"
        toggle-mask
        :feedback="false"
        autocomplete="current-password"
        :invalid="!!errors.password"
        fluid
      />
      <small v-if="errors.password" class="field-error">{{ errors.password }}</small>
    </div>

    <Button
      type="submit"
      label="Entrar"
      icon="pi pi-sign-in"
      :loading="loading"
      fluid
    />

    <p class="auth-form__switch">
      Ainda não tem conta?
      <button type="button" class="link-button" @click="emit('switch')">
        Criar conta
      </button>
    </p>
  </form>
</template>

<style scoped>
.auth-form {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.auth-form__header h2 {
  margin: 0 0 0.35rem;
  font-size: 1.75rem;
  font-weight: 700;
}

.auth-form__header p {
  margin: 0;
  color: var(--p-text-muted-color);
  line-height: 1.6;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 0.45rem;
}

.field label {
  font-size: 0.875rem;
  font-weight: 600;
}

.field-error {
  color: var(--p-red-500);
}

.auth-form__switch {
  margin: 0.5rem 0 0;
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
</style>
