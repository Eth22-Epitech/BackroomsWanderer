package eth22.backroomswanderer.render;

import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.post.PostPipeline;
import foundry.veil.api.client.render.post.PostProcessingManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class ShaderManager {
    private static final Identifier VHS_SHADER = Identifier.of("backroomswanderer", "vhs");
    private static boolean vhsEnabled = true;
    private static boolean registered = false;

    public static void initialize() {
        // Optional: Log or do other setup here if needed
    }

    private static void ensureRegistered() {
        if (registered) return;

        boolean added = VeilRenderSystem.renderer().getPostProcessingManager().add(VHS_SHADER);
        if (added) {
            System.out.println("VHS pipeline registered.");
            registered = true;
        } else {
            System.err.println("Failed to register VHS pipeline!");
        }
    }

    public static void toggleVhs() {
        vhsEnabled = !vhsEnabled;
    }

    public static void updateVHSShader() {
        ensureRegistered();

        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getFramebufferWidth();
        int height = client.getWindow().getFramebufferHeight();

        PostProcessingManager manager = VeilRenderSystem.renderer().getPostProcessingManager();
        PostPipeline vhsPipeline = manager.getPipeline(VHS_SHADER);

        if (vhsPipeline != null) {
            vhsPipeline.setFloat("time", (float) client.getRenderTime());
            vhsPipeline.setFloat("resolutionX", (float) width);
            vhsPipeline.setFloat("resolutionY", (float) height);

            manager.runPipeline(vhsPipeline);
        }
    }

}
