package com.tp4lab4.instrumentos.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tp4lab4.instrumentos.Model.Usuario;
import com.tp4lab4.instrumentos.Repository.UsuarioRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Clave secreta para firmar el JWT (idealmente usar una variable de entorno)
    private final String SECRET_KEY = "miClaveSuperSecretaParaJWTmiClaveSuperSecretaParaJWT"; // mínimo 32 caracteres

    public Usuario getUsuario(Long id) {
        Optional<Usuario> usuarioOption = usuarioRepository.findById(id);
        if (!usuarioOption.isPresent()) {
            throw new RuntimeException("El usuario no existe");
        }
        return usuarioOption.get();
    }

    public Usuario create(Usuario usuario) {
        Optional<Usuario> usuarioOption = usuarioRepository.findByNombreUsuario(usuario.getNombreUsuario());
        if (usuarioOption.isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        usuario.setClave(sha1(usuario.getClave()));
        return usuarioRepository.save(usuario);
    }

    public String login(String nombreUsuario, String claveIngresada) {
        Optional<Usuario> usuarioOption = usuarioRepository.findByNombreUsuario(nombreUsuario);
        if (!usuarioOption.isPresent()) {
            throw new RuntimeException("El usuario no existe");
        }

        Usuario usuario = usuarioOption.get();
        String claveEncriptada = sha1(claveIngresada);

        if (!usuario.getClave().equals(claveEncriptada)) {
            throw new RuntimeException("Clave incorrecta");
        }

        // Usamos la nueva forma con Key
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        String token = Jwts.builder()
                .setSubject(usuario.getNombreUsuario())
                .claim("rol", usuario.getRol())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 horas
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public static String sha1(String input) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
            byte[] result = mDigest.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
