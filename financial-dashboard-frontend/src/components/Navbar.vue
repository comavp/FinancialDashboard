<template>
  <div class="navbar">
    <button @click="$router.push('/')">Портфель</button>
    <button @click="$router.push('/invest-transactions')">
      История пополнений
    </button>
    <button @click="$router.push('/replenishments')">История покупок</button>
  </div>
</template>

<script>
export default {};
</script>

<style scoped></style>
