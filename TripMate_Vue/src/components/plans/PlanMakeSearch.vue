<script setup>
import { getGugun } from "@/api/attraction";
import { ref } from "vue";
import PlanMakeSearchList from "@/components/plans/item/PlanMakeSearchList.vue";

const sidoCode = ref("1");
const gugunCode = ref("0");
const contentId = ref("12");
const keyword = ref("");
const gugunList = ref([]);
const page = ref(1); // 현재 페이지 번호

// 구/군 목록 가져오기
const fetchGugunList = () => {
  gugunCode.value = "0"; // gugunCode 초기화
  getGugun(
    { sidoCode: sidoCode.value },
    (response) => {
      gugunList.value = response.data;
    },
    (error) => {
      console.error("gugunList 불러오기 실패:", error);
    }
  );
};

// 컴포넌트가 로드될 때 초기화
fetchGugunList();

// 검색 조건 설정
const searchConditions = ref({
  sidoCode: sidoCode.value,
  gugunCode: parseInt(gugunCode.value) + 1, // 숫자로 변환
  contentId: contentId.value,
  keyword: keyword.value,
  offset: (page.value - 1) * 6, // 항상 숫자가 되도록 보장
});

// 검색 조건 업데이트
const updateSearchConditions = () => {
  searchConditions.value = {
    sidoCode: sidoCode.value,
    gugunCode: parseInt(gugunCode.value) + 1,
    contentId: contentId.value,
    keyword: keyword.value,
    offset: (page.value - 1) * 6,
  };
};

// 검색 버튼 클릭
const onClickSearch = () => {
  page.value = 1; // 페이지 초기화
  updateSearchConditions();
};

// 이전 페이지로 이동
const onBeforeSearch = () => {
  if (page.value > 1) {
    page.value -= 1;
    updateSearchConditions();
  }
};

// 다음 페이지로 이동
const onAfterSearch = () => {
  page.value += 1;
  updateSearchConditions();
};
</script>



<template>
  <div class="search-menu">
    <h2>검색</h2>
    <div class="inline-selects">
      <select id="sidoCode" v-model="sidoCode" @change="fetchGugunList">
        <option value="1">서울</option>
        <option value="2">인천</option>
        <option value="3">대전</option>
        <option value="4">대구</option>
        <option value="5">광주</option>
        <option value="6">부산</option>
        <option value="7">울산</option>
        <option value="8">세종특별자치시</option>
        <option value="31">경기도</option>
        <option value="32">강원특별자치도</option>
        <option value="33">충청북도</option>
        <option value="34">충청남도</option>
        <option value="35">경상북도</option>
        <option value="36">경상남도</option>
        <option value="37">전북특별자치도</option>
        <option value="38">전라남도</option>
        <option value="39">제주도</option>
      </select>

      <select id="gugunCode" v-model="gugunCode">
        <option v-for="(gugun, index) in gugunList" :key="index" :value="index">
          {{ gugun }}
        </option>
      </select>

      <select id="contentId" v-model="contentId">
        <option value="12">관광지</option>
        <option value="14">문화시설</option>
        <option value="15">축제공연행사</option>
        <option value="25">여행코스</option>
        <option value="28">레포츠</option>
        <option value="32">숙박</option>
        <option value="38">쇼핑</option>
        <option value="39">음식점</option>
      </select>
    </div>

    <div class="inline-input-button">
      <input type="text" id="keyword" v-model="keyword" placeholder="검색어를 입력하세요" />
      <button @click="onClickSearch">검색</button>
    </div>
    <PlanMakeSearchList :searchConditions="searchConditions" />
  </div>
  <div class="inline-buttons">
      <button @click="onBeforeSearch">이전</button>
      <button @click="onAfterSearch">다음</button>
    </div>
</template>

  

<style scoped>
.search-menu {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

h2 {
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  padding-top: 7px;
  padding-left: 10px;
  padding-right: 10px;
  padding-bottom: 7px;
  text-align: center;
}

.inline-selects {
  display: flex;
  gap: 5px; /* 선택 요소 간 간격 */
}

.inline-selects select {
  flex: 1; /* 동일한 비율로 크기를 분배 */
  width: 0; /* 기본 너비를 제거하여 flex 비율에만 의존 */
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  font-size: 14px;
  text-align: center;
}

.inline-input-button {
  display: flex;
  gap: 10px;
  align-items: center;
}

.inline-input-button input {
  flex: 5; /* 5:1 비율 */
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  font-size: 14px;
}

.inline-input-button button {
  flex: 1; /* 5:1 비율 */
  padding: 10px;
  border-radius: 5px;
  border: none;
  background-color: #69a6e7;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

.inline-input-button button:hover {
  background-color: #4b98eb;
}

.inline-buttons {
  display: flex;
  gap: 10px; /* 버튼 간 간격 */
}

.inline-buttons button {
  flex: 1; /* 동일한 비율 */
  padding: 10px;
  margin-top: 8px;
  border-radius: 5px;
  border: none;
  background-color: #56a4f8;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
  text-align: center;
}

.inline-buttons button:hover {
  background-color: #4492e6;
}
</style>