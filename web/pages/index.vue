<script setup lang="ts">
const email = ref('')
const password = ref('')
const year = new Date().getFullYear()
</script>

<template>
  <div class="page">
    <header class="header">
      <div class="header-content">
        <div class="brand">
          <span class="brand-logo">AB</span>
          <div>
            <p class="brand-name">AWS Bank</p>
            <p class="brand-subtitle">Frontend Nuxt 3 + PrimeVue</p>
          </div>
        </div>
      </div>
    </header>

    <main class="main">
      <Card>
        <template #title>
          <div class="card-title">
            <i class="pi pi-check-circle title-icon" />
            PrimeVue configurado
          </div>
        </template>
        <template #content>
          <p class="card-description">
            Componentes PrimeVue disponíveis via auto-import. Tema Aura ativo.
          </p>
        </template>
      </Card>

      <section>
        <h2>Componentes</h2>
        <div class="button-group">
          <Button label="Primário" icon="pi pi-check" />
          <Button label="Secundário" severity="secondary" icon="pi pi-user" />
          <Button label="Sucesso" severity="success" icon="pi pi-check-circle" />
          <Button label="Aviso" severity="warn" icon="pi pi-exclamation-triangle" />
          <Button label="Erro" severity="danger" icon="pi pi-times-circle" />
          <Button label="Info" severity="info" icon="pi pi-info-circle" />
          <Button label="Outlined" outlined icon="pi pi-external-link" />
          <Button label="Text" text icon="pi pi-arrow-right" icon-pos="right" />
        </div>
      </section>

      <section>
        <h2>Formulário</h2>
        <div class="form-grid">
          <div class="field">
            <label for="email">E-mail</label>
            <InputText
              id="email"
              v-model="email"
              type="email"
              placeholder="seu@email.com"
            />
          </div>
          <div class="field">
            <label for="password">Senha</label>
            <Password
              id="password"
              v-model="password"
              placeholder="••••••••"
              toggle-mask
              :feedback="false"
            />
          </div>
          <Button label="Entrar" icon="pi pi-sign-in" class="submit-button" />
        </div>
      </section>

      <Message severity="info" :closable="false">
        Edite <code>pages/index.vue</code> para começar a desenvolver.
      </Message>
    </main>

    <footer class="footer">
      AWS Bank &copy; {{ year }}
    </footer>
  </div>
</template>

<style scoped>
.page {
  display: flex;
  min-height: 100vh;
  flex-direction: column;
  background: var(--p-surface-50);
}

.header {
  border-bottom: 1px solid var(--p-surface-200);
  background: var(--p-surface-0);
  padding: 1rem 1.5rem;
}

.header-content {
  max-width: 64rem;
  margin: 0 auto;
}

.brand {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.brand-logo {
  display: flex;
  height: 2.25rem;
  width: 2.25rem;
  align-items: center;
  justify-content: center;
  border-radius: 0.5rem;
  background: var(--p-primary-500);
  color: var(--p-primary-contrast-color);
  font-size: 0.875rem;
  font-weight: 600;
}

.brand-name {
  margin: 0;
  font-weight: 600;
}

.brand-subtitle {
  margin: 0;
  font-size: 0.875rem;
  color: var(--p-text-muted-color);
}

.main {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2rem;
  max-width: 64rem;
  width: 100%;
  margin: 0 auto;
  padding: 2rem 1.5rem;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.title-icon {
  color: var(--p-green-500);
}

.card-description {
  margin: 0;
  color: var(--p-text-muted-color);
}

section h2 {
  margin: 0 0 1rem;
  font-size: 1.125rem;
  font-weight: 600;
}

.button-group {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.form-grid {
  display: grid;
  gap: 1rem;
  max-width: 24rem;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.field label {
  font-size: 0.875rem;
  font-weight: 500;
}

.submit-button {
  width: 100%;
}

code {
  padding: 0.125rem 0.375rem;
  border-radius: 0.25rem;
  background: var(--p-surface-100);
  font-size: 0.875rem;
}

.footer {
  border-top: 1px solid var(--p-surface-200);
  padding: 1rem 1.5rem;
  text-align: center;
  font-size: 0.875rem;
  color: var(--p-text-muted-color);
}
</style>
