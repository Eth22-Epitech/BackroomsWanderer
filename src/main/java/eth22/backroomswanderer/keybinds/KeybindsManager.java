package eth22.backroomswanderer.keybinds;

import eth22.backroomswanderer.light.LightManager;
import eth22.backroomswanderer.render.ShaderManager;
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

    public static final KeyBinding toggleVhsShader = new KeyBinding(
            "key.backroomswanderer.toggle_vhs_shader",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "category.backroomswanderer"
    );

    public static void register() {
        KeyBindingHelper.registerKeyBinding(toggleFlashlight);
        KeyBindingHelper.registerKeyBinding(toggleVhsShader);
    }

    public static boolean isFlashlightTogglePressed() {
        return toggleFlashlight.wasPressed();
    }
    public static boolean isVhsShaderTogglePressed() { return toggleVhsShader.wasPressed(); }

    public static void handleFlashlightToggle() {
        if (isFlashlightTogglePressed()) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                LightManager.toggleDebugEnabled();
                LightManager.updateFlashlights();
            }
        }
    }

    public static void handleVhsShaderToggle() {
        if (isVhsShaderTogglePressed()) {
            ShaderManager.toggleVhs();
        }
    }
}
