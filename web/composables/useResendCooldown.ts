export function useResendCooldown(initialSeconds = 30) {
  const remaining = ref(initialSeconds)
  const canResend = computed(() => remaining.value === 0)
  let timer: ReturnType<typeof setInterval> | null = null

  function clearTimer() {
    if (timer) {
      clearInterval(timer)
      timer = null
    }
  }

  function start() {
    clearTimer()
    remaining.value = initialSeconds

    timer = setInterval(() => {
      if (remaining.value <= 1) {
        remaining.value = 0
        clearTimer()
        return
      }

      remaining.value -= 1
    }, 1000)
  }

  onUnmounted(clearTimer)

  return {
    remaining,
    canResend,
    start,
    clearTimer,
  }
}
