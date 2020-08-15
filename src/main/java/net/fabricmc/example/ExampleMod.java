package net.fabricmc.example;

import com.nukkitx.protocol.bedrock.BedrockPong;
import com.nukkitx.protocol.bedrock.BedrockServer;
import com.nukkitx.protocol.bedrock.BedrockServerEventHandler;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.v389.Bedrock_v389;
import io.github.hydos.agentspoon.packet.CorbPacketHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

import java.net.InetSocketAddress;

public class ExampleMod implements ModInitializer {

    public static BedrockServer server;

    public static void openBedrockChannel() {
        InetSocketAddress bindAddress = new InetSocketAddress("0.0.0.0", 19132);
        server = new BedrockServer(bindAddress);

        BedrockPong serverPong = new BedrockPong();
        serverPong.setEdition("MCPE");
        serverPong.setMotd("My Server");
        serverPong.setPlayerCount(0);
        serverPong.setMaximumPlayerCount(20);
        serverPong.setGameType("Survival");
        serverPong.setProtocolVersion(Bedrock_v389.V389_CODEC.getProtocolVersion());

        server.setHandler(new BedrockServerEventHandler() {
            @Override
            public boolean onConnectionRequest(InetSocketAddress address) {
                return true;
            }

            @Override
            public BedrockPong onQuery(InetSocketAddress address) {
                return serverPong;
            }

            @Override
            public void onSessionCreation(BedrockServerSession session) {
                session.addDisconnectHandler((reason) -> System.out.println("Disconnected"));
                session.setPacketHandler(new CorbPacketHandler(session));
            }
        });

        server.bind().join();
    }

    @Override
    public void onInitialize() {
        ExampleMod.openBedrockChannel();
    }
}
