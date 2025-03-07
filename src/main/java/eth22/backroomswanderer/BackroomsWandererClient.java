package eth22.backroomswanderer;

import eth22.backroomswanderer.keybinds.KeybindsManager;
import eth22.backroomswanderer.light.LightManager;
import eth22.backroomswanderer.render.ShaderManager;
import foundry.veil.api.event.VeilRenderLevelStageEvent;
import foundry.veil.fabric.event.FabricVeilRenderLevelStageEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;

import java.util.UUID;

public class BackroomsWandererClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		KeybindsManager.register();
		ShaderManager.initialize();

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			UUID playerUuid = MinecraftClient.getInstance().getSession().getUuidOrNull();
			if (playerUuid == null) return;
			LightManager.onPlayerDisconnected(playerUuid);
		});

		ClientPlayConnectionEvents.JOIN.register((handler, client, isFirstJoin) -> {
			UUID playerUuid = MinecraftClient.getInstance().getSession().getUuidOrNull();
			if (playerUuid != null) {
				LightManager.onPlayerRejoined(playerUuid);
			}
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			KeybindsManager.handleFlashlightToggle();
			KeybindsManager.handleVhsShaderToggle();
			LightManager.removeInactiveFlashlights();
		});

		FabricVeilRenderLevelStageEvent.EVENT.register((stage, levelRenderer, bufferSource, matrixStack, frustumMatrix, projectionMatrix, renderTick, deltaTracker, camera, frustum) -> {
			if (stage == VeilRenderLevelStageEvent.Stage.AFTER_LEVEL) {
				LightManager.updateFlashlights();
				ShaderManager.updateVHSShader();
			}
		});
	}
}

