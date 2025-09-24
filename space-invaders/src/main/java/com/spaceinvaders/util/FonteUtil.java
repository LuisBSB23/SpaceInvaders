package com.spaceinvaders.util;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;

public class FonteUtil {
    private static Font fonteJogo;

    static {
        try {
            InputStream is = FonteUtil.class.getResourceAsStream("/fontes/PressStart2P.ttf");
            fonteJogo = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(fonteJogo);
        } catch (Exception e) {
            System.err.println("Erro ao carregar a fonte personalizada.");
            e.printStackTrace();
            fonteJogo = new Font("Arial", Font.PLAIN, 12); // Fallback
        }
    }

    public static Font getFonte(float tamanho) {
        return fonteJogo.deriveFont(tamanho);
    }
}
