package eth22.backroomswanderer.render;

import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.post.PostPipeline;
import foundry.veil.api.client.render.post.PostProcessingManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class ShaderManager {
    private static final Identifier VHS_SHADER = Identifier.of("backroomswanderer", "shaders/post/vhs.json");
    private static boolean vhsEnabled = false;

    public static void initialize() {}

    public static void updateVHSShader() {
        MinecraftClient client = MinecraftClient.getInstance();
        float time = (float) client.getRenderTime(); // This is your "elapsed game time" in ticks

        PostProcessingManager manager = VeilRenderSystem.renderer().getPostProcessingManager();
        PostPipeline vhsPipeline = manager.getPipeline(VHS_SHADER);

        if (vhsPipeline != null) {
            vhsPipeline.setFloat("time", time);
        }
    }
}
