<script setup lang="ts">
const year = new Date().getFullYear()
</script>

<template>
  <aside class="brand-panel">
    <p class="brand-panel__name">aws bank</p>

    <footer class="brand-panel__footer">
      <p>&copy; {{ year }} aws bank</p>
    </footer>
  </aside>
</template>

<style scoped>
.brand-panel {
  display: none;
  position: relative;
  min-height: 100vh;
  background: var(--auth-brand-bg);
  color: var(--auth-brand-text);
  border-right: 1px solid var(--auth-brand-border);
}

@media (min-width: 960px) {
  .brand-panel {
    display: flex;
    flex: 1;
    flex-direction: column;
    justify-content: space-between;
    padding: 2.5rem 2.75rem;
  }
}

.brand-panel__name {
  margin: 0;
  align-self: flex-start;
  font-size: 1.5rem;
  font-weight: 600;
  letter-spacing: -0.02em;
  animation: auth-scale-in 0.4s ease;
}

.brand-panel__footer {
  align-self: stretch;
  font-size: 0.8125rem;
  color: var(--auth-brand-footer);
}

.brand-panel__footer p {
  margin: 0;
}
</style>
