export function checkRoles(request, toFindRoleId) {
  const userRoles = request.body.member.roles;

  let found = false;

  for (let role in userRoles) {
    if (role === toFindRoleId) {
      found = true;
      break;
    }
  }

  return found;
}
