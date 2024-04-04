package bg.sofia.uni.fmi.mjt.intelligenthome.center;

import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceNotFoundException;
import bg.sofia.uni.fmi.mjt.intelligenthome.storage.DeviceStorage;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.AmazonAlexa;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.RgbBulb;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.WiFiThermostat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IntelligentHomeCenterTest {

    @Mock
    private DeviceStorage deviceStorageMock;
    @InjectMocks
    private IntelligentHomeCenter intelligentHomeCenter;

    @Test
    void testRegisterWithNullDevice() {
        assertThrows(IllegalArgumentException.class,
            () -> intelligentHomeCenter.register(null),
            "IllegalArgumentException expected to be thrown when trying to register with null device!");
    }

    @Test
    void testRegisterWithAlreadyRegisteredDevice() {
        IoTDevice registered = new WiFiThermostat("ther", 1, LocalDateTime.now());
        when(registered.getId()).thenReturn(any());
        when(deviceStorageMock.exists(registered.getId())).thenReturn(true);

        Assertions.assertThrows(DeviceAlreadyRegisteredException.class, () -> intelligentHomeCenter.register(registered),
            "DeviceAlreadyRegisteredException expected to be thrown " +
                "when trying to register already registered device!");
    }

    @Test
    void testRegisterWithNewDevice() {
        IoTDevice newDev = new WiFiThermostat("ther", 1, LocalDateTime.now());
        when(deviceStorageMock.exists(any())).thenReturn(false);
        assertDoesNotThrow(() -> intelligentHomeCenter.register(newDev));
    }

    @Test
    void testUnregisterWithNullDevice() {
        assertThrows(IllegalArgumentException.class,
            () -> intelligentHomeCenter.unregister(null),
            "IllegalArgumentException expected to be thrown when trying to register with null device!");
    }

    @Test
    void testUnregisterWithNonExistingDevice() {
        IoTDevice nonExisting = new WiFiThermostat("ther", 1, LocalDateTime.now());
        when(deviceStorageMock.exists(any())).thenReturn(false);
        Assertions.assertThrows(DeviceNotFoundException.class,
            () -> intelligentHomeCenter.unregister(nonExisting),
            "DeviceNotFoundException expected to be thrown " +
                "when trying to unregister with non-existing device!");
    }

    @Test
    void testUnregisterWithExistingDevice() {
        IoTDevice existing = new WiFiThermostat("ther", 1, LocalDateTime.now());
        when(deviceStorageMock.exists(any())).thenReturn(true);
        assertDoesNotThrow(() -> intelligentHomeCenter.unregister(existing));
    }

    @Test
    void testGeDeviceByIdWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> intelligentHomeCenter.getDeviceById(null),
            "IllegalArgumentException expected to be thrown when trying to get device with null id!");
    }

    @Test
    void testGeDeviceByIdWithEmptyId() {
        assertThrows(IllegalArgumentException.class, () -> intelligentHomeCenter.getDeviceById(""),
            "IllegalArgumentException expected to be thrown when trying to get device with empty id!");
    }

    @Test
    void testGeDeviceByIdWithBlankId() {
        assertThrows(IllegalArgumentException.class, () -> intelligentHomeCenter.getDeviceById(""),
            "IllegalArgumentException expected to be thrown when trying to get device with blank id!");
    }

    @Test
    void testGetDeviceByIdWithNonExistingDevice() {
        when(deviceStorageMock.exists("Non-existing device")).thenReturn(false);
        assertThrows(DeviceNotFoundException.class, () -> intelligentHomeCenter.getDeviceById("Non-existing device"),
            "DeviceNotFoundException expected to be thrown " +
                "when trying to get id of non-existing device!");
    }

    @Test
    void testGetDeviceByIdWithExistingDevice() {
        IoTDevice device = new RgbBulb("dev1", 1, LocalDateTime.now());
        when(deviceStorageMock.exists(device.getId())).thenReturn(true);
        when(deviceStorageMock.get(device.getId())).thenReturn(device);
        assertDoesNotThrow(() -> intelligentHomeCenter.getDeviceById(device.getId()));

        assertEquals(device, deviceStorageMock.get(device.getId()));
    }

    @Test
    void testGetDeviceQuantityPerTypeWithNullType() {
        assertThrows(IllegalArgumentException.class, () -> intelligentHomeCenter.getDeviceQuantityPerType(null),
            "IllegalArgumentException expected to be thrown " +
                "when trying to get device quantity with null type!");
    }

    @Test
    void testGetDeviceQuantityPerTypeWithNoDevices() {
        List<IoTDevice> devices = Arrays.asList();
        when(deviceStorageMock.listAll()).thenReturn(devices);
        assertEquals(0, intelligentHomeCenter.getDeviceQuantityPerType(DeviceType.THERMOSTAT),
            "Device quantity of empty storage must be 0, but was "
                + intelligentHomeCenter.getDeviceQuantityPerType(DeviceType.THERMOSTAT));
    }

    @Test
    void testGetDeviceQuantityPerTypeBulb() {
        IoTDevice bulb = new RgbBulb("bulb1", 1, LocalDateTime.now());
        IoTDevice bulb2 = new RgbBulb("bulb2", 1, LocalDateTime.now());
        List<IoTDevice> devices = Arrays.asList(bulb, bulb2);
        when(deviceStorageMock.listAll()).thenReturn(devices);
        assertEquals(2, intelligentHomeCenter.getDeviceQuantityPerType(DeviceType.BULB),
            "");
    }

    @Test
    void testGetDeviceQuantityPerTypeThermostat() {
        IoTDevice ther1 = new WiFiThermostat("thermostat1", 1, LocalDateTime.now());
        IoTDevice ther2 = new WiFiThermostat("thermostat2", 1, LocalDateTime.now());
        List<IoTDevice> devices = Arrays.asList(ther1, ther2);
        when(deviceStorageMock.listAll()).thenReturn(devices);
        assertEquals(2, intelligentHomeCenter.getDeviceQuantityPerType(DeviceType.THERMOSTAT),
            "");
    }

    @Test
    void testGetDeviceQuantityPerTypeSmartSpeaker() {
        IoTDevice ther1 = new AmazonAlexa("thermostat1", 1, LocalDateTime.now());
        IoTDevice ther2 = new AmazonAlexa("thermostat2", 1, LocalDateTime.now());
        List<IoTDevice> devices = Arrays.asList(ther1, ther2);
        when(deviceStorageMock.listAll()).thenReturn(devices);
        assertEquals(2, intelligentHomeCenter.getDeviceQuantityPerType(DeviceType.SMART_SPEAKER),
            "");
    }

    @Test
    void testGetDeviceQuantityPerTypeDifferentTypes() {
        IoTDevice dev1 = new AmazonAlexa("alexa", 1, LocalDateTime.now());
        IoTDevice dev2 = new RgbBulb("bulb", 1, LocalDateTime.now());
        IoTDevice dev3 =  new AmazonAlexa(("alexa2"), 1, LocalDateTime.now());
        IoTDevice dev4 = new WiFiThermostat("thermostat", 1, LocalDateTime.now());
        List<IoTDevice> devices = Arrays.asList(dev1, dev2, dev3, dev4);
        when(deviceStorageMock.listAll()).thenReturn(devices);
        assertEquals(2, intelligentHomeCenter.getDeviceQuantityPerType(DeviceType.SMART_SPEAKER),
            "");
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionWithZero() {
        assertThrows(IllegalArgumentException.class,
            () -> intelligentHomeCenter.getTopNDevicesByPowerConsumption(0),
            "IllegalArgumentException expected to be thrown" +
                " when trying to get top zero devices by power consumption!");
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionWithNegative() {
        assertThrows(IllegalArgumentException.class,
            () -> intelligentHomeCenter.getTopNDevicesByPowerConsumption(-7),
            "IllegalArgumentException expected to be thrown" +
                " when trying to get top devices by power consumption with negative number!");
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionWithListSize() {
        IoTDevice dev1 = new RgbBulb("bulb", 1, LocalDateTime.now());
        IoTDevice dev2 = new WiFiThermostat("thermostat", 1, LocalDateTime.now());
        List<IoTDevice> list = Arrays.asList(dev1, dev2);
        when(deviceStorageMock.listAll()).thenReturn(list);
        assertEquals(intelligentHomeCenter.getTopNDevicesByPowerConsumption(2).size(), 2);
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionWithMoreThanListSize() {
        IoTDevice dev1 = new RgbBulb("bulb", 1, LocalDateTime.now());
        IoTDevice dev2 = new WiFiThermostat("thermostat", 1, LocalDateTime.now());
        List<IoTDevice> list = Arrays.asList(dev1, dev2);
        when(deviceStorageMock.listAll()).thenReturn(list);
        assertEquals(2, intelligentHomeCenter.getTopNDevicesByPowerConsumption(5).size());
    }

    @Test
    void testGetTopNDevicesByPowerConsumption() {
        IoTDevice dev1 = new RgbBulb("dev1", 1, LocalDateTime.now());
        IoTDevice dev2 = new WiFiThermostat("dev2", 2, LocalDateTime.now());
        IoTDevice dev3 = new WiFiThermostat("dev3", 3, LocalDateTime.now());
        IoTDevice dev4 = new RgbBulb("dev4", 4, LocalDateTime.now());
        IoTDevice dev5 = new RgbBulb("dev5", 5, LocalDateTime.now());
        List<IoTDevice> list = Arrays.asList(dev1, dev2, dev3, dev4, dev5);
        when(deviceStorageMock.listAll()).thenReturn(list);
        assertEquals(2, intelligentHomeCenter.getTopNDevicesByPowerConsumption(2).size());

        Collection<String> returnedCollection = intelligentHomeCenter.getTopNDevicesByPowerConsumption(2);
        assertEquals(dev1.getId(), returnedCollection.toArray()[0]);
        assertEquals(dev2.getId(), returnedCollection.toArray()[1]);
    }

    @Test
    void testGetFirstNDevicesByRegistrationWithZero() {
        assertThrows(IllegalArgumentException.class,
            () -> intelligentHomeCenter.getFirstNDevicesByRegistration(0),
            "IllegalArgumentException expected to be thrown" +
                " when trying to get top zero devices by registration!");
    }

    @Test
    void testGetFirstNDevicesByRegistrationWithNegative() {
        assertThrows(IllegalArgumentException.class,
            () -> intelligentHomeCenter.getFirstNDevicesByRegistration(-7),
            "IllegalArgumentException expected to be thrown" +
                " when trying to get top negative devices by registration!");
    }

    @Test
    void testGetFirstNDevicesByRegistrationWithMoreThanListSize() {
        IoTDevice dev1 = new RgbBulb("bulb", 1, LocalDateTime.now());
        IoTDevice dev2 = new WiFiThermostat("thermostat", 1, LocalDateTime.now());
        dev1.setRegistration(LocalDateTime.now());
        dev2.setRegistration(LocalDateTime.now());
        List<IoTDevice> list = Arrays.asList(dev1, dev2);
        when(deviceStorageMock.listAll()).thenReturn(list);
        Collection<IoTDevice> list1 = intelligentHomeCenter.getFirstNDevicesByRegistration(5);
        assertEquals(2, list1.size());
    }

    @Test
    void testGetFirstNDevicesByRegistration() {
        IoTDevice dev1 = new RgbBulb("bulb", 1, LocalDateTime.now());
        IoTDevice dev2 = new WiFiThermostat("thermostat", 1, LocalDateTime.now());
        IoTDevice dev3 = new WiFiThermostat("dev3", 3, LocalDateTime.now());
        IoTDevice dev4 = new RgbBulb("dev4", 4, LocalDateTime.now());
        IoTDevice dev5 = new RgbBulb("dev5", 4, LocalDateTime.now());
        dev1.setRegistration(LocalDateTime.now());
        dev2.setRegistration(LocalDateTime.now().plusHours(2));
        dev3.setRegistration(LocalDateTime.now().plusHours(4));
        dev4.setRegistration(LocalDateTime.now().plusHours(6));
        dev5.setRegistration(LocalDateTime.now().plusHours(8));

        List<IoTDevice> list = Arrays.asList(dev1, dev2, dev3, dev4, dev5);
        when(deviceStorageMock.listAll()).thenReturn(list);
        Collection<IoTDevice> returnedList = intelligentHomeCenter.getFirstNDevicesByRegistration(3);
        assertEquals(3, returnedList.size());

        assertEquals(dev5, returnedList.toArray()[0]);
        assertEquals(dev4, returnedList.toArray()[1]);
        assertEquals(dev3, returnedList.toArray()[2]);
    }
}