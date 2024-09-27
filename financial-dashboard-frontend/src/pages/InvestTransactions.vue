<template>
  <div>
    <h3>История покупок</h3>
    <transactions-table :transactions="transactions" :columnNames="columnNames"></transactions-table>
    <pages-list :totalPages="totalPages" :currentPage="currentPage" @changePage="changePage"></pages-list>
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
      itemsOnPage: 100,
      totalPages: 0,
      currentPage: 1
    };
  },
  mounted() {
    this.getTransactionsCount();
    this.fetchTransactions();
  },
  methods: {
    async fetchTransactions() {
      try {
        const response = await axios.post(
          "http://localhost:8081/api/invest-transactions/filter",
          {
            pageNumber: this.currentPage - 1,
            itemsOnPage: this.itemsOnPage
          }
        );
        this.transactions = response.data;
      } catch (e) {
        console.log(e);
        alert("Ошибка");
      }
    },
    async getTransactionsCount() {
      try {
        const response = await axios.get("http://localhost:8081/api/invest-transactions/count");
        this.totalPages = Math.ceil(response.data / this.itemsOnPage);
      } catch (e) {
        console.log(e);
        alert("Ошибка");
      }
    },
    changePage(pageNumber) {
      this.currentPage = pageNumber;
    }
  },
  watch: {
    currentPage() {
      this.fetchTransactions();
    }
  }
};
</script>

<style scoped>

</style>
