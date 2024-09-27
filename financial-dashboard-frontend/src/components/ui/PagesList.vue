<template>
    <div class="pages-container">
      <div 
        v-for="pageNumber in totalPages" 
        :key="pageNumber" 
        class="page" 
        :class="{'current-page': currentPage === pageNumber}"
        @click="changePage(pageNumber)"
      >
        {{ pageNumber }}
      </div>
    </div>
</template>

<script>

export default {
    name: 'pages-list',
    props: {
        totalPages: {
            type: Number,
            required: true
        },
        currentPage: {
            type: Number,
            required: true
        }
    },
    methods: {
        changePage(pageNumber) {
            this.$emit('changePage', pageNumber);
        }
    }
}
</script>

<style scoped>
.pages-container {
  display: flex;
  margin-top: 15px
}

.page {
  border: 1px solid grey;
  padding: 5px;
}

.current-page {
  border: 2px solid black;
}

.page:hover {
  background-color: rgb(193, 190, 203);
}
</style>
