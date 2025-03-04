package eth22.backroomswanderer;

import eth22.backroomswanderer.light.LightManager;
import eth22.backroomswanderer.light.PlayerFlashlight;
import foundry.veil.api.event.VeilRenderLevelStageEvent;
import foundry.veil.fabric.event.FabricVeilRendererAvailableEvent;
import foundry.veil.fabric.event.FabricVeilRenderLevelStageEvent;
import net.fabricmc.api.ClientModInitializer;

public class BackroomsWandererClient implements ClientModInitializer {
	private final PlayerFlashlight playerFlashlight = new PlayerFlashlight();

	@Override
	public void onInitializeClient() {
		// Capture VeilRenderer once it exists
		FabricVeilRendererAvailableEvent.EVENT.register(veilRenderer -> {
			LightManager.initialize(veilRenderer.getLightRenderer());
		});

		// Every frame, handle light updates and registration/removal
		FabricVeilRenderLevelStageEvent.EVENT.register((stage, levelRenderer, bufferSource, matrixStack, frustumMatrix, projectionMatrix, renderTick, deltaTracker, camera, frustum) -> {
			if (stage == VeilRenderLevelStageEvent.Stage.AFTER_LEVEL) {
				playerFlashlight.update();
				LightManager.registerFlashlight(playerFlashlight);
			}
		});
	}
}
