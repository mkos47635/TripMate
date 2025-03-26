<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { detailArticle, deleteArticle } from "@/api/board";
import { useAuthStore } from "@/stores/authStore";
const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();

// 라우터에서 파라미터로 받은 articleno
const { articleno } = route.params;

const article = ref({});

onMounted(() => {
  getArticle();
});

const getArticle = () => {
  detailArticle(
    articleno,
    ({ data }) => {
      article.value = data;
      article.userName = authStore.loginUserId;
    },
    (error) => {
      console.log(error);
    }
  );
};

function moveList() {
  router.push({ name: "article-list" });
}

function moveModify() {
  router.push({ name: "article-modify", params: { articleno } });
}

function onDeleteArticle() {
  deleteArticle(
    articleno,
    (response) => {
      if (response.status == 200) moveList();
    },
    (error) => {
      console.log(error);
    }
  );
}
</script>
<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-lg-10">
        <h2 class="my-3 py-3 shadow-sm bg-light text-center">
          글보기
        </h2>
      </div>
      <div class="col-lg-10 text-start">
        <div class="row my-2">
          <h2 class="text-secondary px-5">{{ article.articleNo }}. {{ article.subject }}</h2>
        </div>
        <div class="row">
          <div class="col-md-8">
            <div class="clearfix align-content-center">
              <div class="text-secondary">
                {{ article.content }}
              </div>
              <img class="avatar me-2 float-md-start bg-light p-2"
                src="https://raw.githubusercontent.com/twbs/icons/main/icons/person-fill.svg" alt="User avatar" />
              <p>
                <span class="fw-bold">{{ article.userName }}</span> <br />
                <span class="text-secondary fw-light">
                  {{ article.registerTime }} 조회 : {{ article.hit }}
                </span>
              </p>
            </div>
          </div>
          <div class="divider mb-3"></div>
          <div class="divider mt-3 mb-3"></div>
          <div class="d-flex justify-content-center gap-3 w-100">
            <button type="button" class="btn btn-outline-primary mb-3" @click="moveList">
              글목록
            </button>
            <button type="button" class="btn btn-outline-success mb-3" @click="moveModify">
              글수정
            </button>
            <button type="button" class="btn btn-outline-danger mb-3" @click="onDeleteArticle">
              글삭제
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  margin-top: 60px;
  padding: 30px;
  background-color: #f9f9f9;
  border-radius: 10px;
  border: 1px solid #ccc;
  width: 700px;
}

h2 {
  color: #28425e;
  font-size: 1.8em;
  text-align: center;
  margin-bottom: 20px;
  border-bottom: 2px solid #49698b;
  padding-bottom: 10px;
}

.text-secondary {
  font-size: 1.2em;
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}

.divider {
  border-top: 1px solid #ccc;
}

button {
  font-size: 1.1em;
  padding: 8px 10px;
  border-radius: 10px;
}

button:hover {
  opacity: 0.8;
}

.btn-outline-primary {
  border-color: #007bff;
  color: #007bff;
}

.btn-outline-primary:hover {
  background-color: #007bff;
  color: white;
}

.btn-outline-success {
  border-color: #28a745;
  color: #28a745;
}

.btn-outline-success:hover {
  background-color: #28a745;
  color: white;
}

.btn-outline-danger {
  border-color: #dc3545;
  color: #dc3545;
}

.btn-outline-danger:hover {
  background-color: #dc3545;
  color: white;
}

.text-end {
  font-size: 1.2em;
}

.clearfix {
  overflow: auto;
}

.row.my-2 {
  margin-top: 20px;
}

.d-flex {
  display: flex;
  margin-top: 10px;
}

.justify-content-center {
  justify-content: center;
}

.gap-3 {
  gap: 1rem;
}

.w-100 {
  width: 100%;
}
</style>