<template>
    <h3>Доходы</h3>
    <transactions-table :transactions="transactions" :columnNames="columnNames"></transactions-table>
</template>

<script>
import axios from "axios";

export default {
    data() {
    return {
      transactions: [],
      columnNames: [
        'Дата',
        'Сумма',
        'Категория',
        'Описание',
      ]
    };
  },
  mounted() {
    this.fetchTransactions();
  },
  methods: {
    async fetchTransactions() {
      try {
        const response = await axios.get("http://localhost:8081/api/income-transactions");
        this.transactions = response.data;
      } catch (e) {
        console.log(e);
        alert("Ошибка");
      }
    }
  }
}
</script>

<style scoped>

</style>
