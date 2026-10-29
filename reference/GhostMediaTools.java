// Extracted reference method; Telegram Android / GhostGram, GPL-2.0.
public static byte[] packWaveform(int[] levels) {
        if (levels == null || levels.length != 100) throw new IllegalArgumentException("Expected 100 waveform levels");
        byte[] packed = new byte[(levels.length * 5 + 7) / 8];
        for (int i = 0; i < levels.length; i++) {
            int value = Math.max(0, Math.min(31, levels[i]));
            int position = i * 5;
            int byteIndex = position / 8;
            int offset = position % 8;
            int spill = Math.max(0, 5 - (8 - offset));
            // Match the stock Android SeekBarWaveform reader, including its
            // ordering of levels which straddle two bytes.
            packed[byteIndex] |= (byte) ((value >> spill) << offset);
            if (spill > 0) packed[byteIndex + 1] |= (byte) (value & ((1 << spill) - 1));
        }
        return packed;
    }
