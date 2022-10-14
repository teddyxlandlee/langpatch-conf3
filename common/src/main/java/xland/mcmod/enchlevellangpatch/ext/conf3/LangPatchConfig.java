package xland.mcmod.enchlevellangpatch.ext.conf3;

import com.google.common.base.Suppliers;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xland.mcmod.enchlevellangpatch.api.EnchantmentLevelLangPatch;
import xland.mcmod.enchlevellangpatch.api.EnchantmentLevelLangPatchConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class LangPatchConfig {
    // often at ./config/enchlevel-langpatch/2conf.txt (for compatibility)
    private final Path configurationFile;
    private ResourceLocation enchantmentCfg, potionCfg;
    static public final int DEFAULT_ENCHANTMENT = 1, DEFAULT_POTION = 2;
    private static final ResourceLocation DEFAULTS_IF_POSSIBLE = new ResourceLocation("enchlevel-langpatch:roman");
    private static final Pattern ID_PATTERN = Pattern.compile("^([a-z0-9\\u002d_]+)?[a-z0-9\\u002d\\u002f_]+$");
    private static final Logger LOGGER = LogManager.getLogger();

    LangPatchConfig(Path configurationFile) {
        this.configurationFile = configurationFile;
    }

    public void loadConfig() {
        try {
            Properties p = defaultProperties();
            if (Files.isRegularFile(configurationFile)) {
                p = new Properties(p);
                p.load(Files.newBufferedReader(configurationFile));
                String s = p.getProperty("enchantment-cfg");
                if (s != null && ID_PATTERN.matcher(s).matches())
                    enchantmentCfg = new ResourceLocation(s);
                s = p.getProperty("potion-cfg");
                if (s != null && ID_PATTERN.matcher(s).matches())
                    potionCfg = new ResourceLocation(s);
            } else {
                p.store(Files.newBufferedWriter(configurationFile), "Change to enchlevel-langpatch:default for numeric format");
            }
        } catch (IOException e) {
            LOGGER.error("conf3.LangPatchConfig: Can't load enchlevel-langpatch config", e);
        }
    }

    private static Properties defaultProperties() {
        Properties p = new Properties();
        final int flag = defaultFlag();
        if ((flag & DEFAULT_ENCHANTMENT) == 0)
            p.setProperty("enchantment-cfg", DEFAULTS_IF_POSSIBLE.toString());
        if ((flag & DEFAULT_POTION) == 0)
            p.setProperty("potion-cfg", DEFAULTS_IF_POSSIBLE.toString());
        return p;
    }

    public void dumpConfig() {
        Properties p = new Properties();
        if (enchantmentCfg != null)
            p.setProperty("enchantment-cfg", enchantmentCfg.toString());
        if (potionCfg != null)
            p.setProperty("potion-cfg", potionCfg.toString());
        try {
            p.store(Files.newBufferedWriter(configurationFile), "Change to enchlevel-langpatch:default for numeric format");
        } catch (IOException e) {
            LOGGER.error("conf3.LangPatchConfig: Failed to dump enchlevel-langpatch config", e);
        }
    }

    public void applyConfig() {
        if (enchantmentCfg != null) {
            final EnchantmentLevelLangPatch langPatch = EnchantmentLevelLangPatchConfig.getEnchantmentHooksContext().get(enchantmentCfg.toString());
            if (langPatch != null)
                EnchantmentLevelLangPatchConfig.setCurrentEnchantmentHooks(langPatch);
            else LOGGER.warn("Missing enchantment patch: {}", enchantmentCfg);
        }
        if (potionCfg != null) {
            final EnchantmentLevelLangPatch langPatch = EnchantmentLevelLangPatchConfig.getPotionHooksContext().get(potionCfg.toString());
            if (langPatch != null)
                EnchantmentLevelLangPatchConfig.setCurrentPotionHooks(langPatch);
            else LOGGER.warn("Missing potion patch: {}", potionCfg);
        }
    }

    private static final Supplier<Integer> DEFAULT_FLAG = Suppliers.memoize(LangPatchConfig::defaultFlag0);
    private static int defaultFlag() {
        return DEFAULT_FLAG.get();
    }

    @ExpectPlatform
    private static int defaultFlag0() { throw new AssertionError("Stub method"); }

    public static void init() {
        final LangPatchConfig instance = getInstance();
        instance.loadConfig();
        instance.applyConfig();
    }

    private static final LangPatchConfig INSTANCE = new LangPatchConfig(getConfigDir().resolve("enchlevel-langpatch").resolve("2conf.txt"));

    @ExpectPlatform
    private static Path getConfigDir() { throw new AssertionError("Stub method"); }

    public static LangPatchConfig getInstance() {
        return INSTANCE;
    }
}
