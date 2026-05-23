<script setup lang="ts">
const props = withDefaults(defineProps<{
  modelValue: string
  length?: number
  invalid?: boolean
  disabled?: boolean
  masked?: boolean
}>(), {
  length: 8,
  invalid: false,
  disabled: false,
  masked: true,
})

const emit = defineEmits<{
  'update:modelValue': [value: string]
  complete: []
}>()

const inputs = ref<HTMLInputElement[]>([])
const activeIndex = ref(0)

const digits = computed(() => {
  const chars = props.modelValue.split('').slice(0, props.length)
  return Array.from({ length: props.length }, (_, index) => chars[index] ?? '')
})

function focusInput(index: number) {
  nextTick(() => {
    inputs.value[index]?.focus()
  })
}

function updateValue(next: string) {
  const sanitized = next.replace(/\D/g, '').slice(0, props.length)
  emit('update:modelValue', sanitized)

  if (sanitized.length === props.length) {
    emit('complete')
  }
}

function onInput(index: number, event: Event) {
  const target = event.target as HTMLInputElement
  const value = target.value.replace(/\D/g, '')

  if (!value) {
    const next = digits.value.slice()
    next[index] = ''
    updateValue(next.join(''))
    return
  }

  const next = digits.value.slice()

  if (value.length > 1) {
    const pasted = value.slice(0, props.length - index)
    for (let i = 0; i < pasted.length; i++) {
      next[index + i] = pasted[i] ?? ''
    }
    updateValue(next.join(''))
    focusInput(Math.min(index + pasted.length, props.length - 1))
    return
  }

  next[index] = value[0] ?? ''
  updateValue(next.join(''))

  if (index < props.length - 1) {
    focusInput(index + 1)
  }
}

function onKeydown(index: number, event: KeyboardEvent) {
  if (event.key === 'Backspace' && !digits.value[index] && index > 0) {
    const next = digits.value.slice()
    next[index - 1] = ''
    updateValue(next.join(''))
    focusInput(index - 1)
    event.preventDefault()
  }

  if (event.key === 'ArrowLeft' && index > 0) {
    focusInput(index - 1)
  }

  if (event.key === 'ArrowRight' && index < props.length - 1) {
    focusInput(index + 1)
  }
}

function onPaste(event: ClipboardEvent) {
  event.preventDefault()
  const pasted = event.clipboardData?.getData('text') ?? ''
  updateValue(pasted)
  focusInput(Math.min(pasted.replace(/\D/g, '').length, props.length) - 1)
}

function setInputRef(element: Element | null, index: number) {
  if (element instanceof HTMLInputElement) {
    inputs.value[index] = element
  }
}

watch(() => props.modelValue, (value) => {
  activeIndex.value = Math.min(value.length, props.length - 1)
})
</script>

<template>
  <div
    class="pin-input"
    :class="{ 'pin-input--invalid': invalid, 'pin-input--disabled': disabled }"
    @paste="onPaste"
  >
    <div
      v-for="(digit, index) in digits"
      :key="index"
      class="pin-input__cell"
      :class="{ 'pin-input__cell--active': activeIndex === index && !disabled }"
    >
      <input
        :ref="(el) => setInputRef(el, index)"
        class="pin-input__field"
        type="text"
        inputmode="numeric"
        autocomplete="one-time-code"
        maxlength="1"
        :value="digit"
        :disabled="disabled"
        aria-label="Digito da senha"
        @input="onInput(index, $event)"
        @keydown="onKeydown(index, $event)"
        @focus="activeIndex = index"
      >
      <span v-if="masked && digit" class="pin-input__dot" />
    </div>
  </div>
</template>

<style scoped>
.pin-input {
  display: flex;
  gap: 0.625rem;
  justify-content: center;
}

.pin-input__cell {
  position: relative;
  width: 2.75rem;
  height: 3.25rem;
  border: 1.5px solid var(--p-surface-600);
  border-radius: 0.875rem;
  background: var(--p-surface-800);
  transition: border-color 0.2s ease, box-shadow 0.2s ease, transform 0.2s ease;
}

.pin-input__cell--active {
  border-color: var(--p-primary-400);
  box-shadow: 0 0 0 3px color-mix(in srgb, var(--p-primary-500) 16%, transparent);
  transform: translateY(-1px);
}

.pin-input--invalid .pin-input__cell {
  border-color: var(--p-red-400);
}

.pin-input--disabled .pin-input__cell {
  opacity: 0.55;
}

.pin-input__field {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  border: 0;
  background: transparent;
  color: transparent;
  caret-color: var(--p-primary-500);
  text-align: center;
  font-size: 1.25rem;
  outline: none;
}

.pin-input__dot {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  pointer-events: none;
}

.pin-input__dot::after {
  content: '';
  width: 0.55rem;
  height: 0.55rem;
  border-radius: 999px;
  background: var(--p-text-color);
  animation: auth-scale-in 0.18s ease;
}

@media (max-width: 480px) {
  .pin-input {
    gap: 0.45rem;
  }

  .pin-input__cell {
    width: 2.35rem;
    height: 2.9rem;
  }
}
</style>
