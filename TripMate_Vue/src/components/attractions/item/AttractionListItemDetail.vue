<script setup>
import { useRoute } from "vue-router";
import PlanMakeKakao from '@/components/plans/PlanMakeKakao.vue';
import { useMapStore } from "@/stores/mapStore";

const mapStore = useMapStore();

const route = useRoute();
const attraction = {
  no: route.query.no,
  title: route.query.title,
  addr1: route.query.addr1,
  addr2: route.query.addr2,
  firstImage1: route.query.firstImage1 ? route.query.firstImage1 : "/src/assets/img/noImage.png",
  latitude: route.query.latitude,
  longitude: route.query.longitude,
};

mapStore.setPosition(attraction.latitude, attraction.longitude);
</script>

<template>
  <div class="attraction-container">
    <!-- 왼쪽 영역 -->
    <div class="attraction-left">
      <h1>{{ attraction.title }}</h1>
      <p><strong>주소 1:</strong> {{ attraction.addr1 }}</p>
      <p><strong>주소 2:</strong> {{ attraction.addr2 }}</p>
      <p><strong>이미지:</strong></p>
      <img :src="attraction.firstImage1" alt="Attraction Image" />
      <p><strong>위도:</strong> {{ attraction.latitude }}</p>
      <p><strong>경도:</strong> {{ attraction.longitude }}</p>
    </div>
    <!-- 오른쪽 영역 -->
    <div class="attraction-right">
      <PlanMakeKakao />
    </div>
  </div>
</template>

<style scoped>
/* 전체 컨테이너 */
.attraction-container {
  display: flex;
  width: 100%;
  height: 100%;
  min-height: 660px;
  /* 창 높이를 고정 */
  background-color: #f9f9f9;
  /* 배경색 추가 */
  border: 1px solid #ddd;
  border-radius: 10px;
  overflow: hidden;
  /* 넘치는 내용 제거 */
}

/* 왼쪽 영역 */
.attraction-left {
  flex: 3;
  /* 3:7 비율 */
  padding: 20px;
  background-color: #ffffff;
  /* 깔끔한 흰 배경 */
  font-family: Arial, sans-serif;
  display: flex;
  flex-direction: column;
  gap: 15px;
  /* 항목 간 간격 */
}

.attraction-left h1 {
  margin-top: 50px;
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.attraction-left p {
  font-size: 16px;
  color: #555;
  margin: 0;
}

.attraction-left img {
  max-width: 100%;
  height: auto;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin-top: 10px;
}

/* 오른쪽 영역 */
.attraction-right {
  flex: 7;
  /* 3:7 비율 */
  background-color: #f4f4f4;
  /* 약간의 대비를 위한 연한 배경색 */
}
</style>
