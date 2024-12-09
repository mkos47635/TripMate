import { localAxios } from "@/util/http-commons";

const local = localAxios();

function searchAttraction(params, success, fail) {
  local
    .get(`/attraction/list`, { params }) // /attraction/list로 경로 수정
    .then(success)
    .catch(fail);
}

function getGugun(params, success, fail) {
  local
    .get(`/attraction/gugun`, { params }) // /attraction/list로 경로 수정
    .then(success)
    .catch(fail);
}

export { searchAttraction, getGugun };
