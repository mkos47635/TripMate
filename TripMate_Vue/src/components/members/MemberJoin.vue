<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { join, idCheck } from "@/api/member";
import { useAuthStore } from "@/stores/authStore";
const router = useRouter();
const authStore = useAuthStore();

// 상태 변수 정의
const userId = ref("");
const userName = ref("");
const userPwd = ref("");
const emailAdr = ref("");
const emailId = ref("");
const emailDomain = ref("");
const isIdChecked = ref(false); // 아이디 중복 확인 상태 변수
const resultMessage = ref(""); // 아이디 중복 확인 결과 메시지

// 아이디 중복 확인 함수
function checkId() {
  if (!userId.value) {
    resultMessage.value = "아이디를 입력해주세요.";
    return;
  }

  idCheck(
    userId.value,
    (response) => {
      if (response.data === 1) {
        resultMessage.value = "이미 사용 중인 아이디입니다.";
        isIdChecked.value = false;
      } else if (response.data === 0) {
        resultMessage.value = "사용 가능한 아이디입니다.";
        isIdChecked.value = true;
      } else {
        resultMessage.value = "알 수 없는 응답입니다. 다시 시도해주세요.";
        isIdChecked.value = false;
      }
    },
    (error) => {
      console.error("아이디 중복 확인 오류:", error);
      resultMessage.value = "아이디 중복 확인 중 오류가 발생했습니다.";
      isIdChecked.value = false;
    }
  );
}

// 이메일을 ID와 도메인으로 분리하는 함수
function splitEmail() {
  if (emailAdr.value.includes("@")) {
    const [id, domain] = emailAdr.value.split("@");
    emailId.value = id;
    emailDomain.value = domain;
  } else {
    emailId.value = "";
    emailDomain.value = "";
    alert("올바른 이메일 주소를 입력해주세요.");
  }
}

// 회원가입 함수
function signup() {
  if (!isIdChecked.value) {
    alert("아이디 중복 확인을 완료해주세요.");
    return;
  }

  // 사용자 입력값 준비
  const memberDto = {
    userId: userId.value,
    userName: userName.value,
    userPwd: userPwd.value,
    emailId: emailId.value,
    emailDomain: emailDomain.value,
  };
  join(
    memberDto,
    (response) => {
      const accessToken = response.data.accessToken;
      // const refreshToken = response.data.refreshToken; // 쿠키나 응답 데이터에서 리프레시 토큰 확인
      if (accessToken) {
        authStore.setLoginInfo(accessToken, memberDto.userId);
        // localStorage.setItem("refreshToken", refreshToken); // LocalStorage에 리프레시 토큰 저장
        alert("회원가입이 완료되었습니다. 로그인 화면으로 이동합니다.");
        router.replace("/");
      } else {
        alert("회원가입 실패...");
      }
    },
    (error) => {
      console.error("회원가입 실패:", error);
      alert("회원가입 중 오류가 발생했습니다. 다시 시도해주세요.");
    }
  );
}
</script>

<template>
  <main>
    <div class="signup-form">
      <h3>회원가입</h3>
      <form @submit.prevent="signup">
        <div class="form-group">
          <label for="userId">아이디</label>
          <div class="input-with-button">
            <input id="userId" v-model="userId" placeholder="아이디를 입력하세요" required />
            <button type="button" id="btn-idcheck" @click="checkId">중복 확인</button>
          </div>
          <div class="result-message">{{ resultMessage }}</div>
        </div>
        <div class="form-group">
          <label for="userName">이름</label>
          <input id="userName" v-model="userName" placeholder="이름을 입력하세요" required />
        </div>
        <div class="form-group">
          <label for="userPwd">비밀번호</label>
          <input id="userPwd" type="password" v-model="userPwd" placeholder="비밀번호를 입력하세요" required />
        </div>
        <div class="form-group">
          <label for="emailAdr">이메일</label>
          <input id="emailAdr" v-model="emailAdr" @blur="splitEmail" placeholder="example@domain.com" required />
        </div>
        <button type="submit" class="submit-btn">회원가입</button>
      </form>
    </div>
  </main>
</template>

<style scoped>
/* 메인 레이아웃 */
main {
  padding: 60px 10px 30px;
  background-color: #e8f5f5;
  text-align: center;
  max-width: 1200px;
  margin: 0 auto;
}

/* 회원가입 폼 */
.signup-form {
  width: 600px;
  margin: 50px auto;
  padding: 40px;
  text-align: left;
  background-color: #ffffff;
  border-radius: 25px;
  box-shadow: 0px 6px 12px rgba(0, 0, 0, 0.1);
}

.signup-form h3 {
  color: #2ec4ff;
  font-size: 1.8em;
  margin-bottom: 30px;
  text-align: center;
}

/* 폼 그룹 */
.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-size: 1em;
  font-weight: bold;
  color: #555;
  margin-bottom: 8px;
}

.form-group input {
  width: 100%;
  padding: 10px;
  font-size: 1em;
  border: 1px solid #ccc;
  border-radius: 8px;
  transition: border-color 0.3s ease-in-out;
}

.form-group input:focus {
  border-color: #2ec4ff;
  outline: none;
  box-shadow: 0 0 5px rgba(46, 196, 255, 0.4);
}

/* 아이디 중복 확인 버튼과 입력 필드 정렬 */
.input-with-button {
  display: flex;
  gap: 10px;
}

.input-with-button input {
  flex: 1;
}

.input-with-button button {
  width: 120px;
  padding: 10px;
  background-color: #2ec4ff;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1em;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.input-with-button button:hover {
  background-color: #1d9cd7;
}

/* 결과 메시지 */
.result-message {
  margin-top: 5px;
  font-size: 0.9em;
  color: #ff5757; /* 기본적으로 오류 메시지 색상 */
}

.result-message:empty {
  display: none; /* 메시지가 없으면 숨기기 */
}

/* 성공 메시지 */
.result-message.success {
  color: #2ec4ff;
}

/* 버튼 */
.submit-btn {
  width: 100%;
  padding: 15px;
  background-color: #2ec4ff;
  border: none;
  border-radius: 8px;
  color: white;
  font-size: 1.2em;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.submit-btn:hover {
  background-color: #1d9cd7;
}

.submit-btn:active {
  background-color: #1478a6;
}
</style>
