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
import {
  invalidChannelException,
  outOfPermissionException,
} from "./commandExceptionHandler.js";
import { checkRoles } from "./utils/checkRoles.js";

// Create an express app
const app = express();
// Get port, or default to 3000
const PORT = process.env.PORT || 3000;
const HOST = process.env.HOST;

const API_HOST = process.env.API_HOST;

const DISCORD_API = process.env.DISCORD_API;
const DISCORD_API_KEY = process.env.DISCORD_TOKEN;

const GUILD_ID = process.env.GUILD_ID;
const VERIFIED_ROLE_ID = "1357474271751831795";
const STAFF_ROLE_ID = "1357484525885853898";
const LINK_CHANNEL_ID = "1357483788891984024";

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
      const channelId = req.body.channel.id;

      switch (name) {
        case "test":
          return commandTest(res);

        case "info":
          if (options) {
            const targetId = options[0].value;
            return commandInfo(res, targetId);
          }

        case "link":
          if (channelId != LINK_CHANNEL_ID) {
            return invalidChannelException(res, LINK_CHANNEL_ID);
          } else if (!checkRoles(req, STAFF_ROLE_ID)) {
            return outOfPermissionException(res, STAFF_ROLE_ID);
          }
          return commandLink(res, LINK_CHANNEL_ID);

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

    const oauthToken = await axios.post(`${DISCORD_API}/oauth2/token`, data, {
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
    });

    if (oauthToken.data) {
      const access = oauthToken.data.access_token;

      const userConns = await axios.get(
        `${DISCORD_API}/users/@me/connections`,
        {
          headers: {
            Authorization: `Bearer ${access}`,
          },
        }
      );

      const userIdentity = await axios.get(`${DISCORD_API}/users/@me`, {
        headers: {
          Authorization: `Bearer ${access}`,
        },
      });

      const authorId = userIdentity.data.id;
      let leagueNameId = null;
      // iter connections to find league
      const connections = userConns.data;
      for (let i = 0; i < connections.length; i++) {
        if (connections[i].type === "leagueoflegends") {
          leagueNameId = connections[i].name;
        }
      }

      const createUser = await axios.post(`${API_HOST}/user`, {
        leagueId: leagueNameId,
        discordId: authorId,
      });

      // TODO - RENDERIZAR UMA PAGINA BONITA FALANDO QUE DEU CERTO
      if (createUser.status === 201) {
        await axios.put(
          `${DISCORD_API}/guilds/${GUILD_ID}/members/${authorId}/roles/${VERIFIED_ROLE_ID}`,
          {},
          {
            headers: {
              Authorization: `Bot ${DISCORD_API_KEY}`,
            },
          }
        );

        return res
          .status(200)
          .json({ sucesso: "Usuario linkado com sucesso!" });
      }
    }
  }
});

app.listen(PORT, () => {
  console.log("Listening on port", PORT);
});
