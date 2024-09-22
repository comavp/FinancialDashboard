<template>
  <h3>Инфмормация о портфеле</h3>
  <table border="1">
    <tr>
      <td>Название эмитента</td>
      <td>Брокер</td>
      <td>Количество, шт.</td>
    </tr>
    <tr v-for="(item, i) in investmentsPortfolioInfo" :key="i">
      <td>{{ item.issuerName }}</td>
      <td>{{ item.brokerName }}</td>
      <td>{{ item.quantity }}</td>
    </tr>
  </table>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      investmentsPortfolioInfo: [],
    };
  },
  mounted() {
    this.fetchInvestmentsPortfolioInfo();
  },
  methods: {
    async fetchInvestmentsPortfolioInfo() {
      try {
        const response = await axios.get(
          "http://localhost:8081/api/invest-transactions/portfolio"
        );
        response.data.forEach((element) => {
          const issuerName = element.issuerName;
          const brokerNameToQuantityMap = element.brokerNameToQuantityMap;
          for (let [key, value] of Object.entries(brokerNameToQuantityMap)) {
            const item = {
              issuerName: issuerName,
              brokerName: key,
              quantity: value,
            };
            this.investmentsPortfolioInfo.push(item);
          }
        });
      } catch (e) {
        console.log(e);
        alert("Ошибка");
      }
    },
  },
};
</script>

<style scoped></style>
