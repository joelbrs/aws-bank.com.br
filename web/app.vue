<script setup lang="ts">
useHead({
  htmlAttrs: {
    class: 'dark',
  },
})
</script>

<template>
  <NuxtLayout>
    <Toast position="top-right" />
    <NuxtPage />
  </NuxtLayout>
</template>
