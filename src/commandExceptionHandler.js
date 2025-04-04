import { InteractionResponseType } from "discord-interactions";

export function invalidChannelException(res, validChannelId) {
  return res.send({
    type: InteractionResponseType.CHANNEL_MESSAGE_WITH_SOURCE,
    flags: 1 << 6,
    data: {
      content: `Canal inválido, utilize em: <#${validChannelId}>`,
    },
  });
}

export function outOfPermissionException(res, validRoleId) {
  return res.send({
    type: InteractionResponseType.CHANNEL_MESSAGE_WITH_SOURCE,
    flags: 1 << 6,
    data: {
      content: `Você não tem permissão de usar esse comando.`,
    },
  });
}
