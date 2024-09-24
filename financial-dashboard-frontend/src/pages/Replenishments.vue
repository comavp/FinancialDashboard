<template>
  <div>
    <h3>История пополений</h3>
    <transactions-table :transactions="replenishments" :columnNames="columnNames"></transactions-table>
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
      ]
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
