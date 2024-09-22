<template>
  <div class="navbar">
    <button 
      @click="processNavbarButtonClick('/investments-portfolio')" 
      :class="{'active': buttonActive('/investments-portfolio')}"
    >
      Портфель
    </button>
    <button 
      @click="processNavbarButtonClick('/invest-transactions')" 
      :class="{'active': buttonActive('/invest-transactions')}"
    >
      История покупок
    </button>
    <button 
      @click="processNavbarButtonClick('/replenishments')" 
      :class="{'active': buttonActive('/replenishments')}"
    >
      История пополнений
    </button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      currentPath: '',
      buttonsStateMap: {
        '/investments-portfolio': false,
        '/invest-transactions': false,
        '/replenishments': false
      }
    }
  },
  methods: {
    processNavbarButtonClick(selectedPath) {
      this.$router.push(selectedPath);
      if (this.currentPath != '') {
        this.buttonsStateMap[this.currentPath] = false;
      }
      this.buttonsStateMap[selectedPath] = true;
      this.currentPath = selectedPath;
    },
    buttonActive(selectedPath) {
      return this.buttonsStateMap[selectedPath];
    }
  }
};
</script>

<style scoped>
.navbar {
  background-color: rgb(246, 248, 250); /* github navbar color */
  height: 50px;
  align-items: center;
}
button {
  background: none;
  border: 0;
  font-size: 16px;
  padding: 10px 15px;
}
button:hover {
  background-color: rgb(234, 237, 240); /* github navbar color */
}
button.active {
  font-weight: 700;
}
</style>
