<script setup lang="ts">
definePageMeta({
  layout: 'auth',
})

const mode = ref<'signin' | 'signup'>('signin')
const slideDirection = ref<'next' | 'prev'>('next')

const modeOptions = [
  { label: 'Entrar', value: 'signin' },
  { label: 'Criar conta', value: 'signup' },
]

const formTransition = computed(() =>
  slideDirection.value === 'next' ? 'auth-slide-next' : 'auth-slide-prev',
)

watch(mode, (value, previous) => {
  if (!previous) {
    return
  }

  slideDirection.value = value === 'signup' ? 'next' : 'prev'
})
</script>

<template>
  <div class="auth-page">
    <AuthBrandPanel />

    <section class="auth-panel">
      <p class="auth-panel__mobile-title">aws bank</p>

      <div class="auth-panel__card">
        <SelectButton
          v-model="mode"
          :options="modeOptions"
          option-label="label"
          option-value="value"
          class="auth-panel__toggle"
          :allow-empty="false"
        />

        <Transition :name="formTransition" mode="out-in">
          <AuthSignInForm
            v-if="mode === 'signin'"
            key="signin"
            @switch="mode = 'signup'"
          />
          <AuthSignUpForm
            v-else
            key="signup"
            @switch="mode = 'signin'"
          />
        </Transition>
      </div>

      <footer class="auth-panel__footer">
        <p>&copy; {{ new Date().getFullYear() }} aws bank</p>
      </footer>
    </section>
  </div>
</template>

<style scoped>
.auth-page {
  display: flex;
  min-height: 100vh;
}

.auth-panel {
  display: flex;
  flex: 1;
  flex-direction: column;
  justify-content: center;
  padding: 2rem 1.5rem;
  background: var(--p-surface-950);
}

.auth-panel__mobile-title {
  margin: 0 0 1.75rem;
  font-size: 1.25rem;
  font-weight: 600;
  letter-spacing: -0.02em;
  color: var(--p-text-color);
}

@media (min-width: 960px) {
  .auth-panel__mobile-title {
    display: none;
  }

  .auth-panel {
    padding: 3rem;
  }
}

.auth-panel__card {
  width: 100%;
  max-width: 32rem;
  margin: 0 auto;
}

.auth-panel__toggle {
  display: flex;
  width: 100%;
  margin-bottom: 1.75rem;
}

.auth-panel__toggle :deep(.p-togglebutton) {
  flex: 1;
  transition: all 0.2s ease;
}

.auth-panel__footer {
  margin-top: 2rem;
  text-align: center;
  font-size: 0.8125rem;
  color: var(--p-text-muted-color);
}

.auth-panel__footer p {
  margin: 0;
}
</style>
