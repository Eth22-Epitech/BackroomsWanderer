package eth22.backroomswanderer.light;

import foundry.veil.api.client.render.light.AreaLight;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;

public class PlayerFlashlight {
    private final AreaLight areaLight = new AreaLight();
    private boolean enabled = true;

    public PlayerFlashlight() {
        areaLight.setColor(1.0f, 0.95f, 0.85f);
        areaLight.setBrightness(0.0f);
        areaLight.setSize(0.0f, 0.0f);
        areaLight.setDistance(20.0f);
        areaLight.setAngle(0.6f);
    }

    public void toggle() {
        enabled = !enabled;
        areaLight.setBrightness(enabled ? 2.0f : 0.0f);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public AreaLight getLight() {
        return areaLight;
    }

    public void update() {
        if (!enabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        Camera camera = client.gameRenderer.getCamera();

        areaLight.setTo(camera);
        areaLight.setPosition(player.getX(), player.getY() + 1.6f, player.getZ());
    }
}
