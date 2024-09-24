<template>
  <table class="transactions-table">
    <tr class="table-header">
      <td v-for="(columnName, i) in columnNames" :key="i" >{{ columnName }}</td>
    </tr>
    <transaction-row 
      v-for="(transaction, i) in transactions" 
      :key="i" 
      :transaction="transaction"
      :transactionNumber="i"
    >
    </transaction-row>
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
  }
};
</script>

<style scoped>
.table-header {
  background-color: rgb(60, 56, 52);
  color: white;
}
</style>
