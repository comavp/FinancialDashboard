<template>
  <tr :class="{'odd-tr': isOddTr(transactionNumber)}">
    <td v-for="[key, value] in Object.entries(transaction)" :key="key">
      {{ value }}
    </td>
  </tr>
</template>

<script>
export default {
  name: 'transaction-row',
  props: {
    transaction: {
      type: Object,
      required: true
    },
    transactionNumber: {
      type: Number,
      required: true
    }
  },
  methods: {
    isOddTr(i) {
      return i % 2 === 0;
    }
  }
};
</script>

<style scoped>
tr:hover {
  background-color: rgb(193, 190, 203);
}
.odd-tr {
  background-color: rgb(244, 244, 244);
}
</style>
