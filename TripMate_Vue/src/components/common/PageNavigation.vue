<script setup>
import { computed } from "vue";

const props = defineProps({ currentPage: Number, totalPage: Number });
const emit = defineEmits(["pageChange"]);

const navigationSize = parseInt(import.meta.env.VITE_ARTICLE_NAVIGATION_SIZE);

const startPage = computed(() => {
  return parseInt((props.currentPage - 1) / navigationSize) * navigationSize + 1;
});

const endPage = computed(() => {
  let lastPage =
    parseInt((props.currentPage - 1) / navigationSize) * navigationSize + navigationSize;
  return props.totalPage < lastPage ? props.totalPage : lastPage;
});

const endRange = computed(() => {
  return parseInt((props.totalPage - 1) / navigationSize) * navigationSize < props.currentPage;
});

function range(start, end) {
  const list = [];
  for (let i = start; i <= end; i++) list.push(i);
  return list;
}

function onPageChange(pg) {
  console.log(pg + "로 이동!!!");
  emit("pageChange", pg);
}
</script>

<template>
  <div class="pagination-container">
    <ul class="pagination">
      <li class="page-item">
        <a class="page-link" @click="onPageChange(1)">최신</a>
      </li>
      <li class="page-item">
        <a class="page-link" @click="onPageChange(startPage == 1 ? 1 : startPage - 1)">이전</a>
      </li>
      <template v-for="pg in range(startPage, endPage)" :key="pg">
        <li :class="currentPage === pg ? 'page-item active' : 'page-item'">
          <a class="page-link" @click="onPageChange(pg)">{{ pg }}</a>
        </li>
      </template>
      <li class="page-item">
        <a class="page-link" @click="onPageChange(endRange ? totalPage : endPage + 1)">다음</a>
      </li>
      <li class="page-item">
        <a class="page-link" @click="onPageChange(totalPage)">마지막</a>
      </li>
    </ul>
  </div>
</template>

<style scoped>
.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  margin-bottom: 10px;
}

.pagination {
  display: flex;
  gap: 10px;
  list-style: none;
  padding: 0;
}

.page-item {
  font-size: 14px;
  margin-left: 3px;
  margin-right: 3px;
}

.page-link {
  display: inline-block;
  padding: 6px 12px;
  font-size: 14px;
  color: #000000;
  background-color: rgb(199, 230, 255);
  border: 1px solid #83b7ee;
  border-radius: 4px;
  cursor: pointer;
  text-decoration: none;
}

.page-link:hover {
  background-color: #83b7ee;
}

.page-item.active .page-link {
  background-color: #83b7ee;
  border-color: #83b7ee;
}

.page-item a {
  text-decoration: none;
}
</style>
