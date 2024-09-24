<template>
  <div>
    <h3>История покупок</h3>
    <transactions-table :transactions="transactions" :columnNames="columnNames"></transactions-table>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      columnNames: [
        'Дата',
        'Название эмитента',
        'Количество, шт.',
        'Цена, руб',
        'Сумма, руб',
        'Комиссия',
        'Налог',
        'Тип',
        'Брокер'
      ],
      transactions: [],
    };
  },
  mounted() {
    this.fetchTransactions();
  },
  methods: {
    async fetchTransactions() {
      try {
        const response = await axios.get(
          "http://localhost:8081/api/invest-transactions"
        );
        this.transactions = response.data;
      } catch (e) {
        console.log(e);
        alert("Ошибка");
      }
    },
  },
};
</script>

<style scoped>

</style>
