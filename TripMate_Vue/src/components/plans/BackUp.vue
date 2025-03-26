<script setup>
import { defineProps, defineEmits, computed } from 'vue';

const props = defineProps({
    no: Number,
    title: String,
    addr1: String,
    firstImage1: String,
});

const image = computed(() => {
  // props.first_image1 사용
  return props.firstImage1 ? props.firstImage1 : "/src/assets/img/noImage.png";
});

const emits = defineEmits(['click']);

const truncatedTitle = computed(() => {
  return props.title.length > 10 ? props.title.slice(0, 15) + "" : props.title;
});


</script>

<template>
    <div class="attraction-item" @click="$emit('click')"> <!-- 클릭 이벤트 전달 -->
        <div class="attraction-item-image">
            <img :src="image" :alt="image" />
        </div>
        <div class="attraction-item-details">
            <h3 class="attraction-title">{{ truncatedTitle }}</h3>
            <p class="attraction-addr1">{{ addr1 }}</p>
        </div>
    </div>
</template>


<style scoped>
.attraction-item {
    display: flex; /* 가로 정렬 */
    align-items: center; /* 세로 중앙 정렬 */
    width: 390px; /* 전체 너비 */
    height: 75px;
    background-color: #fff;
    border-radius: 10px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    padding: 6px;
    transition: transform 0.3s ease;
    cursor: pointer;
    border: 1px solid #d5e4fa; 
    margin-bottom: 9px;
}

.attraction-item:hover {
    transform: scale(1.01);
}

.attraction-item-image {
    width: 72px; /* 이미지 너비 */
    height: 65px;
    overflow: hidden;
    border-radius: 10px;
    border: 1px solid #ccc; 
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); 
    margin-right: 15px; /* 이미지와 텍스트 사이 간격 */
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
</style>
