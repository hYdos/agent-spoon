package io.github.hydos.agentspoon;

import com.nukkitx.protocol.bedrock.BedrockPong;
import com.nukkitx.protocol.bedrock.BedrockServer;
import com.nukkitx.protocol.bedrock.BedrockServerEventHandler;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.v409.Bedrock_v409;
import io.github.hydos.agentspoon.conversion.JavaGameTranslator;
import io.github.hydos.agentspoon.packet.CorbPacketHandler;
import net.fabricmc.api.ModInitializer;

import java.net.InetSocketAddress;

public class AgentSpoon implements ModInitializer {

    public static final boolean DEBUG = true;
    public static BedrockServer server;
    public static JavaGameTranslator javaGameTranslator;

    public static void openBedrockChannel() {
        InetSocketAddress bindAddress = new InetSocketAddress("192.168.0.110", 19132);
        server = new BedrockServer(bindAddress);

        BedrockPong serverPong = new BedrockPong();
        serverPong.setEdition("MCPE");
        serverPong.setMotd("My Server");
        serverPong.setPlayerCount(0);
        serverPong.setMaximumPlayerCount(20);
        serverPong.setGameType("Survival");
        serverPong.setProtocolVersion(Bedrock_v409.V409_CODEC.getProtocolVersion());

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
                session.setPacketCodec(Bedrock_v409.V409_CODEC);
                session.addDisconnectHandler((reason) -> System.out.println("Disconnected"));
                session.setPacketHandler(new CorbPacketHandler(session));
            }
        });

        server.bind().join();
    }

    @Override
    public void onInitialize() {}
}
