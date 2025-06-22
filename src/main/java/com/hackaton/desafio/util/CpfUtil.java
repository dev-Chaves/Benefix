package com.hackaton.desafio.util;

public class CpfUtil {

    public static boolean isValidCpf(String cpf) {
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            return false; // Verifica se tem 11 dígitos e são todos números
        }

        // Remover CPFs conhecidos e inválidos (ex: todos iguais)
        if (cpf.equals("00000000000") || cpf.equals("11111111111") ||
                cpf.equals("22222222222") || cpf.equals("33333333333") ||
                cpf.equals("44444444444") || cpf.equals("55555555555") ||
                cpf.equals("66666666666") || cpf.equals("77777777777") ||
                cpf.equals("88888888888") || cpf.equals("99999999999")) {
            return false;
        }

        try {
            long cpfNum = Long.parseLong(cpf);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String cleanCpf(String cpf) {
        return cpf.replaceAll("[^0-9]", "");
    }


}
