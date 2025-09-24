package com.spaceinvaders.util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SomUtil {
    private static Clip musicaFundo;

    public static synchronized void tocarSom(String nomeArquivo) {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                        SomUtil.class.getResourceAsStream("/sons/" + nomeArquivo));
                clip.open(inputStream);
                clip.start();
            } catch (Exception e) {
                System.err.println("Erro ao tocar o som: " + nomeArquivo + " - " + e.getMessage());
            }
        }).start();
    }

    public static synchronized void tocarMusicaFundo(String nomeArquivo) {
        if (musicaFundo != null && musicaFundo.isRunning()) {
            return;
        }
        new Thread(() -> {
            try {
                musicaFundo = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                        SomUtil.class.getResourceAsStream("/sons/" + nomeArquivo));
                musicaFundo.open(inputStream);
                
                // Reduz o volume da música
                FloatControl gainControl = (FloatControl) musicaFundo.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-15.0f); // Reduz em 15 decibéis

                musicaFundo.loop(Clip.LOOP_CONTINUOUSLY);
            } catch (Exception e) {
                System.err.println("Erro ao tocar a música de fundo: " + e.getMessage());
            }
        }).start();
    }

    public static synchronized void pararMusicaFundo() {
        if (musicaFundo != null && musicaFundo.isRunning()) {
            musicaFundo.stop();
        }
    }
}
