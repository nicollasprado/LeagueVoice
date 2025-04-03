import { InteractionResponseType } from "discord-interactions";

export function invalidChannelError(res, validChannelId) {
  return res.send({
    type: InteractionResponseType.CHANNEL_MESSAGE_WITH_SOURCE,
    data: {
      content: `Canal inválido, utilize em: <#${validChannelId}>`,
    },
  });
}
