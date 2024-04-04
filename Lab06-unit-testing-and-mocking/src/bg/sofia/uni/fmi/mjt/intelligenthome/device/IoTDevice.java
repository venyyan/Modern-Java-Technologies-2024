package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import java.time.LocalDateTime;

public interface IoTDevice {
    String getId();

    String getName();

    double getPowerConsumption();

    LocalDateTime getInstallationDateTime();

    DeviceType getType();

    public long getRegistration();

    public void setRegistration(LocalDateTime registration);

    public long getPowerConsumptionKWh();
}