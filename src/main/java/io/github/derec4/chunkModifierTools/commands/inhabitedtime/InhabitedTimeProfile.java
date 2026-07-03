package io.github.derec4.chunkModifierTools.commands.InhabitedTime;

import java.util.Locale;
import java.util.Random;

public enum InhabitedTimeProfile {

    FLAT {
        @Override
        public double weight(int dx, int dz, int radius, long seed, int chunkX, int chunkZ) {
            return 1.0;
        }
    },

    BELL {
        @Override
        public double weight(int dx, int dz, int radius, long seed, int chunkX, int chunkZ) {
            double distance = Math.sqrt(dx * dx + dz * dz);
            double normalized = radius == 0 ? 0.0 : distance / radius;
            return Math.exp(-(normalized * normalized) / 2.0);
        }
    },

    LINEAR {
        @Override
        public double weight(int dx, int dz, int radius, long seed, int chunkX, int chunkZ) {
            if (radius == 0) {
                return 1.0;
            }
            double distance = Math.sqrt(dx * dx + dz * dz);
            return Math.max(0.0, 1.0 - distance / radius);
        }
    },

    INVERSE {
        @Override
        public double weight(int dx, int dz, int radius, long seed, int chunkX, int chunkZ) {
            double distance = Math.sqrt(dx * dx + dz * dz);
            return 1.0 / (1.0 + distance);
        }
    },

    NOISE {
        @Override
        public double weight(int dx, int dz, int radius, long seed, int chunkX, int chunkZ) {
            double base = BELL.weight(dx, dz, radius, seed, chunkX, chunkZ);
            double jitter = 0.75 + 0.25 * hash01(seed, chunkX, chunkZ);
            return base * jitter;
        }
    };

    public abstract double weight(int dx, int dz, int radius, long seed, int chunkX, int chunkZ);

    public static InhabitedTimeProfile fromString(String value) {
        return valueOf(value.toUpperCase(Locale.ROOT));
    }

    private static double hash01(long seed, int chunkX, int chunkZ) {
        Random random = new Random(seed ^ (chunkX * 341873128712L) ^ (chunkZ * 1323617872L));
        return random.nextDouble();
    }
}
