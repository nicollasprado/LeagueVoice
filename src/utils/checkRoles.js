export function checkRoles(request, toFindRoleId) {
  const userRoles = request.body.member.roles;

  let found = false;

  for (let i = 0; i < userRoles.length; i++) {
    if (userRoles[i] === toFindRoleId) {
      found = true;
      break;
    }
  }

  return found;
}
