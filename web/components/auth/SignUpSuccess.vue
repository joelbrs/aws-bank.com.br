<script setup lang="ts">
const props = defineProps<{
  email: string
  visible: boolean
}>()

const emit = defineEmits<{
  close: []
}>()

const { resendEmailVerification, getApiErrorMessage } = useAuthApi()
const toast = useToast()
const { remaining, canResend, start, clearTimer } = useResendCooldown(30)

const loading = ref(false)

watch(() => props.visible, (isVisible) => {
  if (isVisible) {
    start()
  }
  else {
    clearTimer()
  }
})

async function resendEmail() {
  if (!canResend.value) {
    return
  }

  loading.value = true

  try {
    await resendEmailVerification({ email: props.email })

    toast.add({
      severity: 'success',
      summary: 'E-mail reenviado',
      detail: 'Verifique sua caixa de entrada e spam.',
      life: 4000,
    })

    start()
  }
  catch (error) {
    toast.add({
      severity: 'error',
      summary: 'Não foi possível reenviar',
      detail: getApiErrorMessage(error),
      life: 6000,
    })
  }
  finally {
    loading.value = false
  }
}

function handleClose() {
  emit('close')
}
</script>

<template>
  <Dialog
    :visible="visible"
    modal
    :closable="false"
    :draggable="false"
    class="signup-success-dialog"
    :style="{ width: 'min(92vw, 26rem)' }"
    @update:visible="(value: boolean) => !value && handleClose()"
  >
    <template #header>
      <div class="dialog-header auth-scale-in">
        <h2>Cadastro realizado com sucesso</h2>
        <p>Verifique seu endereço de e-mail para concluir o processo</p>
      </div>
    </template>

    <div class="dialog-body auth-scale-in">
      <p class="dialog-body__text">
        Enviamos um link de confirmação para
        <strong>{{ email }}</strong>.
        Acesse sua caixa de entrada para ativar sua conta.
      </p>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <Button
          label="Ir para login"
          icon="pi pi-sign-in"
          fluid
          @click="handleClose"
        />
        <Button
          :label="canResend ? 'Reenviar link' : `Reenviar em ${remaining}s`"
          icon="pi pi-refresh"
          outlined
          fluid
          :disabled="!canResend"
          :loading="loading"
          @click="resendEmail"
        />
      </div>
    </template>
  </Dialog>
</template>

<style scoped>
.dialog-header {
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
}

.dialog-header h2 {
  margin: 0;
  font-size: 1.1875rem;
  font-weight: 700;
  line-height: 1.35;
}

.dialog-header p {
  margin: 0;
  font-size: 0.8125rem;
  color: var(--p-text-muted-color);
}

.dialog-body {
  display: flex;
  flex-direction: column;
  gap: 0.875rem;
}

.dialog-body__text {
  margin: 0;
  font-size: 0.875rem;
  color: var(--p-text-muted-color);
  line-height: 1.6;
}

.dialog-footer {
  display: flex;
  flex-direction: column;
  gap: 0.625rem;
  width: 100%;
}
</style>
