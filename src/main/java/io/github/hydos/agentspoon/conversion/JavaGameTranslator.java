package io.github.hydos.agentspoon.conversion;

import com.mojang.authlib.GameProfile;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.data.GamePublishSetting;
import com.nukkitx.protocol.bedrock.data.GameType;
import com.nukkitx.protocol.bedrock.data.SpawnBiomeType;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.level.LevelProperties;

import java.util.UUID;

public class JavaGameTranslator {
    
    public MinecraftServer server;
    
    public JavaGameTranslator(MinecraftServer server) {
        this.server = server;
    }

    public StartGamePacket addPlayerToWorld() {
        //Create funni player
        ServerPlayerEntity player = server.getPlayerManager().createPlayer(new GameProfile(UUID.fromString("836c8cc4-bd0a-45b3-bced-72bbb3af2d0d"), "ramidzkh"));

        WorldProperties levelProperties = server.getOverworld().getLevelProperties();
        BlockPos spawnLocation = server.getOverworld().getSpawnPos();

        StartGamePacket packet = new StartGamePacket();
        packet.setCurrentTick(server.getTicks());
        packet.setAchievementsDisabled(!levelProperties.getGameRules().getBoolean(GameRules.ANNOUNCE_ADVANCEMENTS));
        packet.setDefaultSpawn(Vector3i.ZERO.add(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ()));
        packet.setLevelName("World");
        packet.setPlayerGameType(GameType.SURVIVAL);
        packet.setDimensionId(0);
        packet.setUniqueEntityId(player.getEntityId());
        packet.setBroadcastingToLan(true);
        packet.setDifficulty(2);
        packet.setBlockPalette();
        packet.setDayCycleStopTime(24000);
        packet.setLevelGameType(GameType.SURVIVAL);
        packet.setSeed((int) server.getOverworld().getSeed());
        packet.setXblBroadcastMode(GamePublishSetting.PUBLIC);
        packet.setSpawnBiomeType(SpawnBiomeType.DEFAULT);
        return packet;
    }

}
