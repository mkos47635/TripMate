import { createRouter, createWebHistory } from "vue-router";
import { useAuthStore } from "@/stores/authStore";
import { usePlanStore } from "@/stores/planStore";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: () => import("@/views/TheHomeView.vue"),
    },
    {
      path: "/user",
      name: "user",
      component: () => import("@/views/TheMemberView.vue"),
      children: [
        {
          path: "login",
          name: "user-login",
          component: () => import("@/components/members/MemberLogin.vue"),
        },
        {
          path: "join",
          name: "user-join",
          component: () => import("@/components/members/MemberJoin.vue"),
        },
        {
          path: "myPage",
          name: "user-myPage",
          component: () => import("@/components/members/MyPage.vue"),
          meta: { requiresAuth: true },
        },
        {
          path: "edit",
          name: "user-edit",
          component: () => import("@/components/members/MemberEdit.vue"),
          meta: { requiresAuth: true },
        },
      ],
    },
    {
      path: "/trip",
      name: "trip",
      component: () => import("@/views/TheTripView.vue"),
    },
    {
      path: "/ai",
      name: "ai",
      component: () => import("../views/TheAiRecommendation.vue"),
    },
    // 관광지 관련
    {
      path: "/attraction",
      name: "attraction",
      component: () => import("@/views/TheAttractionView.vue"),
    },
    {
      path: "/attraction/list",
      name: "attraction-list",
      component: () => import("@/components/attractions/AttractionList.vue"),
    },
    {
      path: "/attraction/detail",
      name: "attraction-detail",
      component: () =>
        import("@/components/attractions/item/AttractionListItemDetail.vue"),
      meta: { hideHeader: true }, // 이 페이지에서는 Header를 숨김
    },

    {
      path: "/board",
      name: "board",
      component: () => import("../views/TheBoardView.vue"),
      meta: { requiresAuth: true },
      redirect: { name: "article-list" },
      children: [
        {
          path: "list",
          name: "article-list",
          component: () => import("@/components/boards/BoardList.vue"),
        },
        {
          path: "view/:articleno",
          name: "article-view",
          component: () => import("@/components/boards/BoardDetail.vue"),
        },
        {
          path: "write",
          name: "article-write",
          component: () => import("@/components/boards/BoardWrite.vue"),
        },
        {
          path: "modify/:articleno",
          name: "article-modify",
          component: () => import("@/components/boards/BoardModify.vue"),
        },
      ],
    },

    {
      path: "/plan",
      name: "plan",
      component: () => import("../views/ThePlanView.vue"),
      meta: { requiresAuth: true },
      redirect: { name: "plan-list" },
      children: [
        {
          path: "create",
          name: "plan-create",
          component: () => import("@/components/plans/MyPlannnerMake.vue"),
          meta: { requiresAuth: true },
        },
        {
          path: "list",
          name: "plan-list",
          component: () => import("@/components/plans/PlanList.vue"),
        },
        {
          path: "/plan/view/:id",
          name: "plan-make",
          component: () => import("@/components/plans/PlanMake.vue"),
          props: true, // 라우트 파라미터를 props로 전달
        },

        {
          path: "start",
          name: "plan-start",
          component: () => import("@/components/plans/PlanStart.vue"),
        },
        {
          path: "invite",
          name: "plan-invite",
          component: () => import("@/components/plans/PlanAcception.vue"),
          meta: { requiresAuth: true },
        },
      ],
    },
  ],
});

// 전역 네비게이션 가드
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();

  // 인증이 필요한 경로에 접근 시
  if (to.meta.requiresAuth && !authStore.loginUserId) {
    alert("로그인이 필요합니다.");
    // 로그인 페이지로 리다이렉트 후 로그인하면 다시 그 접근으로
    next({ name: "user-login", query: { redirect: to.fullPath } });
  } else {
    next(); // 접근 허용
  }
});

export default router;
