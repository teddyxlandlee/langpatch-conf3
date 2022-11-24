package xland.mcmod.enchlevellangpatch.ext.conf3.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import xland.mcmod.enchlevellangpatch.ext.conf3.LangPatchConfig;

import java.nio.file.Path;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = "langpatchconf", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LangPatchConfigImpl {
    public static Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static int defaultFlag0() {
        LangPatchConfigOverrideEvent event = new LangPatchConfigOverrideEvent();
        FMLJavaModLoadingContext.get().getModEventBus().post(event);
        return event.getVal();
    }

    @SubscribeEvent
    public static void initMod(FMLCommonSetupEvent event) {
        event.enqueueWork(LangPatchConfig::init);
    }
}
