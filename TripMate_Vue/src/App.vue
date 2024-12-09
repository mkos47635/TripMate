<script setup>
import { RouterView, useRoute } from "vue-router";
import Header from "./components/Header.vue";

const route = useRoute();
</script>

<template>
  <div id="app">
    <!-- route.meta.hideHeader가 없거나 false일 때만 Header 렌더링 -->
    <Header v-if="!route.meta.hideHeader" />
    <router-view />
  </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body {
  height: 100%;
  /* 화면 전체를 채우기 위해 설정 */
  margin: 0;
  padding: 0;
  background-color: #E8F5F5;
  /* 배경 색상을 설정 */
}

#app {
  display: flex;
  flex-direction: column;
  align-items: center;
  /* 수평 중앙 정렬 */
  justify-content: center;
  /* 수직 중앙 정렬 */
  min-height: 100vh;
  /* 전체 화면 높이를 채움 */
  width: 100%;
}


header,
footer {
  width: 100%;
  max-width: 1200px;
  /* 중앙에 위치할 영역의 최대 너비 설정 */
  padding: 0;
  /* 불필요한 패딩 제거 */
}

router-view {
  width: 100%;
  max-width: 1200px;
  /* 동일하게 최대 너비 설정 */
  padding: 0;
  /* 불필요한 패딩 제거 */
}
</style>
