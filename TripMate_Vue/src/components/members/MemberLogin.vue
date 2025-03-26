<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { login } from "@/api/member";
import { useAuthStore } from "@/stores/authStore";

const router = useRouter();
const authStore = useAuthStore();

const userId = ref("");
const userPwd = ref("");
const rememberId = ref(false);

// 아이디 쿠키 설정 함수
const setCookie = (name, value, days) => {
  const date = new Date();
  date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
  document.cookie = `${name}=${value};expires=${date.toUTCString()};path=/`;
};

// 아이디 쿠키 가져오기 함수
const getCookie = (name) => {
  const cookies = document.cookie.split("; ");
  for (let i = 0; i < cookies.length; i++) {
    const [key, value] = cookies[i].split("=");
    if (key === name) {
      return value;
    }
  }
  return null;
};
// 컴포넌트 로드시 rememberId 쿠키값 확인
if (getCookie("rememberId")) {
  userId.value = getCookie("rememberId"); // 쿠키값을 userId에 설정
  rememberId.value = true; // 체크 상태 유지
}
const handleLogin = () => {
  const loginData = {
    userId: userId.value,
    userPwd: userPwd.value,
  };

  login(
    loginData,
    (response) => {
      const accessToken = response.data.accessToken; // 응답 본문에서 액세스 토큰 추출
      if (accessToken) {
        authStore.setLoginInfo(accessToken, loginData.userId); // Pinia 또는 LocalStorage에 저장
        if (rememberId.value) {
          setCookie("rememberId", userId.value, 7); // 7일 동안 유지
        } else {
          setCookie("rememberId", "", -1); // 체크 해제 시 쿠키 삭제
        }
        // 로그인 성공 후 리다이렉트 경로 확인
        const redirectPath = router.currentRoute.value.query.redirect || "/";
        router.push(redirectPath);
      } else {
        console.error("AccessToken이 응답에 포함되어 있지 않습니다.");
      }
    },
    (error) => {
      console.error("로그인 실패:", error);
      alert("로그인 실패: 아이디 또는 비밀번호를 확인해주세요.");
    }
  );
};


</script>
<template>
  <main class="container">
    <section class="header">
      <h2>TripMate</h2>
      <p>로그인 후 맞춤형 여행 서비스를 즐겨보세요!</p>
    </section>

    <section class="login-form">
      <h4>로그인</h4>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="userId">아이디</label>
          <input
            type="text"
            id="userId"
            v-model="userId"
            placeholder="아이디를 입력하세요"
            required
          />
        </div>
        <div class="form-group">
          <label for="userPwd">비밀번호</label>
          <input
            type="password"
            id="userPwd"
            v-model="userPwd"
            placeholder="비밀번호를 입력하세요"
            required
          />
        </div>
        <div class="form-group remember-me">
          <label>
            <input type="checkbox" id="rememberId" v-model="rememberId" />
            아이디 저장
          </label>
        </div>
        <button type="submit" class="submit-btn">로그인</button>
      </form>

      <div class="login-links">
        <a href="#">아이디 찾기</a>
        <a href="#">비밀번호 찾기</a>
        <router-link to="/user/join">회원가입</router-link>
      </div>
    </section>
  </main>
</template>


<style scoped>
/* 메인 컨테이너 */
.container {
  width: 500px;
  margin-top: 80px;
  padding: 80px 60px;
  background: #ffffff;
  border-radius: 25px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
}

/* 헤더 */
.header h2 {
  font-size: 1.8em;
  color: #2ec4ff;
  margin-bottom: 8px;
  font-weight: bold;
}

.header p {
  font-size: 1em;
  color: #777;
  margin-bottom: 20px;
}

/* 로그인 폼 */
.login-form h4 {
  font-size: 1.4em;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
  text-align: left;
}

.form-group label {
  font-size: 0.9em;
  font-weight: bold;
  color: #555;
  margin-bottom: 5px;
  display: block;
}

.form-group input {
  width: 100%;
  padding: 10px;
  font-size: 1em;
  border: 1px solid #ddd;
  border-radius: 8px;
  transition: 0.3s ease;
}

.form-group input:focus {
  border-color: #2ec4ff;
  box-shadow: 0 0 8px rgba(46, 196, 255, 0.3);
  outline: none;
}

/* 체크박스 */
.remember-me {
  display: flex;
  align-items: center;
  gap: 8px;
}

.remember-me input {
  width: auto;
  accent-color: #2ec4ff;
}

.remember-me label {
  font-size: 0.9em;
  color: #555;
  cursor: pointer;
}

/* 버튼 */
.submit-btn {
  width: 100%;
  padding: 12px;
  font-size: 1.1em;
  font-weight: bold;
  background-color: #2ec4ff;
  border: none;
  border-radius: 8px;
  color: #fff;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.submit-btn:hover {
  background-color: #1d9cd7;
  transform: translateY(-2px);
}

.submit-btn:active {
  background-color: #1478a6;
  transform: translateY(0);
}

/* 로그인 링크 */
.login-links {
  margin-top: 20px;
  font-size: 0.9em;
  color: #2ec4ff;
}

.login-links a {
  color: #2ec4ff;
  text-decoration: none;
  margin: 0 10px;
  transition: color 0.3s ease;
}

.login-links a:hover {
  color: #1d9cd7;
}
</style>
