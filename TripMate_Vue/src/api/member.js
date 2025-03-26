import { localAxios } from "@/util/http-commons";

const local = localAxios();

function join(param, success, fail) {
  local.post(`/user/join`, param).then(success).catch(fail);
}
function idCheck(param, success, fail) {
  local.get(`/user/idCheck/${param}`).then(success).catch(fail);
}
function login(param, success, fail) {
  local.post(`/user/login`, param).then(success).catch(fail);
}
// function myPage(param) {
//   local.get(`/user/myPage/${param}`).then(success).catch(fail);
// }
function myPage(param) {
  return local.get(`/user/myPage/${param}`);
}
function updateUser(param, success, fail) {
  return local.patch(`/user/update`, param);
}
export { join, idCheck, login, myPage, updateUser };
