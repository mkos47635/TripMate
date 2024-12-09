<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { createPlan } from "@/api/plan.js";
import { useAuthStore } from "@/stores/authStore";

const router = useRouter();
const authStore = useAuthStore();

// 상태 변수 정의
const plannerTitle = ref("");
const plannerDescription = ref("");
const plannerStartDate = ref("");
const plannerEndDate = ref("");

// 플랜 생성 함수
function createPlanner() {
    if (!plannerStartDate.value || !plannerEndDate.value) {
        alert("출발 날짜와 도착 날짜를 입력해주세요.");
        return;
    }

    // 날짜 차이 계산
    const startDate = new Date(plannerStartDate.value);
    const endDate = new Date(plannerEndDate.value);
    const timeDifference = endDate - startDate;
    const tourDay = timeDifference / (1000 * 60 * 60 * 24) + 1;



    // 플랜 데이터 준비
    const planner = {
        userId: authStore.loginUserId,
        title: plannerTitle.value,
        description: plannerDescription.value,
        startDate: plannerStartDate.value,
        tourDay: tourDay
    };
    console.log(planner);
    // 플랜 생성 요청
    createPlan(
        planner,
        (response) => {
            if (response.status === 200 || response.status === 201) {
                alert("플랜이 성공적으로 생성되었습니다!");
                // 폼 초기화
                plannerTitle.value = "";
                plannerDescription.value = "";
                plannerStartDate.value = "";
                plannerEndDate.value = "";
                // 필요한 경우 다른 페이지로 이동
                router.replace("/plan/list"); // 예시: 플랜 목록 페이지로 이동
            } else {
                alert("플랜 생성에 실패했습니다.");
            }
        },
        (error) => {
            console.error("플랜 생성 실패:", error);
        }
    );
}
</script>

<template>
    <main>
        <div class="planner-form">
            <h3>플랜 생성</h3>
            <form @submit.prevent="createPlanner">
                <div class="form-group">
                    <label for="plannerTitle">플랜 제목</label>
                    <input id="plannerTitle" v-model="plannerTitle" required />
                </div>
                <div class="form-group">
                    <label for="plannerDescription">플랜 설명</label>
                    <textarea id="plannerDescription" v-model="plannerDescription"></textarea>
                </div>
                <div class="form-group">
                    <label for="plannerStartDate">출발 날짜</label>
                    <input id="plannerStartDate" type="date" v-model="plannerStartDate" required />
                </div>
                <div class="form-group">
                    <label for="plannerEndDate">도착 날짜</label>
                    <input id="plannerEndDate" type="date" v-model="plannerEndDate" required />
                </div>
                <button type="submit">플랜 생성</button>
            </form>
        </div>
    </main>
</template>

<style scoped>
/* 메인 레이아웃 */
main {
  padding: 40px 20px;
  text-align: center;
  font-family: "Arial", sans-serif;
}

/* 플랜 생성 폼 */
.planner-form {
  width: 600px;
  margin: 0 auto;
  padding: 40px;
  background-color: #ffffff;
  border-radius: 25px;
  box-shadow: 0px 6px 16px rgba(0, 0, 0, 0.1);
  text-align: left;
}

.planner-form h3 {
  color: #2a82e4;
  font-size: 1.8em;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20px;
}

/* 폼 그룹 */
.form-group {
  margin-bottom: 20px;
}

.form-group label {
  font-size: 1em;
  font-weight: bold;
  color: #555555;
  margin-bottom: 6px;
  display: block;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 12px 14px;
  font-size: 1em;
  border: 1px solid #dcdcdc;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.form-group input:focus,
.form-group textarea:focus {
  border-color: #2a82e4;
  box-shadow: 0 0 8px rgba(42, 130, 228, 0.4);
  outline: none;
}

textarea {
  min-height: 80px;
  resize: vertical; /* 사용자 크기 조절 가능 */
}

/* 버튼 */
button {
  width: 100%;
  padding: 14px;
  font-size: 1.2em;
  font-weight: bold;
  color: #ffffff;
  background-color: #2a82e4;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

button:hover {
  background-color: #1d6ec1;
  transform: translateY(-2px);
}

button:active {
  background-color: #14589c;
  transform: translateY(0);
}

/* 추가적인 반응형 디자인 */
@media (max-width: 768px) {
  .planner-form {
    padding: 20px;
  }

  .planner-form h3 {
    font-size: 1.5em;
  }

  form-group input,
  form-group textarea {
    font-size: 0.9em;
  }
}
</style>
