import "dotenv/config";
import { InstallGlobalCommands, InstallGuildCommands } from "../utils.js";

// Simple test command
const TEST_COMMAND = {
  name: "test",
  description: "Basic command",
  type: 1,
  integration_types: [0, 1],
  contexts: [0, 1, 2],
};

const LINKACCOUNT_COMMAND = {
  name: "link",
  description: "Cria a mensagem de conectar a conta",
  type: 1,
  integration_types: [0, 1],
  contexts: [0, 1],
};

// Command to check league user by discord account connection
const LEAGUEINFO_COMMAND = {
  name: "info",
  description: "Veja as informações do league do usuário solicitado",
  type: 1,
  options: [
    {
      name: "usuario",
      description: "Usuario selecionado",
      type: 6,
      required: true,
    },
  ],
  integration_types: [0, 1],
  contexts: [0, 1],
};

// Command to join the voice call of the running game
const JOIN_COMMAND = {
  name: "join",
  type: 3,
  integration_types: [0, 1],
  contexts: [0, 1],
};

const CREATE_JOIN_MSG_COMMAND = {
  name: "cjoin",
  description: "Cria a msg de conectar a call da partida",
  type: 1,
  integration_types: [0, 1],
  contexts: [0, 1],
};

const ALL_COMMANDS = [
  TEST_COMMAND,
  LEAGUEINFO_COMMAND,
  LINKACCOUNT_COMMAND,
  JOIN_COMMAND,
  CREATE_JOIN_MSG_COMMAND,
];

InstallGlobalCommands(process.env.APP_ID, ALL_COMMANDS);
//InstallGuildCommands(process.env.APP_ID, process.env.GUILD_ID, GUILD_COMMANDS);
