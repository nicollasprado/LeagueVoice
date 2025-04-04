import axios from "axios";
import { InteractionResponseType } from "discord-interactions";

const API_HOST = process.env.API_HOST;
const DISCORD_API = process.env.DISCORD_API;
const DISCORD_API_KEY = process.env.DISCORD_TOKEN;

const GUILD_ID = process.env.GUILD_ID;

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

    response = `Jogador: ${userLeagueId} \nElo: ${leagueInfo.tier} ${leagueInfo.rank} \nWinrate: ${leagueInfo.winrate}% em ${leagueInfo.wins} vitórias e ${leagueInfo.losses} derrotas.`;
  }

  return res.send({
    type: InteractionResponseType.CHANNEL_MESSAGE_WITH_SOURCE,
    data: {
      content: response,
    },
  });
}

export async function commandLink(res, linkChannel) {
  const message = {
    embeds: [
      {
        title: "Conectar sua conta do league of legends com nosso sistema",
        description:
          "Ao realizar a conexão teremos acesso apenas ao seu nome, tag e puuid do league of legends que será usado para validar sua conta e encontrar suas partidas.",
        color: 0x6c3baa,
        footer: {
          text: "League Voice Brasil",
        },
        timestamp: new Date().toISOString(),
      },
    ],
    components: [
      {
        type: 1,
        components: [
          {
            type: 2,
            label: "Conectar",
            style: 5,
            url: "https://discord.com/oauth2/authorize?client_id=1356616558004277450&response_type=code&redirect_uri=https%3A%2F%2Fa2e6-187-61-200-25.ngrok-free.app%2Fapi%2Fauth%2Fdiscord%2Fredirect&scope=identify+connections",
          },
        ],
      },
    ],
  };

  await axios.post(`${DISCORD_API}/channels/${linkChannel}/messages`, message, {
    headers: {
      Authorization: `Bot ${DISCORD_API_KEY}`,
    },
  });
}

export async function commandCJoin(res, joinChannelId) {
  const message = {
    embeds: [
      {
        title: "Conectar a sala de voz da partida em andamento",
        description:
          "Clique em conectar para receber permissão em sua partida em andamento.",
        color: 0x6c3baa,
        footer: {
          text: "League Voice Brasil",
        },
        timestamp: new Date().toISOString(),
      },
    ],
    components: [
      {
        type: 1,
        components: [
          {
            type: 2,
            label: "Conectar",
            style: 1,
            custom_id: "join_button",
          },
        ],
      },
    ],
  };

  await axios.post(
    `${DISCORD_API}/channels/${joinChannelId}/messages`,
    message,
    {
      headers: {
        Authorization: `Bot ${DISCORD_API_KEY}`,
      },
    }
  );
}

export async function commandJoin(res, userId, channelId) {
  try {
    // find the match
    const match = await axios.get(`${API_HOST}/user/match/${userId}`);

    const voiceChannel = 
  } catch (error) {
    if (error.response) {
      if (error.response.status === 404) {
        return res.send({
          type: InteractionResponseType.CHANNEL_MESSAGE_WITH_SOURCE,
          data: {
            content: "Partida não encontrada, tente novamente mais tarde.",
            flags: 1 << 6,
          },
        });
      }
    }
  }

  // const matchChannelData =
  // {
  //   name:
  // }

  // // try to create channel
  // const channel = await axios.post(`${DISCORD_API}/guilds/${GUILD_ID}/channels`, );
}
