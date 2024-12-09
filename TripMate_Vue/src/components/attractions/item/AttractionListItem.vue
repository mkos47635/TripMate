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
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 210px;
    background-color: #fff;
    border-radius: 10px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    padding: 10px;
    margin-top: 10px;
    margin-left: 20px;
    margin-right: 20px;
    margin-bottom: 20px;
    transition: transform 0.3s ease;
    cursor: pointer;
    border: 1px solid #d5e4fa; 
}

.attraction-item:hover {
    transform: scale(1.05);
}

.attraction-item-image {
    width: 100%;
    height: 130px;
    overflow: hidden;
    border-radius: 10px;
    border: 1px solid #ccc; 
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); 
}

.attraction-item-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.attraction-item-details {
    text-align: center;
    margin-top: 17px;
    margin-bottom: 7px;
    height: 60px; /* 고정된 높이 */
    overflow: hidden; /* 내용이 넘칠 경우 숨김 */
}

.attraction-title {
    font-size: 1em;
    font-weight: bold;
    margin-bottom: 5px;
    height: 20px; /* 고정 높이 */
    overflow: hidden; /* 넘치는 텍스트 숨김 */
    text-overflow: ellipsis; /* 넘치는 텍스트에 ... 표시 */
    white-space: nowrap; /* 텍스트를 한 줄로 표시 */
}

.attraction-addr1 {
    margin-top: 10px;
    font-size: 0.9em;
    color: #555;
    height: 20px; /* 고정 높이 */
    overflow: hidden; /* 넘치는 텍스트 숨김 */
    text-overflow: ellipsis; /* 넘치는 텍스트에 ... 표시 */
}
</style>
