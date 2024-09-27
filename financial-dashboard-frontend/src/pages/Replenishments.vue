<template>
  <div>
    <h3>История пополнений</h3>
    <transactions-table :transactions="replenishments" :columnNames="columnNames"></transactions-table>
    <pages-list :totalPages="totalPages" :currentPage="currentPage" @changePage="changePage"></pages-list>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      replenishments: [],
      columnNames: [
        'Дата',
        'Сумма, руб',
        'Нал/безнал',
        'Тип',
        'Брокер'
      ],
      itemsOnPage: 50,
      totalPages: 0,
      currentPage: 1
    };
  },
  mounted() {
    this.getTransactionsCount();
    this.fetchReplenishments();
  },
  methods: {
    async fetchReplenishments() {
      try {
        const response = await axios.post(
          "http://localhost:8081/api/replenishment-transactions/filter",
          {
            pageNumber: this.currentPage - 1,
            itemsOnPage: this.itemsOnPage
          }
        );
        this.replenishments = response.data;
      } catch (e) {
        console.log(e);
        alert("Ошибка");
      }
    },
    async getTransactionsCount() {
      try {
        const response = await axios.get("http://localhost:8081/api/replenishment-transactions/count");
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
      this.fetchReplenishments();
    }
  }
};
</script>

<style scoped></style>
