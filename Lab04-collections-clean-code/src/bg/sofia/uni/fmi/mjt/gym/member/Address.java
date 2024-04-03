package bg.sofia.uni.fmi.mjt.gym.member;

public record Address(double longitude, double latitude) {
    public double getDistanceTo(Address other) {
        double dx = other.longitude - this.longitude;
        double dy = other.latitude - this.latitude;

        return Math.sqrt(dx * dx + dy * dy);
    }
}
