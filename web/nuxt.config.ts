import Aura from '@primeuix/themes/aura'

// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2025-07-15',
  devtools: { enabled: true },

  app: {
    head: {
      htmlAttrs: {
        class: 'dark',
      },
    },
  },

  modules: ['@primevue/nuxt-module'],

  css: ['~/assets/css/main.css', '~/assets/css/auth.css', 'primeicons/primeicons.css'],

  runtimeConfig: {
    public: {
      apiBaseUrl: process.env.NUXT_PUBLIC_API_BASE_URL || '/api',
    },
  },

  nitro: {
    devProxy: {
      '/api/': {
        target: 'http://localhost:9090/',
        changeOrigin: true,
      },
    },
  },

  primevue: {
    options: {
      ripple: true,
      theme: {
        preset: Aura,
        options: {
          darkModeSelector: '.dark',
        },
      },
    },
  },
})
