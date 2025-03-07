package eth22.backroomswanderer.keybinds;

import eth22.backroomswanderer.light.LightManager;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeybindsManager {
    public static final KeyBinding toggleFlashlight = new KeyBinding(
            "key.backroomswanderer.toggle_flashlight",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "category.backroomswanderer"
    );

    public static void register() {
        KeyBindingHelper.registerKeyBinding(toggleFlashlight);
    }

    public static boolean isFlashlightTogglePressed() {
        return toggleFlashlight.wasPressed();
    }

    public static void handleFlashlightToggle() {
        if (isFlashlightTogglePressed()) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                LightManager.toggleDebugEnabled();
                LightManager.updateFlashlights();
            }
        }
    }
}
