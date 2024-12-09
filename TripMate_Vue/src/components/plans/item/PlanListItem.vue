<script setup>
import { defineProps, defineEmits, ref } from 'vue';

const props = defineProps({
    planId: Number,
    title: String,
    description: String,
    image: String,
});

const emits = defineEmits(['click', 'delete', 'share']); // 이벤트 정의
const isHovered = ref(false); // 마우스 오버 상태 관리
</script>

<template>
    <div class="plan-item" @click="$emit('click')" @mouseover="isHovered = true" @mouseleave="isHovered = false">
        <div class="plan-item-image">
            <img :src="image" :alt="title" />
        </div>
        <div class="plan-item-details">
            <h3 class="plan-title">{{ title }}</h3>
            <p class="plan-description">{{ description }}</p>
        </div>
        <!-- 공유 및 삭제 버튼 -->
        <div v-if="isHovered" class="plan-item-actions">
            <button class="action-btn share" @click.stop="$emit('share', planId)">공유</button>
            <button class="action-btn delete" @click.stop="$emit('delete', planId)">삭제</button>
        </div>
    </div>
</template>

<style scoped>
.plan-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 300px;
    background-color: #fff;
    border-radius: 10px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    padding: 15px;
    margin: 15px;
    transition: transform 0.3s ease;
    cursor: pointer;
    border: 1px solid #d5e4fa;
    position: relative;
    /* 액션 버튼 배치를 위해 */
}

.plan-item:hover {
    transform: scale(1.05);
}

.plan-item-image {
    width: 100%;
    height: 200px;
    overflow: hidden;
    border-radius: 10px;
    border: 1px solid #d5e4fa;
}

.plan-item-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.plan-item-details {
    text-align: center;
    margin-top: 15px;
}

.plan-title {
    font-size: 1.2em;
    font-weight: bold;
    margin-bottom: 10px;
}

.plan-description {
    font-size: 1em;
    color: #555;
}

/* 액션 버튼 컨테이너 */
.plan-item-actions {
    position: absolute;
    top: 10px;
    right: 10px;
    display: flex;
    gap: 10px;
}

/* 공유 및 삭제 버튼 스타일 */
.action-btn {
    padding: 5px 10px;
    border: none;
    border-radius: 5px;
    font-size: 0.9em;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.action-btn.share {
    background-color: #4caf50;
    color: white;
}

.action-btn.share:hover {
    background-color: #45a049;
}

.action-btn.delete {
    background-color: #f44336;
    color: white;
}

.action-btn.delete:hover {
    background-color: #e53935;
}
</style>
