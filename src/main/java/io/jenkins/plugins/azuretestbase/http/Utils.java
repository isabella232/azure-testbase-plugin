package io.jenkins.plugins.azuretestbase.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    // read all content from given inputstream
    public static byte[] readAllBytes(InputStream inputstream) throws IOException {
        // read bytes piecewise
        List<Byte> garbages = new ArrayList<>();
        // read one byte per loop and save to a List
        while(true) {
            int b = inputstream.read();
            if(b < 0)
                break;
            garbages.add((byte)b);
        }
        // assemble the final string
        byte[] blocks = new byte[garbages.size()];
        for(int i = 0; i < garbages.size(); i++)
            blocks[i] = garbages.get(i);
        return blocks;
    }
}
