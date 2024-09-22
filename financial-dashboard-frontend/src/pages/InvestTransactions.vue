<template>
  <div>
    <h3>История покупок</h3>
    <table border="1">
      <tr class="table-header">
        <td>Дата</td>
        <td>Название эмитента</td>
        <td>Количество, шт.</td>
        <td>Цена, руб</td>
        <td>Сумма, руб</td>
        <td>Комиссия</td>
        <td>Налог</td>
        <td>Тип</td>
        <td>Брокер</td>
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
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
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
        console.log(response);
        this.transactions = response.data;
      } catch (e) {
        console.log(e);
        alert("Ошибка");
      }
    },
    isOddTr(i) {
      return i % 2 === 0;
    }
  },
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
