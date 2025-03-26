<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { listArticle } from "@/api/board.js";

import VSelect from "@/components/common/VSelect.vue";
import BoardListItem from "@/components/boards/item/BoardListItem.vue";
import VPageNavigation from "@/components/common/PageNavigation.vue";

const router = useRouter();

const selectOption = ref([
  { text: "검색조건", value: "" },
  { text: "글번호", value: "article_no" },
  { text: "제목", value: "subject" },
  { text: "작성자아이디", value: "user_id" },
]);

const articles = ref([]);
const currentPage = ref(1);
const totalPage = ref(0);
const { VITE_ARTICLE_LIST_SIZE } = import.meta.env;
const param = ref({
  pgno: currentPage.value,
  spp: VITE_ARTICLE_LIST_SIZE,
  key: "",
  word: "",
});

onMounted(() => {
  getArticleList();
});

const changeKey = (val) => {
  console.log("BoardList에서 선택한 조건 : " + val);
  param.value.key = val;
};

const getArticleList = () => {
  console.log("서버에서 글목록 얻어오자!!!", param.value);
  listArticle(
    param.value,
    ({ data }) => {
      articles.value = data.articles;
      currentPage.value = data.currentPage;
      totalPage.value = data.totalPageCount;
    },
    (error) => {
      console.log(error);
    }
  );
};

const onPageChange = (val) => {
  console.log(val + "번 페이지로 이동 준비 끝!!!");
  currentPage.value = val;
  param.value.pgno = val;
  getArticleList();
};

const moveWrite = () => {
  router.push({ name: "article-write" });
};
</script>

<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-lg-10">
        <h2 class="title my-3 py-3 text-center">
          질문게시판
        </h2>
      </div>
      <div class="col-lg-10">
        <div class="row mb-2 align-items-center">
          <div class="col">
            <form class="d-flex align-items-center search-form">
              <button type="button" class="btn btn-outline-primary btn-sm write-btn" @click="moveWrite">
                글쓰기
              </button>
              <div class="ms-auto d-flex align-items-center">
                <VSelect :selectOption="selectOption" @onKeySelect="changeKey" class="flex-shrink-0 me-2" style="height: 28px;"/>

                <input
                  type="text"
                  class="form-control me-2"
                  v-model="param.word"
                  placeholder="검색어..."
                />
                <button class="btn btn-dark" type="button" @click="getArticleList">검색</button>
              </div>
            </form>
          </div>
        </div>
        <table class="table table-hover table-striped">
          <thead>
            <tr class="text-center">
              <th scope="col">글번호</th>
              <th scope="col">제목</th>
              <th scope="col">작성자</th>
              <th scope="col">조회수</th>
              <th scope="col">작성일</th>
            </tr>
          </thead>
          <tbody>
            <BoardListItem
              v-for="article in articles"
              :key="article.articleNo"
              :article="article"
            ></BoardListItem>
          </tbody>
        </table>
      </div>
      <div class="page">
        <VPageNavigation
          :current-page="currentPage"
          :total-page="totalPage"
          @pageChange="onPageChange"
        ></VPageNavigation>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 페이지 전체 컨테이너 스타일 */
.container {
  width: 1200px;
  margin-top: 40px;
  padding: 0 15px;
  background-color: #f7f9fb;
  border-radius: 20px;
  box-shadow: 0 -1px 8px rgba(0, 0, 0, 0.1);
}

/* 제목 스타일 */
.title {
  font-size: 1.8rem;
  color: #4a4a4a;
  padding-top: 20px;
  margin-bottom: 10px;
  font-weight: bold;
  text-align: center;
}

/* 테이블 스타일 */
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

th, td {
  padding: 12px;
  text-align: center;
}

th {
  background-color: #e9ecef;
  font-weight: bold;
  color: #333;
}

tr:hover {
  background-color: #f5f5f5;
}

/* 검색 및 글쓰기 버튼 스타일 */
.search-form {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
}

.write-btn {
  background-color: #75aae2;
  color: white;
  padding: 0 15px;
  border: none;
  height: 30px;
  border-radius: 5px;
  margin-left: 10px;
}

.write-btn:hover {
  background-color: #0056b3;
}

/* 검색 항목을 오른쪽으로 정렬 */
.search-form .ms-auto {
  margin-left: auto;
  display: flex;
  align-items: center;
}

.form-control, .btn-dark, .search-form input[type="text"] {
  height: 30px;
  border-radius: 5px;
}

.form-control {
  max-width: 220px;
  border: 1px solid #ced4da;
}

input[type="text"] {
  border: 1px solid #ced4da;
}

input[type="text"]:focus, .form-control:focus {
  border-color: #007bff;
  box-shadow: 0 0 5px rgba(0, 123, 255, 0.25);
}

.btn-dark {
  width: 60px;
  height: 30px;
  background-color: #343a40;
  border: none;
  color: white;
  margin-right: 10px;
}

.btn-dark:hover {
  background-color: #23272b;
}

/* VSelect와 검색창의 레이아웃 조정 */
.search-form select, .search-form input {
  margin-right: 10px;
}

.page {
  padding-bottom: 20px;
}

/* 페이지 네비게이션 스타일 */
.page-navigation {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  gap: 5px;
}

.page-navigation button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 8px 15px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.page-navigation button:hover {
  background-color: #0056b3;
}

.page-navigation button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
