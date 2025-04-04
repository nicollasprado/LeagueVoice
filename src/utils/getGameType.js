export function getGameType(gameQueueConfigId) {
  let type = "ranked";
  if (gameQueueConfigId === 400 || gameQueueConfigId === 430) {
    type = "normal";
  }
  return type;
}
