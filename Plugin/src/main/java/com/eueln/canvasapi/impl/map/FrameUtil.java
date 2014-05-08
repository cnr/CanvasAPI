package com.eueln.canvasapi.impl.map;

import net.minecraft.server.v1_7_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

public class FrameUtil {

    public static EntityItemFrame[] createFrames(MapCanvas canvas) {
        EntityItemFrame[] frames = new EntityItemFrame[canvas.getSections().length];
        for (MapCanvas.CanvasSection section : canvas.getSections()) {

            BlockFace positiveY = BlockFace.DOWN;
            BlockFace positiveX = canvas.getPositiveXFace();

            for (int y = 0; y < canvas.getBlocksHeight(); y++) {
                Vector yOffset = new Vector(0, positiveY.getModY() * y, 0);
                for (int x = 0; x < canvas.getBlocksWidth(); x++) {
                    Vector xOffset = new Vector(positiveX.getModX() * x, 0, positiveX.getModZ() * x);

                    Location loc = canvas.getLocation().add(yOffset).add(xOffset);
                    EntityItemFrame frame = new EntityItemFrame(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), getFaceNumeric(canvas.getBlockFace()));
                    frame.setItem(CraftItemStack.asNMSCopy(new ItemStack(Material.MAP, 1, (short)canvas.getSection(x, y).getMapId())));

                    frames[x + (y * canvas.getBlocksWidth())] = frame;
                }
            }
        }

        return frames;
    }

    private static int getFaceNumeric(BlockFace face) {
        switch (face) {
            case SOUTH:
                return 0;

            case WEST:
                return 1;

            case NORTH:
                return 2;

            case EAST:
                return 3;

            default:
                throw new IllegalArgumentException();
        }
    }

    public static void spawn(Player player, EntityItemFrame[] frames) {
        for (EntityItemFrame frame : frames) {
            PacketPlayOutSpawnEntity spawnPacket = new PacketPlayOutSpawnEntity(frame, 71, frame.direction);
            spawnPacket.a(MathHelper.d((float) (frame.x * 32)));
            spawnPacket.b(MathHelper.d((float) (frame.y * 32)));
            spawnPacket.c(MathHelper.d((float) (frame.z * 32)));
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(spawnPacket);

            PacketPlayOutEntityMetadata metaPacket = new PacketPlayOutEntityMetadata(frame.getId(), frame.getDataWatcher(), false);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(metaPacket);
        }
    }

    public static void destroy(Player player, EntityItemFrame[] frames) {
        for (EntityItemFrame frame : frames) {
            PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(frame.getId());
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(destroyPacket);
        }
    }

    public static void sendSection(List<Player> players, int mapId, byte[][] data) {
        for (Player player : players) {
            for (int x = 0; x < 128; ++x) {
                byte[] bytes = new byte[131];
                bytes[1] = (byte) x;
                for (int y = 0; y < 128; ++y) {
                    bytes[y + 3] = data[x][y];
                }
                PacketPlayOutMap packet = new PacketPlayOutMap(mapId, bytes);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }
}
