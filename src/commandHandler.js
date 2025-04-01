import axios from "axios";
import { InteractionResponseType } from "discord-interactions";

export function commandTest(res) {
  // Send a message into the channel where command was triggered from
  return res.send({
    type: InteractionResponseType.CHANNEL_MESSAGE_WITH_SOURCE,
    data: {
      // Fetches a random emoji to send from a helper function
      content: `hello world ${getRandomEmoji()}`,
    },
  });
}

export function commandInfo(res, targetId) {
  return res.send({
    type: InteractionResponseType.CHANNEL_MESSAGE_WITH_SOURCE,
    data: {
      content: "test",
    },
  });
}

export async function commandLink(res, authorId) {
  return res.send({
    type: InteractionResponseType.CHANNEL_MESSAGE_WITH_SOURCE,
    data: {
      content:
        "https://discord.com/oauth2/authorize?client_id=1356616558004277450&response_type=code&redirect_uri=https%3A%2F%2F4535-187-61-200-25.ngrok-free.app%2Fapi%2Fauth%2Fdiscord%2Fredirect&scope=connections+identify+guilds",
    },
  });
}
