package dev.risas.zurixgens.utilities;

import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class CurrencyUtil {

    public String format(int number) {
        return String.format(Locale.US, "%,d", number);
    }

    public String format(double number) {
        return String.format(Locale.US, "%,.2f", number);
    }
}
