<script setup>
import PlanMakeSearch from "@/components/plans/PlanMakeSearch.vue";
import PlanMakeRouteMap from '@/components/plans/PlanMakeRouteMap.vue';
import { defineProps, ref, onMounted } from "vue";
import PlanMakeList from '@/components/plans/PlanMakeList.vue';
import { usePlanStore } from "@/stores/planStore";
import { useMapStore } from "@/stores/mapStore";
import { getPlanDetailList, sendPlanItems  } from "@/api/plan.js";

const planStore = usePlanStore();
const mapStore = useMapStore();
const props = defineProps({
  id: String,
});

const planId = planStore.planInfo.planId;

const getDetail = () => {
  getPlanDetailList(
    planId,
    ({ data }) => {
      console.log("Fetched plan details:", data); // 데이터 확인
      planStore.items = data; // 응답 데이터를 저장
    },
    (error) => {
      console.error("API 요청 실패:", error);
    }
  );
};


// tourDay 값 가져오기
const tourDay = planStore.planInfo.tourDay || 1; // 기본값 설정
const selectedDay = ref(1); // 기본적으로 Day 1이 선택된 상태

// Day 선택 및 처리 함수
const searchDayPlan = (day) => {
  selectedDay.value = day; // 선택된 Day 업데이트
  planStore.selectDay = day;
  console.log("Selected Day:", planStore.selectDay);
  console.log("Plan Items:", planStore.filteredItems); // items 확인
};

const searchRoute = () => {
  const planStore = usePlanStore();
  const itemsToSend = planStore.items;

  sendPlanItems(
    itemsToSend,
    ({ data }) => {
      console.log(data);
      mapStore.setMarkers(data);
    },
    (error) => {
      console.error("API 요청 실패:", error);
    }
  );
}




onMounted(() => {
  getDetail();
  selectedDay.value = "1";
  planStore.selectDay = "1";
});
</script>

<template>
  <div class="page-layout">
    <div class="content-container">
      <div class="search-section">
        <PlanMakeSearch />
      </div>
      <div class="map-section">
        <PlanMakeRouteMap />
      </div>
      <div class="list-section">
        <div class="title-section">
          <h1>{{ planStore.planInfo.title }}</h1>
        </div>
        
        <!-- 버튼 리스트 -->
        <div class="button-list">
          <button 
            v-for="day in tourDay" 
            :key="day" 
            class="day-button"
            :class="{ active: selectedDay === day }"
            @click="searchDayPlan(day)"
          >
            Day {{ day }}
          </button>
          <button class="routeBuffon" @click="searchRoute">경로 찾기</button>
        </div>
        
        <PlanMakeList />
      </div>
    </div>
  </div>
</template>

<style scoped>
.title-section {
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  padding-top: 20px;
  padding-left: 10px;
  padding-right: 10px;
  padding-bottom: 20px;
  text-align: center;
}

.page-layout {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 100vh; /* 화면 전체 높이를 차지 */
  box-sizing: border-box;
}

.content-container {
  display: grid;
  grid-template-columns: 1fr 2fr 1fr;
  gap: 10px;
  width: 1800px;
  height: 800px;
  padding: 30px;
  box-sizing: border-box;
  margin-top: 100px; /* 헤더 아래로 공간 추가 */
}

/* 공통 스타일 */
.search-section,
.list-section {
  background-color: #f7fdff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); 
}

.map-section {
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

/* 버튼 리스트 스타일 */
.button-list {
  display: flex;
  flex-wrap: wrap; /* 버튼이 넘칠 경우 다음 줄로 이동 */
  gap: 10px; /* 버튼 간격 */
  margin-top: 20px;
  margin-bottom: 10px;
}

.day-button {
  flex: 1; /* 동일한 가로 너비를 가지도록 설정 */
  min-width: 80px; /* 버튼 최소 너비 */
  max-width: 120px; /* 버튼 최대 너비 */
  height: 40px;
  background-color: #007BFF;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  text-align: center;
  line-height: 40px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  transition: background-color 0.3s;
}

.day-button:hover {
  background-color: #0056b3;
}

.day-button.active {
  background-color: #0056b3; /* 활성 상태일 때 배경색 변경 */
  font-weight: bold; /* 강조 */
  box-shadow: 0 0 10px rgba(0, 91, 179, 0.5); /* 강조된 외곽선 */
}

.routeBuffon {
  flex: 1; /* 동일한 가로 너비를 가지도록 설정 */
  min-width: 90px; /* 버튼 최소 너비 */
  max-width: 150px; /* 버튼 최대 너비 */
  height: 40px;
  background-color: #929292; /* 기본 배경색 (초록색 계열) */
  color: white; /* 글자색 */
  border: none;
  border-radius: 5px; /* 모서리 둥글게 */
  cursor: pointer;
  text-align: center;
  line-height: 40px; /* 세로 정렬 */
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
  transition: background-color 0.3s, transform 0.2s; /* 부드러운 색상 및 크기 전환 */
}

.routeBuffon:hover {
  background-color: #616161; /* 호버 상태 배경색 (더 어두운 초록색 계열) */
  transform: scale(1.05); /* 살짝 확대 */
}

.routeBuffon:active {
  background-color: #616161; /* 클릭 상태 배경색 */
  transform: scale(0.95); /* 클릭 시 살짝 축소 */
}
</style>
