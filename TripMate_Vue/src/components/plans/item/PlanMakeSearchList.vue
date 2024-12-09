<script setup>
import { ref, watch } from "vue";
import PlanMakeSearchListItem from "@/components/plans/item/PlanMakeSearchListItem.vue";
import { searchAttraction } from "@/api/attraction"; // 올바른 경로를 확인

const props = defineProps({
  searchConditions: Object,
});

const attractions = ref([]);

// search 함수 정의
const search = () => {
  if (searchAttraction) {
    searchAttraction(
      props.searchConditions,
      (response) => {
        attractions.value = response.data;
      },
      (error) => {
        console.error("검색 요청 실패:", error);
      }
    );
  } else {
    console.error("searchAttraction 함수가 정의되지 않았습니다.");
  }
};

// watch를 search 함수 정의 이후에 설정
watch(
  () => props.searchConditions, // 검색 조건이 변경될 때 트리거
  (newConditions) => {
    search(); // 새로운 검색 조건으로 search 실행
  },
  { immediate: true } // 초기 로드 시에도 실행
);
</script>


<template>
    <div class="attraction-list">
      <div class="attraction-list-grid">
        <PlanMakeSearchListItem
          v-for="attraction in attractions"
          :key="attraction.contentId"
          :contentId="attraction.contentId"
          :title="attraction.title"
          :addr1="attraction.addr1"
          :firstImage1="attraction.firstImage1"
          :latitude="attraction.latitude"
          :longitude="attraction.longitude"
          @click="onAttractionClick(attraction.latitude, attraction.longitude)"
        />
      </div>
    </div>
  </template>

<style scoped>
.attraction-list {
  display: flex;
  justify-content: flex-start; /* 부모 컨테이너의 세로 정렬을 위쪽으로 설정 */
  align-items: flex-start;
  width: 390px;
}

.attraction-list-grid {
  display: grid;
  width: 100%;
  max-width: 800px;
  justify-items: start; /* 그리드 아이템을 왼쪽 정렬 */
  align-items: start; /* 그리드 아이템을 상단에 배치 */
}


</style>
