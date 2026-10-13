package com.example.GameVault.controllers;

import com.example.GameVault.models.Juego;
import com.example.GameVault.servicio.JuegoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class GameController {
    @Autowired
    private JuegoService juegoService;

    @GetMapping("/framents-demo")
    public String fragmentsDemo(){
        return "fragments-demo";
    }

    @GetMapping({"/", "/juegos"})
    public String listarJuegos(@RequestParam(name = "buscar" ,required = false) String buscar,Model model){
        if(buscar != null && !buscar.isEmpty()){
            model.addAttribute("juegos", juegoService.buscarporTitulo(buscar));
            model.addAttribute("busquedaActual", buscar);
        } else {
            model.addAttribute("juegos", juegoService.obtenerTodos());
        }
        return "juegos";
    }

    @GetMapping("/juegos/nuevo")
    public String mostrarFormulario(){
        return "formulario";
    }

    @GetMapping("/juegos")
    public String guardarJuego(@RequestParam("titulo") String titulo,
                               @RequestParam("descripcion") String descripcion,
                               @RequestParam("portada")MultipartFile portada){
        Juego nuevoJuego = new Juego();
        nuevoJuego.setTitulo(titulo);
        nuevoJuego.setDescripcion(descripcion);

        juegoService.guardarJuego(nuevoJuego, portada);
        return "redirect:/juegos";
    }
}
