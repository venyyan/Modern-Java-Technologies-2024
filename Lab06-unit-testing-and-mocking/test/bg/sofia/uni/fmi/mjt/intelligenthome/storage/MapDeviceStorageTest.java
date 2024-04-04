package bg.sofia.uni.fmi.mjt.intelligenthome.storage;

import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDeviceBase;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.RgbBulb;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapDeviceStorageTest {
    @Test
    void testDeleteWithNull() {
        MapDeviceStorage mp = new MapDeviceStorage();
        assertFalse(mp.delete(null));
    }
     @Test
    void testDelete() {
         MapDeviceStorage mp = new MapDeviceStorage();
         IoTDevice dev1 = new RgbBulb("de1", 1, LocalDateTime.now());
         mp.store("1", dev1);
         assertTrue(mp.delete("1"));
    }
}
