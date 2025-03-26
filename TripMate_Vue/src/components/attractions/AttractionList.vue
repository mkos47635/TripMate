<script setup>
import { ref, watch } from "vue";
import AttractionListItem from "@/components/attractions/item/AttractionListItem.vue";
import { searchAttraction } from "@/api/attraction";

const props = defineProps({
  searchConditions: Object,
});

const attractions = ref([]);

// search 함수 정의
const search = () => {
  console.log("검색 조건:", props.searchConditions);
  searchAttraction(
    props.searchConditions,
    (response) => {
      console.log("검색 결과:", response.data);
      attractions.value = response.data;
    },
    (error) => {
      console.error("검색 요청 실패:", error);
    }
  );
};

// watch를 search 함수 정의 이후에 설정
watch(
  () => props.searchConditions, // 검색 조건이 변경될 때 트리거
  (newConditions) => {
    console.log("검색 조건 변경:", newConditions);
    search(); // 새로운 검색 조건으로 search 실행
  },
  { immediate: true } // 초기 로드 시에도 실행
);

// 아이템 클릭 핸들러
const handleItemClick = (attraction) => {

  // 새 창 크기
  const width = 1200;
  const height = 660;

  // 화면 크기
  const screenLeft = window.screenLeft || window.screenX;
  const screenTop = window.screenTop || window.screenY;
  const screenWidth = window.innerWidth || document.documentElement.clientWidth || screen.width;
  const screenHeight = window.innerHeight || document.documentElement.clientHeight || screen.height;

  // 중앙 위치 계산
  const left = screenLeft + (screenWidth - width) / 2 - 9;
  const top = screenTop + (screenHeight - height) / 2 + 80;

  // URL 생성
  const url = `${window.location.origin}/attraction/detail?no=${attraction.no}&title=${encodeURIComponent(
    attraction.title
  )}&addr1=${encodeURIComponent(attraction.addr1)}&addr2=${encodeURIComponent(
    attraction.addr2
  )}&firstImage1=${encodeURIComponent(
    attraction.firstImage1
  )}&latitude=${encodeURIComponent(
    attraction.latitude
  )}&longitude=${encodeURIComponent(attraction.longitude)}`;

  // 새 창 열기
  window.open(url, "_blank", `width=${width},height=${height},top=${top},left=${left}`);
};
</script>

<template>
  <div class="attraction-list-grid">
    <AttractionListItem
      v-for="attraction in attractions"
      :key="attraction.no"
      :no="attraction.no"
      :title="attraction.title"
      :addr1="attraction.addr1"
      :firstImage1="attraction.firstImage1"
      @click="handleItemClick(attraction)"
    />
  </div>
</template>

<style scoped>
.attraction-list-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 한 줄에 3개 */
  width: 100%;
  max-width: 800px;
  justify-items: start; /* 그리드 아이템을 왼쪽 정렬 */
  align-items: start; /* 그리드 아이템을 상단에 배치 */
}
</style>
