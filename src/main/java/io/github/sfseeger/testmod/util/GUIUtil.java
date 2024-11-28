package io.github.sfseeger.testmod.util;

public class GUIUtil {
    public static boolean isMouseInBounds(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
