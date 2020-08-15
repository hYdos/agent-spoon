package io.github.hydos.agentspoon.packet;

import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;
import com.nukkitx.protocol.bedrock.v409.Bedrock_v409;

public class CorbPacketHandler extends CorbPacketLogger {

    private final BedrockServerSession session;

    public CorbPacketHandler(BedrockServerSession session) {
        this.session = session;
    }

    @Override
    public boolean handle(LoginPacket packet) {
        PlayStatusPacket statusPacket = new PlayStatusPacket();
        statusPacket.setStatus(PlayStatusPacket.Status.LOGIN_SUCCESS);
        session.sendPacket(statusPacket);
        return true;
    }
}
