import "dotenv/config";
import express from "express";
import { commandInfo, commandLink, commandTest } from "./commandHandler.js";
import axios from "axios";
import { URLSearchParams } from "url";
import {
  InteractionResponseType,
  InteractionType,
  verifyKeyMiddleware,
} from "discord-interactions";

// Create an express app
const app = express();
// Get port, or default to 3000
const PORT = process.env.PORT || 3000;
const HOST = process.env.HOST;

/**
 * Interactions endpoint URL where Discord will send HTTP requests
 * Parse request body and verifies incoming requests using discord-interactions package
 */
app.post(
  "/interactions",
  verifyKeyMiddleware(process.env.PUBLIC_KEY),
  async function (req, res) {
    // Interaction id, type and data
    const { id, type, data } = req.body;

    /**
     * Handle verification requests
     */
    if (type === InteractionType.PING) {
      return res.send({ type: InteractionResponseType.PONG });
    }

    /**
     * Handle slash command requests
     * See https://discord.com/developers/docs/interactions/application-commands#slash-commands
     */
    if (type === InteractionType.APPLICATION_COMMAND) {
      const { name, options } = data;
      const authorId = req.body.member.user.id;

      switch (name) {
        case "test":
          return commandTest(res);
        case "info":
          if (options) {
            const targetId = options[0].value;
            return commandInfo(res, targetId);
          }
          return commandInfo(res, targetId);
        case "link":
          return commandLink(res, authorId);
        default:
          console.error(`unknown command: ${name}`);
          return res.status(400).json({ error: "unknown command" });
      }
    }

    console.error("unknown interaction type", type);
    return res.status(400).json({ error: "unknown interaction type" });
  }
);

app.get("/api/auth/discord/redirect", async (req, res) => {
  const { code } = req.query;

  if (code) {
    const data = new URLSearchParams({
      client_id: process.env.APP_ID,
      client_secret: process.env.DISCORD_SECRET,
      grant_type: "authorization_code",
      code: code.toString(),
      redirect_uri: `${HOST}/api/auth/discord/redirect`,
    });

    const oauthToken = await axios.post(
      "https://discord.com/api/v10/oauth2/token",
      data,
      {
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
      }
    );

    if (oauthToken.data) {
      const access = oauthToken.data.access_token;

      const userConns = await axios.get(
        "https://discord.com/api/v10/users/@me/connections",
        {
          headers: {
            Authorization: `Bearer ${access}`,
          },
        }
      );

      const userIdentity = await axios.get(
        "https://discord.com/api/v10/users/@me",
        {
          headers: {
            Authorization: `Bearer ${access}`,
          },
        }
      );

      const authorId = userIdentity.data.id;
      let leagueNameId = null;
      // iter connections to find league
      const connections = userConns.data;
      for (let i = 0; i < connections.length; i++) {
        if (connections[i].type === "leagueoflegends") {
          leagueNameId = connections[i].name;
        }
      }

      const createUser = await axios.post(`${HOST}/api/user`, {
        leagueNameId: leagueNameId,
        authorId: authorId,
      });

      // TODO - RENDERIZAR UMA PAGINA BONITA FALANDO QUE DEU CERTO
      if (createUser.status === 201) {
        return res
          .status(200)
          .json({ sucesso: "Usuario linkado com sucesso!" });
      }
    }
  }
});

app.use(express.json());
app.post("/api/user", async (req, res) => {
  try {
    const { leagueNameId, authorId } = req.body;

    let [leagueUsername, leagueTag] = leagueNameId.split("#");
    const leagueUserData = await axios.get(
      `https://americas.api.riotgames.com/riot/account/v1/accounts/by-riot-id/${leagueUsername}/${leagueTag}?api_key=${process.env.LEAGUE_APIKEY}`
    );
    const leaguePuuid = leagueUserData.data.puuid;

    const newUser = await axios.post(`http://localhost:8080/api/user`, {
      leagueId: leagueNameId,
      leaguePuuid: leaguePuuid,
      discordId: authorId,
    });

    return res.status(201).json({ user: newUser });
  } catch (error) {
    return res.status(500).json({ error: "Erro interno ao criar usuário." });
  }
});

app.listen(PORT, () => {
  console.log("Listening on port", PORT);
});
