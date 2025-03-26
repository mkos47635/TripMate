<script setup>
defineProps({ article: Object })
</script>

<template>
  <tr class="text-center">
    <th scope="row">{{ article.articleNo }}</th>
    <td class="text-start">
      <RouterLink :to="{ name: 'article-view', params: { articleno: article.articleNo } }"
        class="article-title link-dark">
        {{ article.subject }}
      </RouterLink>
    </td>
    <td>{{ article.userName }}</td>
    <td>{{ article.hit }}</td>
    <td>{{ article.registerTime }}</td>
  </tr>
</template>

<style scoped>
a {
  text-decoration: none;
}
/* 모든 td 요소에 중앙 정렬을 적용 */
td {
  text-align: center;
}
/* article.subject가 표시되는 td는 왼쪽 정렬 */
td.text-start {
  text-align: left;
}
</style>
