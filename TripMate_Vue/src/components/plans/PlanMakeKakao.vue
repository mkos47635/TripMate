<script setup>
import { onMounted, watch } from "vue";
import { useMapStore } from "@/stores/mapStore";

const mapStore = useMapStore();

let map; // 지도 객체를 전역 변수로 선언

const loadKakaoMap = () => {
  const script = document.createElement("script");
  script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=5ed1b358ed0b3e33372d6096097a6a34&autoload=false`;
  script.async = true;
  document.head.appendChild(script);

  script.onload = () => {
    kakao.maps.load(() => {
      const mapContainer = document.getElementById("map");
      const mapOption = {
        center: new kakao.maps.LatLng(37.5665, 126.9780), // 초기 지도 중심
        level: 3, // 확대 레벨
      };
      map = new kakao.maps.Map(mapContainer, mapOption);
      updateMarker(mapStore.latitude, mapStore.longitude); // 초기 마커 설정
    });
  };
};

// 지도에 마커 업데이트 함수
const updateMarker = (latitude, longitude) => {
  if (!latitude || !longitude || !map) return;

  const markerPosition = new kakao.maps.LatLng(latitude, longitude);
  const marker = new kakao.maps.Marker({
    position: markerPosition,
  });
  marker.setMap(map);

  // 지도 중심 이동
  map.setCenter(markerPosition);
};

// 위치 변경 감지 및 업데이트
watch(
  () => [mapStore.latitude, mapStore.longitude],
  ([latitude, longitude]) => {
    updateMarker(latitude, longitude);
  }
);

onMounted(() => {
  loadKakaoMap();
});
</script>

<template>
  <div id="map" style="width: 100%; height: 100%;"></div>
</template>

<style scoped>
#map {
  width: 100%;
  height: 100%;
  border-radius: 10px;
}
</style>
