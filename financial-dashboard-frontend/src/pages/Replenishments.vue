<template>
  <div>
    <h3>История пополений</h3>
    <table border="1">
      <tr>
        <td>Дата</td>
        <td>Сумма, руб</td>
        <td>Нал/безнал</td>
        <td>Тип</td>
        <td>Брокер</td>
      </tr>
      <tr v-for="(replenishment, i) in replenishments" :key="i">
        <td>{{ replenishment.transactionDate }}</td>
        <td>{{ replenishment.sum }}</td>
        <td>{{ replenishment.nonCash }}</td>
        <td>{{ replenishment.type }}</td>
        <td>{{ replenishment.brokerName }}</td>
      </tr>
    </table>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      replenishments: [],
    };
  },
  mounted() {
    this.fetchReplenishments();
  },
  methods: {
    async fetchReplenishments() {
      try {
        const response = await axios.get(
          "http://localhost:8081/api/replenishment-transactions"
        );
        this.replenishments = response.data;
      } catch (e) {
        console.log(e);
        alert("Ошибка");
      }
    },
  },
};
</script>

<style scoped></style>
