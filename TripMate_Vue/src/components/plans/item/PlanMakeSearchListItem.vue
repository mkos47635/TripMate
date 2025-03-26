<script setup>
import { defineProps, defineEmits, computed } from 'vue';
import { useMapStore } from '@/stores/mapStore';
import { usePlanStore } from "@/stores/planStore";

const props = defineProps({
    contentId: String,
    title: String,
    addr1: String,
    addr2: String,
    firstImage1: String,
    latitude: String,
    longitude: String,
});


const emits = defineEmits(['click']);

// Pinia 스토어 가져오기
const mapStore = useMapStore();
const planStore = usePlanStore();

const image = computed(() => {
  return props.firstImage1 ? props.firstImage1 : "/src/assets/img/noImage.png";
});

// 제목 글자 수 제한
const truncatedTitle = computed(() => {
  return props.title.length > 10 ? props.title.slice(0, 15) + "..." : props.title;
});

// 설명 글자 수 제한
const truncatedAddr1 = computed(() => {
  return props.addr1.length > 20 ? props.addr1.slice(0, 20) + "..." : props.addr1;
});

// 항목 클릭 시 Pinia 스토어에 위치 저장
const handleClick = () => {
  mapStore.setPosition(props.latitude, props.longitude);
};

const addClick = () => {
    const addr2 = props.addr2 || "";
    planStore.addItem(planStore.planInfo.planId, props.contentId, props.title, props.addr1, addr2, planStore.selectDay, props.latitude, props.longitude);
    console.log("Items in store:", planStore.items.value);
};

</script>

<template>
    <div class="attraction-item" @click="handleClick"> <!-- 클릭 이벤트 연결 -->
        <div class="attraction-item-image">
            <img :src="image" :alt="image" />
        </div>
        <div class="attraction-item-details">
            <h3 class="attraction-title">{{ truncatedTitle }}</h3>
            <p class="attraction-addr1">{{ truncatedAddr1 }}</p>
        </div>
        <button class="add-button" @click.stop="addClick">+</button>
    </div>
</template>





<style scoped>
.attraction-item {
    display: flex; /* 가로 정렬 */
    align-items: center; /* 세로 중앙 정렬 */
    width: 390px; /* 전체 너비 */
    height: 72px;
    background-color: #fff;
    border-radius: 5px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    padding: 4px;
    transition: transform 0.3s ease;
    cursor: pointer;
    border: 1px solid #d5e4fa;
    margin-bottom: 9px;
    position: relative; /* 추가: 자식 요소(.add-button)의 기준점 */
}


.attraction-item:hover {
    transform: scale(1.01);
}

.attraction-item-image {
    width: 73px; /* 이미지 너비 */
    height: 63px;
    overflow: hidden;
    border-radius: 5px;
    border: 1px solid #ccc; 
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); 
    margin-right: 18px; /* 이미지와 텍스트 사이 간격 */
}

.attraction-item-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.attraction-item-details {
    flex: 1; /* 남은 공간을 채움 */
    text-align: left; /* 왼쪽 정렬 */
}

.attraction-title {
    font-size: 1.1em;
    font-weight: bold;
    margin-bottom: 8px;
    text-overflow: ellipsis; /* 넘치는 텍스트에 ... 표시 */
    white-space: nowrap; /* 텍스트를 한 줄로 표시 */
    overflow: hidden; /* 넘치는 텍스트 숨김 */
}

.attraction-addr1 {
    font-size: 0.9em;
    color: #555;
    text-overflow: ellipsis; /* 넘치는 텍스트에 ... 표시 */
    white-space: nowrap; /* 텍스트를 한 줄로 표시 */
    overflow: hidden; /* 넘치는 텍스트 숨김 */
}

.add-button {
    position: absolute; /* 부모 요소 내에서 절대 위치 지정 */
    bottom: 10px; /* 아래에서 10px */
    right: 10px; /* 오른쪽에서 10px */
    background-color: #cacfd4; /* 버튼 배경색 */
    color: white; /* 텍스트 색상 */
    border: none; /* 테두리 제거 */
    border-radius: 3px; /* 둥근 모서리 */
    padding: 5px 10px; /* 내부 여백 */
    font-size: 0.8em; /* 텍스트 크기 */
    font-weight: bold; /* 텍스트 굵게 */
    cursor: pointer; /* 커서 변경 */
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2); /* 그림자 효과 */
    transition: background-color 0.2s ease, transform 0.2s ease; /* 호버 효과 */
    opacity: 1; /* 항상 표시 */
    visibility: visible; /* 항상 표시 */
    display: block; /* 항상 표시 */
}

.add-button:hover {
    background-color: #9ea1a4; /* 호버 시 배경색 */
    transform: scale(1.05); /* 약간 확대 */
}

.add-button:active {
    background-color: #9ea1a4; /* 클릭 시 배경색 */
    transform: scale(0.95); /* 약간 축소 */
}



</style>
