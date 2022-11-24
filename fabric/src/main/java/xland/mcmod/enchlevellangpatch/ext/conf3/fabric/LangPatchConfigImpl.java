package xland.mcmod.enchlevellangpatch.ext.conf3.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xland.mcmod.enchlevellangpatch.ext.conf3.LangPatchConfig;

import java.nio.file.Path;
import java.util.Optional;

public class LangPatchConfigImpl {
    public static Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static int defaultFlag0() {
        boolean enchantment = false, potion = false;
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            final ModMetadata metadata = mod.getMetadata();
            if (metadata.containsCustomValue("assets/enchlevel-langpatch-conf3")) {
                final CustomValue val = metadata.getCustomValue("assets/enchlevel-langpatch-conf3");
                if (val.getType() == CustomValue.CvType.OBJECT) {
                    final CustomValue.CvObject obj = val.getAsObject();
                    CustomValue v;
                    if ("assets/enchlevel-langpatch-conf3".equals(metadata.getId()) && (v = obj.get("skipped_mods")) != null) {
                        if (v.getType() == CustomValue.CvType.ARRAY) {
                            for (CustomValue v0 : v.getAsArray()) {
                                if (v0.getType() == CustomValue.CvType.STRING) {
                                    if (FabricLoader.getInstance().isModLoaded(v0.getAsString()))
                                        return 3;
                                } else if (v0.getType() == CustomValue.CvType.OBJECT) {
                                    CustomValue.CvObject obj0 = v0.getAsObject();
                                    String modId = Optional.ofNullable(obj0.get("id"))
                                            .flatMap(LangPatchConfigImpl::flatMapCvString)
                                            .orElse(null);
                                    if (modId == null) continue;
                                    final Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(modId);
                                    if (!modContainer.isPresent()) continue;
                                    final VersionPredicate version = Optional.ofNullable(obj0.get("version"))
                                            .flatMap(LangPatchConfigImpl::flatMapCvString)
                                            .flatMap(s -> {
                                                try {
                                                    return Optional.of(VersionPredicate.parse(s));
                                                } catch (VersionParsingException e) {
                                                    LOGGER.error(e);
                                                    return Optional.empty();
                                                }
                                            })
                                            .orElse(ANY);
                                    if (version.test(modContainer.get().getMetadata().getVersion()))
                                        return 3;
                                }
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

    private static Optional<String> flatMapCvString(CustomValue cv) {
        return cv.getType() == CustomValue.CvType.STRING ?
                Optional.of(cv.getAsString()) :
                Optional.empty();
    }

    private static final Logger LOGGER = LogManager.getLogger();
    private static final VersionPredicate ANY;
    static {
        try {
            ANY = VersionPredicate.parse("*");
        } catch (VersionParsingException e) {
            throw new IncompatibleClassChangeError("Can't parse version predicate *");
        }
    }
}
