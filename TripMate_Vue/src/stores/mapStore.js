import { defineStore } from "pinia";
import { ref } from "vue";

export const useMapStore = defineStore(
  "map",
  () => {

    const latitude = ref("");
        const longitude = ref("");

        const setPosition = (la, lo) => {
            latitude.value = la;
            longitude.value = lo;
    }
    
    // 상태
    const markers = ref([]);

    // 마커 초기화
      const setMarkers = (data) => {
      markers.value = data; // 기존 ref의 value를 업데이트
    };

    return {
      latitude,
      longitude,
      setPosition,
      markers,
      setMarkers,
    };
  }
);