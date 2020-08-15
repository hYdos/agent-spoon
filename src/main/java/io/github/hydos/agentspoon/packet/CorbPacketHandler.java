package io.github.hydos.agentspoon.packet;

import com.google.gson.Gson;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.packet.*;
import io.github.hydos.agentspoon.AgentSpoon;
import io.github.hydos.agentspoon.data.ChainDatas;

import java.text.ParseException;

public class CorbPacketHandler extends CorbPacketLogger {

    private static final Gson GSON = new Gson();

    private final BedrockServerSession session;
    private ChainDatas chainDatas;
    private JWT chainData;

    public CorbPacketHandler(BedrockServerSession session) {
        this.session = session;
    }

    @Override
    public boolean handle(LoginPacket packet) {
        super.handle(packet);
        chainDatas = GSON.fromJson(packet.getChainData().toString(), ChainDatas.class);
        try {
            chainData = JWTParser.parse(chainDatas.chain.get(0));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PlayStatusPacket statusPacket = new PlayStatusPacket();
        ResourcePacksInfoPacket resourcePacksInfoPacket = new ResourcePacksInfoPacket();
        statusPacket.setStatus(PlayStatusPacket.Status.LOGIN_SUCCESS);

        session.sendPacket(statusPacket);
        session.sendPacket(resourcePacksInfoPacket);
        return true;
    }

    @Override
    public boolean handle(ResourcePackClientResponsePacket packet) {
        super.handle(packet);
        StartGamePacket startGamePacket = AgentSpoon.javaGameTranslator.addPlayerToWorld();
        session.sendPacket(startGamePacket);

        //TODO: send chunks & biome list

        PlayStatusPacket statusPacket = new PlayStatusPacket();
        statusPacket.setStatus(PlayStatusPacket.Status.PLAYER_SPAWN);
        session.sendPacket(statusPacket);
        return true;
    }
}
