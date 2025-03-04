package eth22.backroomswanderer.components;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class FlashlightComponent {
    private boolean flashlightEnabled = false;

    public boolean isFlashlightEnabled() {
        return flashlightEnabled;
    }

    public void toggleFlashlight() {
        flashlightEnabled = !flashlightEnabled;
    }

    public void writeNbt(NbtCompound nbt) {
        nbt.putBoolean("flashlightEnabled", flashlightEnabled);
    }

    public void readNbt(NbtCompound nbt) {
        flashlightEnabled = nbt.getBoolean("flashlightEnabled");
    }

    public static void writeToPacket(PacketByteBuf buf, FlashlightComponent component) {
        buf.writeBoolean(component.flashlightEnabled);
    }

    public static FlashlightComponent readFromPacket(PacketByteBuf buf) {
        FlashlightComponent component = new FlashlightComponent();
        component.flashlightEnabled = buf.readBoolean();
        return component;
    }
}
