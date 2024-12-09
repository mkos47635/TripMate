<script setup>
import { useAuthStore } from "@/stores/authStore";
import { storeToRefs } from "pinia";
const authStore = useAuthStore();
const { loginUserId } = storeToRefs(authStore);

const handleLogout = async () => {
  try {
    await authStore.logout(); // Pinia과 로그아웃 처리
    alert("로그아웃되었습니다.");
    // 로그아웃 후 메인 페이지로 이동
    window.location.href = "/";
  } catch (error) {
    console.error("로그아웃 처리 중 오류:", error);
  }
};
</script>

<template>
  <header>
    <div class="nav-container">
      <div class="nav-container-left">
        <div class="left-nav">
          <router-link to="/">
            <img src="/logo.png" width="80px" height="50px" alt="logo">
          </router-link>
          
          <router-link to="/attraction">여행지찾기</router-link>
          <router-link to="/ai">여행추천</router-link>
          <router-link to="/board">게시판</router-link>
          <router-link to="/plan/create">플래너 만들기</router-link>
          <router-link to="/plan">My Planer</router-link>
        </div>
      </div>
      <div class="nav-container-right">
        <div class="right-nav">
          <div class="profile-container">
            <img src="/profile.png" class="profile" width="50px" height="50px" />
            <div class="dropdown-menu">
              <!-- 회원가입 및 로그인 버튼 -->
              <router-link to="/user/join" v-show="!loginUserId" class="dropdown-button">회원가입</router-link>
              <router-link to="/user/login" v-show="!loginUserId" class="dropdown-button">로그인</router-link>
              <!-- 로그아웃 버튼 -->
              <span @click="handleLogout" v-show="loginUserId" class="dropdown-link">로그아웃</span>
              <router-link to="/user/myPage" v-show="loginUserId" class="dropdown-link">마이 페이지</router-link>


            </div>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<style scoped>
header {
  position: fixed;
  top: 0;
  padding: 20px 0;
  background-color: #E8F5F5;
  text-align: center;
  width: 100%;
  /* 전체 화면 너비 */
  max-width: 1200px;
  /* 최대 너비 설정 */
  margin: 0 auto;
  /* 가운데 정렬 */
}

.nav-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  /* 부모의 너비에 맞게 확장 */
}


.nav-container-left {
  display: flex;
  align-items: center;
  background-color: white;
  padding: 5px 10px;
  border-radius: 35px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  /* 왼쪽 네비에 그림자 추가 */
}

.nav-container-right {
  display: flex;
  align-items: center;
  background-color: white;
  padding: 10px 10px 5px;
  border-radius: 45px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  /* 오른쪽 네비에 그림자 추가 */
}

.left-nav a {
  color: #0c274e;
  text-decoration: none;
  margin: 0 15px;
  padding: 10px 10px;
  border-radius: 30px;
  font-weight: bold;
}

.left-nav a:hover,
.right-nav a:hover {
  background-color: #f2f2f2;
  color: rgb(101, 173, 255);
}

.left-nav {
  display: flex;
  align-items: center;
}

.left-nav img {
  border-radius: 45px;
  width: 120px;
  height: 40px;
}

.left-nav img:hover {
  background-color: #f2f2f2;
}

.profile-container {
  position: relative;
}

.profile {
  border: 1px solid #b4adad;
  border-radius: 50%;
  padding: 1px;
  cursor: pointer;
}

.nav-container-right:hover {
  background-color: #F2F2F2;
}

.dropdown-menu {
  display: none;
  position: absolute;
  top: 60px;
  right: 0;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 150px;
  padding: 10px 0;
}

.dropdown-menu a {
  color: #333;
  text-decoration: none;
  padding: 8px 15px;
  display: block;
  text-align: center;
}

.dropdown-menu a:hover {
  background-color: #f2f2f2;
  color: rgb(101, 173, 255);
  margin: 0 10px;
  border-radius: 8px;
}

.dropdown-link {
  color: #333;
  /* 텍스트 색상 */
  text-decoration: none;
  /* 밑줄 제거 */
  padding: 8px 15px;
  /* 내부 여백 */
  display: block;
  /* 블록 형태 */
  text-align: center;
  /* 텍스트 가운데 정렬 */
  border-radius: 8px;
  /* 둥근 모서리 */
  font-weight: bold;
  /* 텍스트 굵게 */
  cursor: pointer;
  /* 클릭 가능 마우스 포인터 */
  margin: 0 10px;
  transition: background-color 0.3s ease, color 0.3s ease;
  /* 호버 효과 부드럽게 */
}

.dropdown-link:hover {
  background-color: #f2f2f2;
  /* 배경색 변경 */
  color: rgb(101, 173, 255);
  /* 텍스트 색상 변경 */
}


.nav-container-right:hover .dropdown-menu {
  display: block;
}
</style>
