<script setup>
import { usePlanStore } from "@/stores/planStore";
import { computed, onMounted } from "vue";
import Sortable from "sortablejs";
import { savePlanDetails, deletePlanDetails } from "@/api/plan.js";

const planStore = usePlanStore();

// 선택된 Day 값에 따라 items 필터링
const filteredItems = computed(() =>
  planStore.items
    .filter((item) => parseInt(item.day) === parseInt(planStore.selectDay))
    .sort((a, b) => a.planIndex - b.planIndex) // planIndex 기준으로 정렬
);

// DOM이 렌더링된 후 Sortable 초기화
onMounted(() => {
  const sortable = new Sortable(document.querySelector("ul"), {
    animation: 150,
    onEnd: (event) => {
      const { oldIndex, newIndex } = event;

      if (oldIndex !== undefined && newIndex !== undefined) {
        const movedItem = filteredItems.value[oldIndex];

        const originalIndex = planStore.items.findIndex(
          (item) =>
            item.contentId === movedItem.contentId && item.day === movedItem.day
        );

        planStore.items.splice(originalIndex, 1);
        planStore.items.splice(newIndex, 0, movedItem);

        planStore.items
          .filter((item) => parseInt(item.day) === parseInt(planStore.selectDay))
          .forEach((item, idx) => {
            item.planIndex = idx;
          });

        console.log("Updated filtered items:", filteredItems.value);
      }
    },
  });
});

// 삭제 기능 추가
const deleteItem = (item) => {
  // 클라이언트 측에서 삭제
  planStore.items = planStore.items.filter(
    (currentItem) =>
      currentItem.contentId !== item.contentId || parseInt(currentItem.day) !== parseInt(item.day)
  );
  console.log("Updated items:", planStore.items);

  // 서버에 삭제 요청
  deletePlanDetails(
    item, // 객체로 전달
    ({ data }) => {
      console.log("삭제 성공:", data);
    },
    (error) => {
      console.error("삭제 실패:", error);
    }
  );
};

// 저장 기능 추가
const saveClick = () => {
  filteredItems.value.forEach((item, idx) => {
    const detail = {
      planId: planStore.planInfo.planId,
      planIndex: idx,
      day: item.day,
      title: item.title,
      contentId: item.contentId,
      addr1: item.addr1,
      addr2: item.addr2,
      latitude: item.latitude,
      longitude: item.longitude,
    };
    console.log(item.latitude);
    console.log(item.latitude);
    console.log(item.latitude);
    saveItems(detail);
  });
};

const saveItems = (detail) => {
  savePlanDetails(
    detail,
    ({ data }) => {
      console.log("응답 데이터:", data);
    },
    (error) => {
      console.error("API 요청 실패:", error);
    }
  );
};
</script>

<template>
  <div>
    <div class="list">
      <ul>
        <li v-if="filteredItems.length === 0">일정을 추가해주세요.</li>
        <li v-for="(item, index) in filteredItems" :key="item.contentId" class="list-item">
          <div class="text-container">
            <span class="title">. {{ item.title }}</span>
            <span class="address">{{ item.addr1 }}</span>
          </div>
          <button class="delete-button" @click="deleteItem(item)">-</button>
        </li>
      </ul>
    </div>
    <div class="area">
      <button class="search-button" @click="saveClick">저장</button>
    </div>
  </div>
</template>






<style scoped>
/* 기존 스타일 유지 */
h1 {
  font-size: 24px;
  margin-bottom: 10px;
}

p {
  font-size: 18px;
  margin: 5px 0;
}

ul {
  padding: 0;
  list-style-type: none;
}

li {
  margin: 5px 0;
  padding: 10px;
  background-color: #f1f9ff;
  border: 1px solid #ddd;
  border-radius: 8px;
  cursor: grab;
  height: 70px;
  display: flex; /* 플렉스박스 활성화 */
  align-items: center; /* 세로 중앙 정렬 */
  justify-content: flex-start; /* 가로 정렬 (필요에 따라 변경 가능) */
}

.list {
  overflow: auto;
  height: 500px;
}

.list-item {
  display: flex; /* 가로 정렬 */
  justify-content: space-between; /* 양쪽 정렬 */
  align-items: center; /* 세로 중앙 정렬 */
  padding: 10px;
  background-color: #f1f9ff;
  border: 1px solid #ddd;
  border-radius: 8px;
  margin: 5px 0;
  height: 75px;
  cursor: grab;
  position: relative; /* 삭제 버튼 위치 지정용 */
}

.text-container {
  flex: 1; /* 텍스트 영역이 버튼을 제외한 공간을 차지 */
  text-align: left; /* 텍스트를 왼쪽 정렬 */
}

.title {
  display: block; /* 제목을 독립적인 블록으로 표시 */
  font-size: 16px; /* 제목 크기 */
  font-weight: bold; /* 제목 강조 */
  text-overflow: ellipsis; /* 넘치는 텍스트에 ... 표시 */
  white-space: nowrap; /* 텍스트를 한 줄로 표시 */
  overflow: hidden; /* 넘치는 텍스트 숨김 */
}

.address {
  display: block; /* 주소를 독립적인 블록으로 표시 */
  font-size: 14px; /* 주소 크기 */
  color: #555; /* 주소 색상 */
  text-overflow: ellipsis; /* 넘치는 텍스트에 ... 표시 */
  white-space: nowrap; /* 텍스트를 한 줄로 표시 */
  overflow: hidden; /* 넘치는 텍스트 숨김 */
  margin-top: 10px; /* 제목과 간격 추가 */
}

/* 검색 버튼 스타일 */
.search-button {
  display: block;
  margin: 8px auto; /* 가운데 정렬 */
  padding: 10px 20px; /* 버튼 안의 여백 */
  font-size: 16px;
  font-weight: bold;
  color: white;
  background-color: #007bff; /* 기본 색상 */
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* 그림자 추가 */
}

.search-button:hover {
  background-color: #0056b3; /* 호버 시 색상 변경 */
  transform: scale(1.05); /* 살짝 확대 효과 */
}

.search-button:active {
  background-color: #003f8c; /* 클릭 시 색상 변경 */
  transform: scale(0.98); /* 살짝 축소 효과 */
}

.delete-button {
  flex-shrink: 0; /* 버튼 크기 유지 */
  position: relative;
  background-color: #ff7a7c; /* 빨간색 */
  color: white; /* 텍스트 색상 */
  border: none; /* 테두리 제거 */
  border-radius: 5px; /* 둥근 모서리 */
  padding: 5px 10px; /* 내부 여백 */
  font-size: 14px; /* 텍스트 크기 */
  cursor: pointer; /* 커서 변경 */
  transition: background-color 0.3s ease; /* 호버 효과 */
}

.delete-button:hover {
  background-color: #ff515a; /* 호버 시 색상 */
}

.delete-button:active {
  background-color: #ff6169; /* 클릭 시 색상 */
}
</style>