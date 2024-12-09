<script setup>
import { ref } from "vue";
import { tripAxios } from "@/util/http-commons"; // axios 사용
import { VueSpinnerBars } from "vue3-spinners"; // 로딩 스피너

import TripCard from "@/components/attractions/TripCard.vue"; // TripCard 컴포넌트

// axios 인스턴스
const axios = tripAxios();

const { VITE_SEARCH_TRIP_URL, VITE_OPEN_API_SERVICE_KEY } = import.meta.env;

const isLoading = ref(false); // 로딩 상태
const attractions = ref([]); // 관광지 목록
const param = ref({
  serviceKey: VITE_OPEN_API_SERVICE_KEY,
  pageNo: 1,
  numOfRows: 10,
  keyword: "",
  MobileOS: "ETC",
  MobileApp: "AppTest",
  _type: "json",
});

// 관광지 검색 함수
const searchAttraction = () => {
  isLoading.value = true; // 로딩 시작
  axios
    .get(VITE_SEARCH_TRIP_URL, { params: param.value })
    .then(({ data }) => {
      attractions.value = data.response.body.items.item;
    })
    .catch((error) => {
      console.log(error);
    })
    .finally(() => {
      isLoading.value = false; // 로딩 종료
    });
};
</script>

<template>
  <main>
    <div class="find-attraction">
      <h2>관광지 찾기</h2>
    </div>

    <div class="search-container">
      <!-- 검색창 입력 필드 -->
      <input
        type="text"
        placeholder="관광지 검색어를 입력하세요"
        class="search-input"
        v-model="param.keyword" 
      />
      <!-- 검색 버튼 -->
      <button class="search-button" @click="searchAttraction">검색</button>
    </div>

    <!-- 로딩 중일 때 스피너 표시 -->
    <div class="row mt-5" v-if="isLoading">
      <VueSpinnerBars size="50" color="deeppink" />
    </div>

    <!-- 검색 결과가 있을 때 관광지 리스트 표시 -->
    <div class="row" v-else>
      <TripCard
        v-for="attraction in attractions"
        :key="attraction.contentid"
        :attraction="attraction"
      />
    </div>
  </main>
</template>

<style scoped>
main {
  padding: 30px 10px; /* 상단 고정된 박스를 고려하여 상단 여백 추가 */
  background-color: #E8F5F5;
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
}

.find-attraction {
  background-color: rgb(73, 154, 221);
  width: 900px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  top: 160px;
  left: 50%;
  transform: translateX(-50%); /* 중앙 정렬 */
  z-index: 10; /* 다른 요소들보다 위에 표시 */
}

h2 {
  color: white;
  margin: 0; /* 기본 마진 제거 */
}

.search-container {
  margin-top: 200px; /* 상단 고정 박스를 고려하여 여백 추가 */
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

.search-input {
  padding: 10px;
  width: 300px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.search-button {
  padding: 10px 15px;
  background-color: rgb(73, 154, 221);
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.search-button:hover {
  background-color: #0056b3;
}

.search-input:focus,
.search-button:focus {
  outline: none;
  box-shadow: 0 0 5px rgba(73, 154, 221, 0.5);
}
</style>
