<script setup>
import { getGugun } from "@/api/attraction";
import { ref, watch } from "vue";
import AttractionList from "@/components/attractions/AttractionList.vue";

const sidoCode = ref("1");
const gugunCode = ref("0");
const contentId = ref("12");
const keyword = ref("");
const gugunList = ref([]);
const page = ref(1); // 현재 페이지 번호

// 검색 조건 상태
const searchConditions = ref({
  sidoCode: sidoCode.value,
  gugunCode: parseInt(gugunCode.value) + 1,
  contentId: contentId.value,
  keyword: keyword.value,
  offset: (page.value - 1) * 6, // 항상 숫자가 되도록 보장
});

// 구/군 목록 초기화
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

// 컴포넌트가 로드될 때 구/군 목록을 초기화
fetchGugunList();

// 검색 조건 업데이트
const updateSearchConditions = () => {
  searchConditions.value = {
    sidoCode: sidoCode.value,
    gugunCode: parseInt(gugunCode.value) + 1,
    contentId: contentId.value,
    keyword: keyword.value,
    offset: (page.value - 1) * 6,
  };
  console.log("검색 조건 업데이트:", searchConditions.value);
};

// 검색 버튼 클릭
const onClickSearch = () => {
  page.value = 1; // 페이지를 1로 초기화
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

// 검색 조건 변경 시 AttractionList 컴포넌트로 전달
watch(searchConditions, () => {
  console.log("검색 조건 변경:", searchConditions.value);
});
</script>

<template>
  <div class="page-layout">
    <div class="content-container">
      <div class="search-section">
        <h2>검색</h2>
        <div class="search-menu">
          <div class="select-container">
            <label for="sidoCode">시도 선택:</label>
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
          </div>

          <div class="select-container">
            <label for="gugunCode">구군 선택:</label>
            <select id="gugunCode" v-model="gugunCode">
              <option v-for="(gugun, index) in gugunList" :key="index" :value="index">
                {{ gugun }}
              </option>
            </select>
          </div>

          <div class="select-container">
            <label for="contentId">유형 선택:</label>
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

          <div class="input-container">
            <label for="keyword">키워드 검색:</label>
            <input type="text" id="keyword" v-model="keyword" placeholder="검색어를 입력하세요" />
          </div>
          <div class="button-container">
            <button @click="onClickSearch">검색</button>
          </div>
        </div>
      </div>
      <div class="list-section">
        <h2>여행지</h2>
        <!-- AttractionList에 검색 조건 전달 -->
        <div class="attraction-list">
          <div class="containItem">
            <AttractionList :searchConditions="searchConditions" />
          </div>
          
          <div class="inline-buttons">
            <button @click="onBeforeSearch">이전</button>
            <button @click="onAfterSearch">다음</button>
          </div>
        </div>
        
      </div>
    </div>
  </div>
</template>


<style scoped>
.page-layout {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 1200px;
}

h2 {
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  padding-left: 10px;
  padding-right: 10px;
  padding-bottom: 10px;
}

.content-container {
  display: grid;
  grid-template-columns: 2fr 5fr;
  gap: 20px;
  width: 100%;
  padding: 100px 0px;
  box-sizing: border-box;
  margin-top: 30px;
  height: 900px;
}

.attraction-list {
  background-color: #f7fdff;
  width: 800px;
  height: 600px;
  border-radius: 10px;
  box-shadow: 0 -1px 8px rgba(0, 0, 0, 0.1);
}

.containItem {
  display: flex;
  justify-content: flex-start; /* 부모 컨테이너의 세로 정렬을 위쪽으로 설정 */
  align-items: flex-start;
  padding-top: 20px;
  padding-left: 20px;
  padding-right: 20px;
}

.search-section,
.list-section {
  background-color: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 -1px 4px rgba(0, 0, 0, 0.1);
}

.search-section h2,
.list-section h2 {
  margin-bottom: 15px;
  font-size: 1.5em;
  text-align: center;
}

.search-menu {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.select-container,
.input-container,
.button-container {
  display: flex;
  flex-direction: column;
  margin-top: 10px;
}

label {
  margin-bottom: 5px;
  font-weight: bold;
}

select,
input[type="text"] {
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}


button {
  padding: 10px;
  border-radius: 5px;
  border: none;
  background-color: #56a4f8;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #4492e6;
}

.inline-buttons {
  display: flex;
  justify-content: center; /* 수평 가운데 정렬 */
  align-items: center; /* 수직 가운데 정렬 */
  gap: 10px; /* 버튼 간 간격 */
}


.inline-buttons button {
  padding-top: 8px;
  border-radius: 5px;
  background-color: #56a4f8;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
  text-align: center;
  height: 35px;
  width: 53px;
}

.inline-buttons button:hover {
  background-color: #4492e6;
}


</style>
