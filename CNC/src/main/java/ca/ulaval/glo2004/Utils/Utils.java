package ca.ulaval.glo2004.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final double rate = 25.4;
    private static final double dpi = 96;

    public static double convertToMm(double po) {
        return roundToTwoDecimals(po * rate);
    }

    public static double convertToPo(double mm) {
        return roundToTwoDecimals(mm / rate);
    }

    public static double convertPixelsToMm(double pixels) {
        return roundToTwoDecimals((pixels / dpi) * rate);
    }

    public static double convertMmToPixels(double mm) {
        return roundToTwoDecimals((mm / rate) * dpi);
    }

    public static double convertMetersToPixels(double meters) {
        return roundToTwoDecimals((convertMetersToMm(meters) / rate) * dpi);
    }

    public static double convertMetersToMm(double meters) {
        return roundToTwoDecimals(meters * 1000);
    }

    public static double convertPoToPixels(double po) {
        return roundToTwoDecimals(po * dpi);
    }

    public static double convertFeetToPixels(double feet) {
        return roundToTwoDecimals(convertPoToPixels(feet * 12));
    }

    private static double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public static double convertValueToPixel(double value, String type) {
        double newValue = 0;
        switch (type) {
            case "mm" -> newValue = Utils.convertMmToPixels(value);
            case "cm" -> newValue = Utils.convertMmToPixels(value*10);
            case "m" -> newValue = Utils.convertMetersToPixels(value);
            case "in", "po" -> newValue = Utils.convertPoToPixels(value);
            case "pi", "feet" -> newValue = Utils.convertFeetToPixels(value);
        };
        return newValue;
    }

    public static double convertValueToUnit(double value, String unit){

        double newValue = 0;
        switch (unit) {
            case "mm", "px" -> newValue = Utils.convertPixelsToMm(value);
            case "cm" -> newValue = Utils.convertPixelsToMm(value/10); //À changer plus tard
            case "m" -> newValue = Utils.convertPixelsToMm(value/1000);
            case "in", "po" -> newValue = Utils.convertToPo(convertPixelsToMm(value));
            case "pi", "feet" -> newValue = Utils.convertToPo(convertPixelsToMm(value))*12;
        }
        return newValue;
    }

    public static double parseMetricValue(String input) throws IllegalArgumentException {
        // Retirer les espaces pour simplifier le traitement
        input = input.trim().replaceAll(" ", "");

        // Regex pour capturer les formats valides (partie entière et/ou fraction)
        String regex = "^(\\d+(\\.\\d+)?)?(\"?(\\d+)/(\\d+))?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            double wholeNumber = 0.0;

            if (matcher.group(1) != null) {
                wholeNumber = Double.parseDouble(matcher.group(1));
            }

            // Si une fraction est présente
            if (matcher.group(4) != null && matcher.group(5) != null) {
                double numerator = Double.parseDouble(matcher.group(4));
                double denominator = Double.parseDouble(matcher.group(5));

                if (denominator == 0) {
                    throw new IllegalArgumentException("Le dénominateur ne peut pas être zéro.");
                }

                return wholeNumber + (numerator / denominator);
            }

            return wholeNumber;
        }

        throw new IllegalArgumentException("Format invalide : " + input);
    }


}