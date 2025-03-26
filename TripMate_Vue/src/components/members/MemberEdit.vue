<script setup>
import { ref, onMounted } from "vue";
import { useAuthStore } from "@/stores/authStore";
import { updateUser, myPage } from "@/api/member";
import { useRouter } from "vue-router";

const router = useRouter();
const authStore = useAuthStore();
const userId = ref("");
const userName = ref("");
const emailId = ref("");
const emailDomain = ref("");

// 서버로부터 사용자 정보 가져오기
const fetchUserInfo = async () => {
    try {
        const response = await myPage(authStore.loginUserId);
        userId.value = response.data.userId;
        userName.value = response.data.userName;
        emailId.value = response.data.emailId;
        emailDomain.value = response.data.emailDomain;
    } catch (error) {
        console.error("마이페이지 데이터 로드 실패:", error);
        alert("사용자 정보를 가져오는데 실패했습니다.");
    }
};

// 컴포넌트가 로드될 때 사용자 정보 가져오기
onMounted(() => {
    fetchUserInfo();
});

// 사용자 정보 업데이트
const updateUserInfo = async () => {
    try {
        const updateData = {
            userId: userId.value,
            userName: userName.value,
            emailId: emailId.value,
            emailDomain: emailDomain.value,
        };
        await updateUser(updateData);
        alert("회원 정보가 성공적으로 수정되었습니다.");
        router.push({ name: "user-myPage" }); // 마이페이지로 이동
    } catch (error) {
        console.error("회원 정보 수정 실패:", error);
        alert("회원 정보를 수정하는데 실패했습니다.");
    }
};
</script>

<template>
    <main class="container">
        <section class="header">
            <h2>회원 정보 수정</h2>
            <p>아래 정보를 수정하고 <span class="highlight">[저장하기]</span> 버튼을 눌러주세요.</p>
        </section>
        <section class="form">
            <div class="form-group">
                <label for="userId">아이디</label>
                <span id="userId" class="static-value">{{ userId }}</span>
            </div>
            <div class="form-group">
                <label for="userName">이름</label>
                <input id="userName" v-model="userName" type="text" placeholder="이름을 입력하세요" />
            </div>
            <div class="form-group">
                <label for="email">이메일</label>
                <div class="email-input">
                    <input v-model="emailId" type="text" placeholder="이메일 아이디" />
                    <span>@</span>
                    <input v-model="emailDomain" type="text" placeholder="도메인" />
                </div>
            </div>
            <button @click="updateUserInfo" class="submit-btn">저장하기</button>
        </section>
    </main>
</template>

<style scoped>
/* 메인 컨테이너 */
.container {
    max-width: 600px;
    margin: 40px auto;
    padding: 20px;
    background: #ffffff;
    border-radius: 15px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    font-family: 'Arial', sans-serif;
}

/* 헤더 */
.header h2 {
    text-align: center;
    font-size: 1.8em;
    margin-bottom: 10px;
    color: #333;
}

.header p {
    text-align: center;
    font-size: 1em;
    color: #777;
}

.header .highlight {
    color: #2ec4ff;
    font-weight: bold;
}

/* 폼 */
.form {
    margin-top: 20px;
}

.form-group {
    display: flex;
    flex-direction: column;
    margin-bottom: 20px;
}

label {
    font-size: 1em;
    font-weight: bold;
    margin-bottom: 5px;
    color: #555;
}

.static-value {
    padding: 10px;
    background: #f9f9f9;
    border: 1px solid #ddd;
    border-radius: 5px;
    font-size: 1em;
    color: #333;
}

input {
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 5px;
    font-size: 1em;
    color: #333;
    transition: all 0.3s ease-in-out;
}

input:focus {
    border-color: #2ec4ff;
    outline: none;
    box-shadow: 0 0 4px rgba(46, 196, 255, 0.4);
}

/* 이메일 인풋 */
.email-input {
    display: flex;
    align-items: center;
    gap: 10px;
}

.email-input input {
    flex: 1;
}

.email-input span {
    font-size: 1.2em;
    color: #555;
}

/* 버튼 */
.submit-btn {
    display: block;
    width: 100%;
    padding: 12px 20px;
    background-color: #2ec4ff;
    color: white;
    border: none;
    border-radius: 5px;
    font-size: 1.2em;
    font-weight: bold;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.submit-btn:hover {
    background-color: #1d9cd7;
}

.submit-btn:active {
    background-color: #1478a6;
}

/* 반응형 */
@media (max-width: 768px) {
    .container {
        padding: 15px;
    }

    .header h2 {
        font-size: 1.5em;
    }

    label {
        font-size: 0.9em;
    }

    input {
        font-size: 0.9em;
    }

    .submit-btn {
        font-size: 1em;
    }
}
</style>
