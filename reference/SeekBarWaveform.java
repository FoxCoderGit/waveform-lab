// Extracted reference method; Telegram Android / GhostGram, GPL-2.0.
private float[] calculateHeights(int count) {
        if (waveformBytes == null || count <= 0) {
            return null;
        }
        float[] heights = new float[count];
        byte value;
        int samplesCount = (waveformBytes.length * 8 / 5);
        float samplesPerBar = samplesCount / (float) count;
        float barCounter = 0;
        int nextBarNum = 0;

        int barNum = 0;
        int lastBarNum;
        int drawBarCount;
        for (int a = 0; a < samplesCount; a++) {
            if (a != nextBarNum) {
                continue;
            }
            drawBarCount = 0;
            lastBarNum = nextBarNum;
            while (lastBarNum == nextBarNum) {
                barCounter += samplesPerBar;
                nextBarNum = (int) barCounter;
                drawBarCount++;
            }

            int bitPointer = a * 5;
            int byteNum = bitPointer / 8;
            int byteBitOffset = bitPointer - byteNum * 8;
            int currentByteCount = 8 - byteBitOffset;
            int nextByteRest = 5 - currentByteCount;
            value = (byte) ((waveformBytes[byteNum] >> byteBitOffset) & ((2 << (Math.min(5, currentByteCount) - 1)) - 1));
            if (nextByteRest > 0 && byteNum + 1 < waveformBytes.length) {
                value <<= nextByteRest;
                value |= waveformBytes[byteNum + 1] & ((2 << (nextByteRest - 1)) - 1);
            }

            for (int b = 0; b < drawBarCount; b++) {
                if (barNum >= heights.length) {
                    return heights;
                }
                heights[barNum++] = Math.max(0, 7 * value / 31.0f);
            }
        }
        return heights;
    }
