<template>
    <h3>Расходы</h3>
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
        const response = await axios.get("http://localhost:8081/api/expenses-transactions");
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
