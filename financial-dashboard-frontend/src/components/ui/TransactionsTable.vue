<template>
  <table class="transactions-table">
    <tr class="table-header">
      <td v-for="(columnName, i) in columnNames" :key="i" >{{ columnName }}</td>
    </tr>
    <tr v-for="(transaction, i) in transactions" :key="i" :class="{'odd-tr': isOddTr(i)}">
      <td>{{ transaction.transactionDate }}</td>
      <td>{{ transaction.issuerName }}</td>
      <td>{{ transaction.quantity }}</td>
      <td>{{ transaction.price }}</td>
      <td>{{ transaction.totalSum }}</td>
      <td>{{ transaction.comission }}</td>
      <td>{{ transaction.tax }}</td>
      <td>{{ transaction.operationType }}</td>
      <td>{{ transaction.brokerName }}</td>
    </tr>
  </table>
</template>

<script>
export default {
  name: 'transactions-table',
  props: {
    transactions: {
      type: Array,
      required: true
    },
    columnNames: {
      type: Array,
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
.table-header {
  background-color: rgb(60, 56, 52);
  color: white;
}
tr:hover {
  background-color: rgb(193, 190, 203);
}
.odd-tr {
  background-color: rgb(244, 244, 244);
}
</style>
