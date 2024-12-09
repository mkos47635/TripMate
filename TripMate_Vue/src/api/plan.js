import { localAxios } from "@/util/http-commons";

const local = localAxios();

function getPlanList(params, success, fail) {
  console.log("요청 Params:", params); // 디버깅용
  local
    .get(`/plan/list`, { params }) // userId를 객체로 전달
    .then(success)
    .catch(fail);
}

function getPlanDetailList(planId, success, fail) {
  console.log("요청 Params:", planId); // 디버깅 로그
  local
    .get(`/plan/detail`, { params: { planId } }) // params 객체로 전달
    .then(success)
    .catch(fail);
}
function deletePlan(planId, success, fail) {
  local.delete(`/plan/delete/${planId}`).then(success).catch(fail);
}
function savePlanDetails(detail, success, fail) {
  console.log("planjs detail", detail);
  local.post(`/plan/save`, JSON.stringify(detail)).then(success).catch(fail);
}

function deletePlanDetails(detail, success, fail) {
  console.log("planjs detail", detail);
  local.post(`/plan/delete`, JSON.stringify(detail)).then(success).catch(fail);
}

function sendPlanItems(items, success, fail) {
  console.log("planjs send", items);
  local.post("/plan/send", items).then(success).catch(fail);
}

function generateShareLink(planId, success, fail) {
  local.get(`/plan/share/${planId}`).then(success).catch(fail);
}
function acceptPlan(encryptedId, userId, success, fail) {
  local
    .post(`/plan/accept`, {
      encryptedId,
      userId,
    })
    .then(success)
    .catch(fail);
}
function createPlan(planner, success, fail) {
  local.post(`/plan/create`, JSON.stringify(planner)).then(success).catch(fail);
}
export {
  getPlanList,
  getPlanDetailList,
  deletePlan,
  savePlanDetails,
  deletePlanDetails,
  generateShareLink,
  acceptPlan,
  createPlan,
  sendPlanItems,
};
