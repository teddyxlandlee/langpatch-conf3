package xland.mcmod.enchlevellangpatch.ext.conf3.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod("langpatchconf")
public class LangPatchConfigForge {
    @SuppressWarnings("unused")
    public static boolean is117Compatible() {
        return true;
    }
}
