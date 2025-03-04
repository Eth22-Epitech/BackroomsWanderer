package eth22.backroomswanderer.light;

import foundry.veil.api.client.render.light.renderer.LightRenderer;

public class LightManager {
    private static LightRenderer lightRenderer = null;
    private static boolean flashlightRegistered = false;

    public static void initialize(LightRenderer renderer) {
        lightRenderer = renderer;
    }

    public static void registerFlashlight(PlayerFlashlight flashlight) {
        if (lightRenderer == null) return;

        if (flashlight.isEnabled() && !flashlightRegistered) {
            lightRenderer.addLight(flashlight.getLight());
            flashlightRegistered = true;
        } else if (!flashlight.isEnabled() && flashlightRegistered) {
            lightRenderer.removeLight(flashlight.getLight());
            flashlightRegistered = false;
        }
    }
}
