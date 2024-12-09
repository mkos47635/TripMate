<script setup>
import { onMounted, watch } from "vue";
import { useMapStore } from "@/stores/mapStore";

const mapStore = useMapStore();

let map; // 지도 객체
let linePath = []; // 선 경로
let polyline; // 폴리라인 객체

const loadKakaoMap = () => {
  const script = document.createElement("script");
  script.src = "https://dapi.kakao.com/v2/maps/sdk.js?appkey=5ed1b358ed0b3e33372d6096097a6a34&autoload=false";
  script.async = true;
  document.head.appendChild(script);

  script.onload = () => {
    kakao.maps.load(() => {
      const mapContainer = document.getElementById("map");
      const mapOption = {
        center: new kakao.maps.LatLng(37.5665, 126.9780), // 초기 지도 중심
        level: 5, // 확대 레벨
      };
      map = new kakao.maps.Map(mapContainer, mapOption);
    });
  };
};

// 마커 및 선 업데이트 함수
const updateMarkersAndPolyline = () => {
  if (!mapStore.markers || mapStore.markers.length === 0 || !map) {
    console.error("markers 데이터가 비어 있습니다.", mapStore.markers);
    return;
  }

  if (polyline) {
    polyline.setMap(null);
  }
  linePath = [];

  mapStore.markers.forEach((item, index) => {
    const { latitude, longitude } = item;
    const position = new kakao.maps.LatLng(latitude, longitude);

    const marker = new kakao.maps.Marker({ position });
    marker.setMap(map);

    // 지도 중심 이동 (첫 번째 마커 기준)
    if (index === 0) {
      map.setCenter(position);
      map.relayout();
    }

    // 마지막 지점을 제외하고 선 경로에 추가
    if (index < mapStore.markers.length - 1) {
      linePath.push(position);
    }
  });

  // 마지막 점을 제외하고 선 연결
  if (mapStore.markers.length > 1) {
    const lastPosition = new kakao.maps.LatLng(
      mapStore.markers[mapStore.markers.length - 1].latitude,
      mapStore.markers[mapStore.markers.length - 1].longitude
    );
    linePath.push(lastPosition);
  }

  polyline = new kakao.maps.Polyline({
    path: linePath,
    strokeWeight: 5,
    strokeColor: "#FF0000",
    strokeOpacity: 0.8,
    strokeStyle: "solid",
  });

  polyline.setMap(map);
};

watch(
  () => mapStore.markers,
  (newMarkers) => {
    console.log("새로운 마커 데이터:", newMarkers);
    updateMarkersAndPolyline();
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
