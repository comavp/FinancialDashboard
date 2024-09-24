<template>
  <h3>Инфмормация о портфеле</h3>
  <transactions-table :transactions="investmentsPortfolioInfo" :columnNames="columnNames" ></transactions-table>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      investmentsPortfolioInfo: [],
      columnNames: [
        'Название эмитента',
        'Брокер',
        'Количество, шт.'
      ]
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
