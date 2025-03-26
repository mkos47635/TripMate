<template>
    <div class="container">
      <!-- 왼쪽: 검색 폼 -->
      <div class="search-section">
        <h2>여행 일정 생성</h2>
        <form @submit.prevent="submitForm">
          <div class="form-group">
            <label for="destination">여행지:</label>
            <input v-model="form.destination" id="destination" required />
          </div>
          <div class="form-group">
            <label for="days">여행 기간 (일):</label>
            <input v-model="form.days" id="days" type="number" required />
          </div>
          <div class="form-group">
            <label for="purpose">여행 목적:</label>
            <select v-model="form.purpose" id="purpose">
              <option value="관광">관광</option>
              <option value="휴식">휴식</option>
              <option value="액티비티">액티비티</option>
            </select>
          </div>
          <div class="form-group">
            <label for="budget">예산 (원):</label>
            <input v-model="form.budget" id="budget" required />
          </div>
          <div class="form-group">
            <label for="timeFrame">여행 시간대:</label>
            <input v-model="form.timeFrame" id="timeFrame" required />
          </div>
          <button type="submit" class="btn-submit">일정 생성</button>
        </form>
      </div>
  
      <!-- 오른쪽: 결과 -->
      <div class="result-section">
        <h3>추천 일정</h3>
        <div v-if="itinerary" class="itinerary-box">
          <pre>{{ itinerary }}</pre>
        </div>
        <div v-else>
          <p>일정을 생성하고 결과를 확인하세요!</p>
        </div>
      </div>
    </div>
  </template>
  
    
  <script>
  import { generateItinerary } from "@/api/itinerary";
  
  export default {
    data() {
      return {
        form: {
          destination: "",
          days: 1,
          purpose: "관광",
          budget: "30만 원",
          timeFrame: "오전 9시 ~ 오후 7시",
        },
        itinerary: null,
      };
    },
    methods: {
      async submitForm() {
        try {
          const pureForm = JSON.parse(JSON.stringify(this.form));
          console.log("전송 데이터 (순수 객체):", pureForm);
  
          // 백엔드 API 호출
          const result = await generateItinerary(pureForm);
          this.itinerary = result; // 응답 데이터 설정
        } catch (error) {
          console.error("일정 생성 중 오류 발생:", error);
        }
      },
    },
  };
  </script>
  
    
  <style scoped>
  /* 전체 컨테이너 레이아웃 */
  .container {
    display: flex;
    flex-direction: row;
    gap: 20px; /* 왼쪽과 오른쪽 간격 */
    width: 1200px; /* 고정 너비 */
    height: 700px; /* 고정 높이 */
    margin-top: 70px;
    background-color: #f9f9f9;
    border-radius: 10px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    overflow: hidden; /* 자식 요소가 넘치지 않도록 */
  }
  
  /* 검색 섹션 스타일 */
  .search-section {
    flex: 1; /* 비율 1 */
    padding: 20px;
    background-color: #ffffff;
    border-radius: 10px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    overflow-y: auto; /* 내용이 넘칠 경우 스크롤 생성 */
  }
  
  .search-section h2 {
    font-size: 24px;
    margin-bottom: 20px;
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  .form-group label {
    display: block;
    font-size: 14px;
    font-weight: bold;
    margin-bottom: 5px;
  }
  
  .form-group input,
  .form-group select {
    width: 100%;
    padding: 10px;
    font-size: 14px;
    border: 1px solid #ccc;
    border-radius: 5px;
  }
  
  .btn-submit {
    width: 100%;
    padding: 10px;
    font-size: 16px;
    background-color: #007bff;
    color: #fff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    margin-top: 10px;
  }
  
  .btn-submit:hover {
    background-color: #0056b3;
  }
  
  /* 결과 섹션 스타일 */
  .result-section {
    flex: 3; /* 비율 3 */
    padding: 20px;
    background-color: #ffffff;
    border-radius: 10px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    overflow-y: auto; /* 내용이 넘칠 경우 스크롤 생성 */
  }
  
  .result-section h3 {
    font-size: 20px;
    margin-bottom: 15px;
  }
  
  .itinerary-box {
    height: 610px;
    padding: 15px;
    background-color: #f0f8ff;
    border-radius: 5px;
    border: 1px solid #d1e7ff;
    display: block; /* 전체 내용을 하나의 블록으로 처리 */
    white-space: pre-wrap; /* 줄바꿈 유지 및 자동 줄바꿈 */
    word-break: break-word; /* 긴 단어 줄바꿈 */
    overflow-wrap: break-word; /* 최신 브라우저용 줄바꿈 */
    max-width: 100%; /* 컨테이너를 넘지 않도록 */
    box-sizing: border-box; /* 패딩 포함 크기 계산 */
    line-height: 1.6; /* 줄 간격 */
    word-spacing: 1px; /* 단어 간의 간격 */
    overflow-x: auto; /* 좌우로 스크롤 가능하도록 */
  }
  
  .itinerary-box span {
    display: inline-block; /* 텍스트를 블록처럼 나눔 */
    max-width: 100%; /* 부모 크기를 넘지 않도록 제한 */
    word-break: break-word; /* 긴 단어 줄바꿈 */
  }
  
  .result-section p {
    font-size: 14px;
    color: #666;
    word-wrap: break-word; /* 긴 단어 줄바꿈 */
    overflow-wrap: break-word; /* 최신 브라우저용 줄바꿈 */
    white-space: normal; /* 기본 줄바꿈 */
  }
  </style>