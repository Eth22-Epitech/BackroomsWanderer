package eth22.backroomswanderer;

import eth22.backroomswanderer.keybinds.KeybindsManager;
import eth22.backroomswanderer.light.LightManager;
import foundry.veil.api.event.VeilRenderLevelStageEvent;
import foundry.veil.fabric.event.FabricVeilRenderLevelStageEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class BackroomsWandererClient implements ClientModInitializer {

@Override
public void onInitializeClient() {
	// Handle player disconnect
	ClientPlayConnectionEvents.DISCONNECT.register(((clientPlayNetworkHandler, minecraftClient) -> {
		minecraftClient.execute(() -> {
			LightManager.removeInactiveFlashlights();
		});
	}));

	// Register keybinds and flashlight handling
	KeybindsManager.register();

	// Listen for keybind presses
	ClientTickEvents.END_CLIENT_TICK.register(client -> {
		KeybindsManager.handleFlashlightToggle();
	});

	// Handle flashlight rendering
	FabricVeilRenderLevelStageEvent.EVENT.register((stage, levelRenderer, bufferSource, matrixStack, frustumMatrix, projectionMatrix, renderTick, deltaTracker, camera, frustum) -> {
		if (stage == VeilRenderLevelStageEvent.Stage.AFTER_LEVEL) {
			LightManager.updateFlashlights();
		}
	});
	}
}

