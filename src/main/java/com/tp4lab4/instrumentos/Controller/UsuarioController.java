package com.tp4lab4.instrumentos.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tp4lab4.instrumentos.Model.Usuario;
import com.tp4lab4.instrumentos.Service.UsuarioService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@AllArgsConstructor
@RequestMapping("api/v1/usuario")
public class UsuarioController {

    private UsuarioService usuarioService;
    
    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        try {
            System.out.println(usuario);
            return ResponseEntity.ok().body(usuarioService.create(usuario));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error a la hora de crear el usuario: " + e.getMessage());
        }
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody String nombreUsuario, @RequestBody String password) {
        try {
            return ResponseEntity.ok().body(usuarioService.login(nombreUsuario, password));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error de login");
        }
    }
}