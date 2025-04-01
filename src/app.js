import "dotenv/config";
import express from "express";
import { commandInfo, commandTest } from "./commandHandler.js";
import axios from "axios";
import { URLSearchParams } from "url";
import {
  ButtonStyleTypes,
  InteractionResponseFlags,
  InteractionResponseType,
  InteractionType,
  MessageComponentTypes,
  verifyKeyMiddleware,
} from "discord-interactions";

// Create an express app
const app = express();
// Get port, or default to 3000
const PORT = process.env.PORT || 3000;

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
      const targetId = options[0].value;

      switch (name) {
        case "test":
          return commandTest(res);
        case "info":
          return commandInfo(res, targetId);
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
      redirect_uri:
        "https://4535-187-61-200-25.ngrok-free.app/api/auth/discord/redirect",
    });

    const output = await axios.post(
      "https://discord.com/api/v10/oauth2/token",
      data,
      {
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
      }
    );

    if (output.data) {
      const access = output.data.access_token;

      const userConns = await axios.get(
        "https://discord.com/api/v10/users/@me/connections",
        {
          headers: {
            Authorization: `Bearer ${access}`,
          },
        }
      );

      console.log(userConns.data);
    }
  }
});

app.listen(PORT, () => {
  console.log("Listening on port", PORT);
});
