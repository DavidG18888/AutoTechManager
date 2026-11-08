/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidades;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {

    private static final int ITERACIONES = 65536;
    private static final int LONGITUD_HASH = 256;
    private static final int LONGITUD_SALT = 16;

    public static String generarHash(String contrasena) {

        try {
            SecureRandom random = new SecureRandom();

            byte[] salt = new byte[LONGITUD_SALT];
            random.nextBytes(salt);

            KeySpec spec = new PBEKeySpec(
                    contrasena.toCharArray(),
                    salt,
                    ITERACIONES,
                    LONGITUD_HASH
            );

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            return "pbkdf2$" + ITERACIONES + "$"
                    + Base64.getEncoder().encodeToString(salt) + "$"
                    + Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el hash.", e);
        }
    }

    public static boolean verificarContrasena(
            String contrasena,
            String hashGuardado) {

        try {
            String[] partes = hashGuardado.split("\\$");

            if (partes.length != 4 || !partes[0].equals("pbkdf2")) {
                return false;
            }

            int iteraciones = Integer.parseInt(partes[1]);

            byte[] salt = Base64.getDecoder().decode(partes[2]);
            byte[] hashOriginal = Base64.getDecoder().decode(partes[3]);

            KeySpec spec = new PBEKeySpec(
                    contrasena.toCharArray(),
                    salt,
                    iteraciones,
                    hashOriginal.length * 8
            );

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] hashNuevo = factory.generateSecret(spec).getEncoded();

            return java.security.MessageDigest.isEqual(
                    hashOriginal, hashNuevo
            );

        } catch (Exception e) {
            return false;
        }
    }
}