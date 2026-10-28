package com.example.GameVault.controllers;

import com.example.GameVault.models.Usuario;
import com.example.GameVault.servicio.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("login")
    public String mostrarLogin(){
        return "login";
    }

    @PostMapping("login")
    public String procesarLogin(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, Model model){
        Usuario usuario = usuarioService.autenticarUsuario(email, password);
        if(usuario != null){
            session.setAttribute("usuario_id", usuario);
            return "redirect:/mis-juegos";
        } else {
            model.addAttribute("error", "Credenciales incorrectas");
            return "login";
        }
    }

    @GetMapping("/registro")
    public String mostrarRegistro(){
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(Usuario usuario){
        usuarioService.registrarUsuario(usuario);
        return "redirect:/login?registrado=true";
    }

    @GetMapping("logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}
