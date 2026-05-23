# AWS Bank — Web

Frontend do AWS Bank com **Nuxt 3** e **PrimeVue 4**.

## Stack

- [Nuxt 3](https://nuxt.com/)
- [PrimeVue 4](https://primevue.org/) com tema Aura
- [PrimeIcons](https://primevue.org/icons/)
- TypeScript

## Estrutura

```
web/
├── app.vue              # Raiz da aplicação
├── nuxt.config.ts       # Configuração Nuxt + PrimeVue
├── pages/               # Rotas file-based
│   └── index.vue
├── layouts/             # Layouts reutilizáveis
│   └── default.vue
├── assets/              # CSS e assets processados
│   └── css/
│       └── main.css
└── public/              # Arquivos estáticos
```

## Setup

```bash
cd web
npm install
```

## Desenvolvimento

```bash
npm run dev
```

Acesse [http://localhost:3000](http://localhost:3000).

## Build

```bash
npm run build
npm run preview
```

## PrimeVue

O módulo oficial `@primevue/nuxt-module` registra componentes automaticamente (tree-shaking). Exemplo:

```vue
<Button label="Salvar" icon="pi pi-check" />
```

Configuração em `nuxt.config.ts`.
