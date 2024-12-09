<script setup>
import { ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { registArticle, getModifyArticle, modifyArticle } from "@/api/board";
import { useAuthStore } from "@/stores/authStore";
const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();
const props = defineProps({ type: String });


const article = ref({
  articleNo: 0,
  subject: "",
  content: "",
  userId: authStore.loginUserId || "", // loginUserId로 초기화
  userName: "",
  hit: 0,
  registerTime: "",
});

if (props.type === "modify") {
  let { articleno } = route.params;
  console.log(articleno + "번글 얻어와서 수정할거야");
  getModifyArticle(
    articleno,
    ({ data }) => {
      article.value = data;
    },
    (error) => {
      console.log(error);
    }
  );
}

const subjectErrMsg = ref("");
const contentErrMsg = ref("");
watch(
  () => article.value.subject,
  (value) => {
    let len = value.length;
    if (len == 0 || len > 30) {
      subjectErrMsg.value = "";
    } else subjectErrMsg.value = "";
  },
  { immediate: true }
);
watch(
  () => article.value.content,
  (value) => {
    let len = value.length;
    if (len == 0 || len > 500) {
      contentErrMsg.value = "";
    } else contentErrMsg.value = "";
  },
  { immediate: true }
);

function onSubmit() {
  if (subjectErrMsg.value) {
    alert(subjectErrMsg.value);
  } else if (contentErrMsg.value) {
    alert(contentErrMsg.value);
  } else {
    props.type === "regist" ? writeArticle() : updateArticle();
  }
}

function writeArticle() {
  registArticle(
    article.value,
    (response) => {
      let msg = "글등록 처리시 문제 발생했습니다.";
      if (response.status == 201) msg = "글등록이 완료되었습니다.";
      alert(msg);
      moveList();
    },
    (error) => console.log(error)
  );
}

function updateArticle() {
  modifyArticle(
    article.value,
    (response) => {
      let msg = "글수정 처리시 문제 발생했습니다.";
      if (response.status == 200) msg = "글정보 수정이 완료되었습니다.";
      alert(msg);
      moveList();
    },
    (error) => console.log(error)
  );
}

function moveList() {
  router.push({ name: "article-list" });
}
</script>

<template>
  <form @submit.prevent="onSubmit">
    <div class="form-group">
      <label for="userid">작성자 ID : </label>
      <input type="text" id="userid" class="form-control" v-model="article.userId" readonly />
    </div>
    <div class="form-group">
      <label for="subject">제목 : </label>
      <input type="text" id="subject" class="form-control" v-model="article.subject" placeholder="제목..." />
      <small v-if="subjectErrMsg" class="form-text text-danger">{{ subjectErrMsg }}</small>
    </div>
    <div class="form-group">
      <label for="content">내용 : </label>
      <textarea id="content" class="form-control" v-model="article.content" rows="10"></textarea>
      <small v-if="contentErrMsg" class="form-text text-danger">{{ contentErrMsg }}</small>
    </div>
    <div class="form-group text-center">
      <button type="submit" class="btn btn-primary mb-3" v-if="type === 'regist'">
        글작성
      </button>
      <button type="submit" class="btn btn-success mb-3" v-else>
        글수정
      </button>
      <button type="button" class="btn btn-danger mb-3 ms-1" @click="moveList">
        목록으로 이동...
      </button>
    </div>
  </form>
</template>

<style scoped>
.form-group {
  margin-bottom: 20px;
}

.form-group label {
  font-size: 1.1em;
  margin-bottom: 10px;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 12px;
  font-size: 1em;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-sizing: border-box;
}

button {
  width: 100%;
  padding: 12px;
  margin-top: 5px;
  background-color: #75aae2;
  border: none;
  border-radius: 10px;
  color: white;
  font-size: 1.2em;
  cursor: pointer;
}

button:hover {
  background-color: #1d9cd7;
}
</style>
