package com.bellintegrator.spring_mvc_homework.common;
import java.math.BigDecimal;
import java.util.Random;

public class NumberGenerator {

    public static BigDecimal generateAccountNumber(NumberTypeForGenerator type) {

        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();

        for (int i = 0; i < type.getLength(); i++) {
            int digit = random.nextInt(10);
            accountNumber.append(digit);
        }

        return new BigDecimal(accountNumber.toString());
    }
}
