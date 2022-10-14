package xland.mcmod.enchlevellangpatch.ext.conf3.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
import xland.mcmod.enchlevellangpatch.ext.conf3.LangPatchConfig;

import java.nio.file.Path;

public class LangPatchConfigImpl {
    public static Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static int defaultFlag0() {
        boolean enchantment = false, potion = false;
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            final ModMetadata metadata = mod.getMetadata();
            if (metadata.containsCustomValue("enchlevel-langpatch-conf3")) {
                final CustomValue val = metadata.getCustomValue("enchlevel-langpatch-conf3");
                if (val.getType() == CustomValue.CvType.OBJECT) {
                    final CustomValue.CvObject obj = val.getAsObject();
                    CustomValue v;
                    if ("enchlevel-langpatch-conf3".equals(metadata.getId()) && (v = obj.get("skipped_mods")) != null) {
                        if (v.getType() == CustomValue.CvType.ARRAY) {
                            for (CustomValue v0 : v.getAsArray()) {
                                if (v0.getType() == CustomValue.CvType.STRING && FabricLoader.getInstance().isModLoaded(v0.getAsString()))
                                    return 3;
                            }
                        }
                    }

                    if (!enchantment && (v = obj.get("no_override_enchantment")) != null) {
                        if (v.getType() == CustomValue.CvType.BOOLEAN && v.getAsBoolean())
                            enchantment = true;
                    }
                    if (!potion && (v = obj.get("no_ovverride_potion")) != null) {
                        if (v.getType() == CustomValue.CvType.BOOLEAN && v.getAsBoolean())
                            potion = true;
                    }
                }
            }
            if (enchantment && potion) return 3;
        }
        int ret = 0;
        if (enchantment) ret += LangPatchConfig.DEFAULT_ENCHANTMENT;
        if (potion) ret += LangPatchConfig.DEFAULT_POTION;
        return ret;
    }
}
