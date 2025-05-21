package com.tp4lab4.instrumentos.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tp4lab4.instrumentos.Model.RoleEnum;
import com.tp4lab4.instrumentos.Model.Usuario;
import com.tp4lab4.instrumentos.Repository.UsuarioRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public Usuario getUsuario(Long id){
        Optional<Usuario> usuarioOption = usuarioRepository.findById(id);
        if (!usuarioOption.isPresent()) {
            throw new Error("El usuario no existe");
        }
        Usuario usuarioEncontrado = usuarioOption.get();
        return usuarioEncontrado;
    }

    public Usuario create(Usuario usuario){

        Optional<Usuario> usuarioOption = usuarioRepository.findByNombreUsuario(usuario.getNombreUsuario());
        
        if (usuarioOption.isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }
        
        usuario.setClave(sha1(usuario.getClave()));

        return usuarioRepository.save(usuario);
    }
    
    public Usuario login(String nombreUsuario, String claveIngresada) {
        // Buscar usuario por nombre de usuario
        Optional<Usuario> usuarioOption = usuarioRepository.findByNombreUsuario(nombreUsuario);
        
        if (!usuarioOption.isPresent()) {
            throw new RuntimeException("El usuario no existe");
        }

        Usuario usuario = usuarioOption.get();

        // Encriptar la clave ingresada y compararla
        String claveEncriptada = sha1(claveIngresada);
        
        if (!usuario.getClave().equals(claveEncriptada)) {
            throw new RuntimeException("Clave incorrecta");
        }

        return usuario;
    }

    public static String sha1(String input) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
            byte[] result = mDigest.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                sb.append(String.format("%02x", b));  // convierte a hexadecimal
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
