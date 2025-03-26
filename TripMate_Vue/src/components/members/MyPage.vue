<script setup>
import { ref, onMounted } from "vue";
import { useAuthStore } from "@/stores/authStore";
import { myPage } from "@/api/member";

const authStore = useAuthStore();

const userId = ref("");
const userName = ref("");
const emailAdr = ref("");

// 서버로부터 사용자 정보 가져오기
const fetchUserInfo = async () => {
    try {
        const response = await myPage(authStore.loginUserId);
        userId.value = response.data.userId;
        userName.value = response.data.userName;
        emailAdr.value = response.data.emailId + "@" + response.data.emailDomain;
    } catch (error) {
        console.error("마이페이지 데이터 로드 실패:", error);
        alert("사용자 정보를 가져오는데 실패했습니다.");
    }
};

// 컴포넌트가 로드될 때 사용자 정보 가져오기
onMounted(() => {
    fetchUserInfo();
});
</script>

<template>
    <main class="container">
        <header class="header">
            <h2>안녕하세요, <span class="username">{{ userName }}</span>님!</h2>
            <p>아래에서 회원 정보를 확인하고 활동 내역을 확인해보세요.</p>
        </header>

        <section class="user-info">
            <h3>회원 정보</h3>
            <div class="info-row">
                <label>아이디</label>
                <span>{{ userId }}</span>
            </div>
            <div class="info-row">
                <label>이름</label>
                <span>{{ userName }}</span>
            </div>
            <div class="info-row">
                <label>이메일</label>
                <span>{{ emailAdr }}</span>
            </div>
            <router-link :to="{ name: 'user-edit', query: { userId: userId, userName: userName, emailAdr: emailAdr } }"
                class="edit-btn">정보 수정</router-link>
        </section>

        <section class="user-actions">
            <h3>내 활동</h3>
            <ul>
                <li><router-link to="/board">작성한 게시글</router-link></li>
                <li><router-link to="/plan">내 여행 계획</router-link></li>
            </ul>
        </section>
    </main>
</template>

<style scoped>
/* 메인 컨테이너 */
.container {
    max-width: 700px;
    margin: 40px auto;
    padding: 20px 30px;
    background: #ffffff;
    border-radius: 15px;
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
    font-family: 'Arial', sans-serif;
}

/* 헤더 */
.header {
    text-align: center;
    margin-bottom: 30px;
}

.header h2 {
    font-size: 1.8em;
    margin-bottom: 10px;
    color: #333;
}

.header .username {
    color: #2ec4ff;
    font-weight: bold;
}

.header p {
    font-size: 1em;
    color: #666;
}

/* 회원 정보 섹션 */
.user-info {
    margin-bottom: 30px;
}

.user-info h3 {
    font-size: 1.5em;
    color: #444;
    margin-bottom: 20px;
    border-bottom: 2px solid #2ec4ff;
    display: inline-block;
    padding-bottom: 5px;
}

.info-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 0;
    border-bottom: 1px solid #eee;
}

.info-row label {
    font-weight: bold;
    color: #555;
}

.info-row span {
    font-size: 1em;
    color: #333;
}

/* 정보 수정 버튼 */
.edit-btn {
    display: block;
    margin: 20px auto 0;
    padding: 10px 20px;
    background-color: #2ec4ff;
    color: white;
    font-weight: bold;
    border-radius: 5px;
    text-align: center;
    text-decoration: none;
    transition: background-color 0.3s ease;
}

.edit-btn:hover {
    background-color: #1d9cd7;
}

/* 활동 섹션 */
.user-actions {
    margin-top: 30px;
}

.user-actions h3 {
    font-size: 1.5em;
    color: #444;
    margin-bottom: 20px;
    border-bottom: 2px solid #2ec4ff;
    display: inline-block;
    padding-bottom: 5px;
}

.user-actions ul {
    list-style: none;
    padding: 0;
}

.user-actions ul li {
    margin: 15px 0;
}

.user-actions ul li a {
    text-decoration: none;
    font-size: 1em;
    color: #2ec4ff;
    transition: color 0.3s ease;
}

.user-actions ul li a:hover {
    color: #1d9cd7;
}
</style>
