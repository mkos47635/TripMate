import axios from "axios";
import { useAuthStore } from "@/stores/authStore";

const { VITE_SERVER_API_URL, VITE_SEARCH_TRIP_URL } = import.meta.env;

// 요청 인터셉터 함수
function attachAuthorizationHeader(config) {
  const authStore = useAuthStore(); // authStore 가져오기
  const accessToken = authStore.accessToken; // accessToken 가져오기

  // accessToken이 있으면 Authorization 헤더에 추가
  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
    console.log("Authorization Header:", config.headers.Authorization);
  }
  return config;
}

// Axios 인스턴스 생성 함수
function createAxiosInstance(
  baseUrl,
  useInterceptors = true,
  withCredentials = false
) {
  const instance = axios.create({
    baseURL: baseUrl,
    headers: {
      "Content-Type": "application/json",
    },
    withCredentials, // 동적으로 설정
  });

  if (useInterceptors) {
    // 요청 인터셉터 등록
    instance.interceptors.request.use(attachAuthorizationHeader, (error) =>
      Promise.reject(error)
    );

    // 응답 인터셉터 등록
    instance.interceptors.response.use(
      (response) => response, // 성공적인 응답은 그대로 반환
      async (error) => {
        const authStore = useAuthStore(); // authStore 인스턴스 가져오기

        if (error.response.status === 401) {
          console.log("Access token expired, attempting to refresh...");

          try {
            const newAccessToken = await authStore.refreshToken();
            error.config.headers.Authorization = `Bearer ${newAccessToken}`;
            console.log("Retrying with new access token:", newAccessToken);
            return axios(error.config);
          } catch (refreshError) {
            console.error("Failed to refresh token:", refreshError);
          }
        }
        return Promise.reject(error);
      }
    );
  }

  return instance;
}

// localAxios: Vue 앱의 기본 API를 호출하는 Axios 인스턴스, 인터셉터 활성화, withCredentials 활성화
function localAxios() {
  return createAxiosInstance(VITE_SERVER_API_URL, true, true);
}

// tripAxios: 특정 외부 API를 호출하는 Axios 인스턴스, 인터셉터 비활성화, withCredentials 비활성화
function tripAxios() {
  return createAxiosInstance(VITE_SEARCH_TRIP_URL, false, false);
}

export { localAxios, tripAxios };