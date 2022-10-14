package xland.mcmod.enchlevellangpatch.ext.conf3.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;
import org.jetbrains.annotations.ApiStatus;
import xland.mcmod.enchlevellangpatch.ext.conf3.LangPatchConfig;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("unused")
public class LangPatchConfigOverrideEvent extends Event implements IModBusEvent {
    private int val;

    public void noOverrideEnchantment() {
        val |= LangPatchConfig.DEFAULT_ENCHANTMENT;
    }

    public void noOverridePotion() {
        val |= LangPatchConfig.DEFAULT_POTION;
    }

    public void terminateAll() {
        val = 3;
    }

    @ApiStatus.Internal
    public int getVal() {
        return val;
    }
}
