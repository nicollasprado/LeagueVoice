-- CreateTable
CREATE TABLE "users" (
    "id" TEXT NOT NULL,
    "leagueId" TEXT NOT NULL,
    "discordId" TEXT NOT NULL,

    CONSTRAINT "users_pkey" PRIMARY KEY ("id")
);

-- CreateIndex
CREATE UNIQUE INDEX "users_leagueId_key" ON "users"("leagueId");

-- CreateIndex
CREATE UNIQUE INDEX "users_discordId_key" ON "users"("discordId");
