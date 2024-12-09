<script setup>
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useAuthStore } from "@/stores/authStore";
import { acceptPlan } from "@/api/plan.js";
const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();

// URL 쿼리 매개변수에서 값 가져오기
const encryptedId = ref(route.query.encryptedId || "");
const inviterName = ref(route.query.userName || "초대자 이름");
const travelTitle = ref(route.query.planTitle || "여행 제목");
const travelDates = ref("2024.12.2 - 12.5");
const profilePicture = ref("path/to/profile/picture.jpg"); // 실제 이미지 경로로 변경하세요
const coverImage = ref("path/to/cover/image.jpg"); // 실제 이미지 경로로 변경하세요

async function acceptInvite() {
    if (!encryptedId.value) {
        console.error("Encrypted ID가 없습니다.");
        return;
    }
    try {
        const response = await acceptPlan(encryptedId.value, authStore.loginUserId); // encryptedId와 userId 전달
        console.log("초대 수락 성공:", response);

        // 수락 성공 시 계획 목록 페이지로 이동
        router.push({ name: "plan-list" });
    } catch (error) {
        console.error("초대 수락 실패:", error);
        alert("초대 수락에 실패했습니다. 다시 시도해주세요.");
    }
}
</script>

<template>
    <div class="invite-container">
        <div class="invite-header">
            <img :src="coverImage" alt="Cover" class="cover-image" />
            <div class="profile-picture-wrapper">
                <img :src="profilePicture" alt="Profile" class="profile-picture" />
            </div>
        </div>
        <div class="invite-content">
            <h3>{{ inviterName }}님이 초대합니다</h3>
            <h1>{{ travelTitle }}</h1>
            <h4>{{ travelDates }}</h4>
            <p>초대를 수락하고 즐거운 여행을 계획해 보세요. 여행 일정을 같이 보고, 편집할 수 있습니다.</p>
            <button class="accept-button" @click="acceptInvite">초대 수락하기</button>
        </div>
        <div class="invite-footer">
            <p>여행 일정 참여는 TripMate 웹에서만 가능합니다.</p>
        </div>
    </div>
</template>

<style scoped>
.invite-container {
    max-width: 600px;
    margin: 0 auto;
    background-color: #f5f7f8;
    border-radius: 15px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    padding: 20px;
    text-align: center;
}

.invite-header {
    position: relative;
    background-color: #63cdda;
    padding-bottom: 80px;
    border-top-left-radius: 15px;
    border-top-right-radius: 15px;
}

.cover-image {
    width: 100%;
    height: 150px;
    object-fit: cover;
    border-top-left-radius: 15px;
    border-top-right-radius: 15px;
}

.profile-picture-wrapper {
    position: absolute;
    bottom: -40px;
    left: 50%;
    transform: translateX(-50%);
    width: 80px;
    height: 80px;
    border-radius: 50%;
    overflow: hidden;
    border: 4px solid #ffffff;
}

.profile-picture {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.invite-content h3 {
    margin-top: 50px;
    color: #4a4a4a;
    font-weight: normal;
}

.invite-content h1 {
    font-size: 24px;
    font-weight: bold;
    color: #333333;
    margin: 10px 0;
}

.invite-content h4 {
    font-size: 18px;
    font-weight: normal;
    color: #666666;
    margin-bottom: 20px;
}

.accept-button {
    background-color: #4a90e2;
    color: #ffffff;
    border: none;
    padding: 10px 20px;
    font-size: 16px;
    cursor: pointer;
    border-radius: 5px;
}

.accept-button:hover {
    background-color: #357abd;
}

.invite-footer {
    margin-top: 20px;
    font-size: 14px;
    color: #888888;
}
</style>
