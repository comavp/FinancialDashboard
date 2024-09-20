<template>
  <div class="navbar">
    <button @click="$router.push('/investments-portfolio')">Портфель</button>
    <button @click="$router.push('/invest-transactions')">
      История покупок
    </button>
    <button @click="$router.push('/replenishments')">История пополнений</button>
  </div>
</template>

<script>
export default {};
</script>

<style scoped></style>
