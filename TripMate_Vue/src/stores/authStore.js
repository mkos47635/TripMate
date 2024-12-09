import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "axios";
import router from "@/router";
export const useAuthStore = defineStore(
  "auth",
  () => {
    const accessToken = ref(null);
    const loginUserId = ref("");

    const setLoginInfo = (token, id) => {
      accessToken.value = token;
      loginUserId.value = id;
    };

    const logout = async () => {
      try {
        // 로그아웃 요청만 처리하는 새로운 Axios 인스턴스 생성
        const instance = axios.create({
          baseURL: "http://localhost", // API 서버 주소
          headers: {
            "Content-Type": "application/json",
          },
          withCredentials: true, // HttpOnly 쿠키 포함 설정
        });

        await instance.post("/user/logout"); // 로그아웃 요청
      } catch (error) {
        console.error("로그아웃 실패:", error);
      } finally {
        accessToken.value = null;
        loginUserId.value = "";
        router.push({ name: "user-login" }); // 로그인 페이지로 리다이렉트
      }
    };

    // 토큰 갱신 메서드 (새로운 Axios 인스턴스 사용)
    const refreshToken = async () => {
      try {
        // 새로운 Axios 인스턴스 생성
        const instance = axios.create({
          baseURL: "http://localhost", // API 서버 주소
          headers: {
            "Content-Type": "application/json",
          },
          withCredentials: true, // HttpOnly 쿠키 포함 설정
        });
        console.log("리프레쉬 직전!!");
        // 서버에 리프레시 요청
        const response = await instance.post("/user/refresh");

        // 새 액세스 토큰 저장
        accessToken.value = response.data.accessToken;
        console.log("New access token received:", accessToken.value);

        return accessToken.value; // 새 토큰 반환
      } catch (error) {
        if (error.response && error.response.status === 403) {
          console.warn("Refresh token expired. Logging out...");
          alert("세션이 만료되었습니다. 다시 로그인해주세요.");
          await logout(); // 로그아웃 처리
          return;
        }
        throw error; // 기타 에러는 그대로 전파
      }
    };
    return {
      accessToken,
      loginUserId,
      setLoginInfo,
      logout,
      refreshToken,
    };
  },
  {
    persist: {
      storage: sessionStorage,
    },
  }
);
