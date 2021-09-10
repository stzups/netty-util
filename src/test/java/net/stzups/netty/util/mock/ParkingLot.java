package net.stzups.netty.util.mock;

import io.netty.buffer.ByteBuf;
import net.stzups.netty.util.DeserializationException;
import net.stzups.netty.util.NettyUtils;
import net.stzups.netty.util.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ParkingLot implements Serializable {
    private final Map<UUID, Car> map;

    public ParkingLot() {
        map = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            map.put(UUID.randomUUID(), new Car());
        }
    }

    public ParkingLot(ByteBuf byteBuf) throws DeserializationException {
        map = NettyUtils.readHashMap32(byteBuf, b -> new UUID(b.readLong(), b.readLong()), Car::new);
    }

    public void serialize(ByteBuf byteBuf) {
        NettyUtils.writeHashMap32(byteBuf, map, (b, uuid) -> {
            b.writeLong(uuid.getMostSignificantBits());
            b.writeLong(uuid.getLeastSignificantBits());
        }, (b, car) -> car.serialize(b));
    }
}
