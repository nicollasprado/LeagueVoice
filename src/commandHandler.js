import axios from "axios";
import { InteractionResponseType } from "discord-interactions";

const API_HOST = process.env.API_HOST;

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

export async function commandInfo(res, targetId) {
  const userLeagueIdRequest = await axios.get(`${API_HOST}/user/${targetId}`);

  let response;
  if (userLeagueIdRequest.status != 200) {
    response = "Jogador não registrado no nosso banco de dados.";
  } else {
    const userLeagueId = userLeagueIdRequest.data.leagueId;

    const infoRequest = await axios.get(
      `${API_HOST}/user/leagueInfo/${targetId}`
    );
    const leagueInfo = infoRequest.data;

    const winrate = (leagueInfo.wins / leagueInfo.losses).toFixed(2);

    response = `Jogador: ${userLeagueId} \nElo: ${leagueInfo.tier} ${leagueInfo.rank} \nWinrate: ${winrate} em ${leagueInfo.wins} vitórias e ${leagueInfo.losses} derrotas.`;
  }

  return res.send({
    type: InteractionResponseType.CHANNEL_MESSAGE_WITH_SOURCE,
    data: {
      content: response,
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
