package ca.ulaval.glo2004;

import ca.ulaval.glo2004.gui.MainWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        mainWindow.setExtendedState(mainWindow.getExtendedState());
        mainWindow.setVisible(true);
    }
}