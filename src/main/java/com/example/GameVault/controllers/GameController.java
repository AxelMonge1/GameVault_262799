package com.example.GameVault.controllers;

import com.example.GameVault.models.Juego;
import com.example.GameVault.models.Usuario;
import com.example.GameVault.servicio.JuegoService;
import com.example.GameVault.servicio.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    @Autowired
    private UsuarioService usuarioService;

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
    public String mostrarFormulario(Model model){
        model.addAttribute("juego", new Juego());

        return "formulario";
    }

    @GetMapping("/mis-juegos")
    public String dashboard(Model model){
        Usuario logeado = usuarioService.obtenerUsuarioLogeado();
        model.addAttribute("juegos", juegoService.obtenerJuegosPorUsuario(logeado.getId()));
        model.addAttribute("usuario", logeado);
        return "mis-juegos";
    }

    @GetMapping("/juegos")
    public String guardarJuego(@RequestParam("titulo") String titulo,
                               @RequestParam("descripcion") String descripcion,
                               @RequestParam("precio") Double precio,
                               @RequestParam("categoria") String categoria,
                               @RequestParam("portada")MultipartFile portada){
        Juego nuevoJuego = new Juego();
        nuevoJuego.setTitulo(titulo);
        nuevoJuego.setDescripcion(descripcion);
        nuevoJuego.setPrecio(precio);
        nuevoJuego.setCategoria(categoria);

        juegoService.guardarJuego(nuevoJuego, portada);
        return "redirect:/juegos";
    }

    @GetMapping("/juegos/editar/{id}")
    public String editarJuego(@PathVariable Long id, Model model) {
        Juego juego = juegoService.obtenerJuegoPorId(id);
        Usuario logueado = usuarioService.obtenerUsuarioLogeado();
        if (juego != null && juego.getUsuario().getId() == logueado.getId()) {
            model.addAttribute("juego", juego);
            return "formulario";
        }
        return "redirect:/mis-juegos";
    }

    @PostMapping("/juegos/editar/{id}")
    public String actualizarJuego(@PathVariable Long id,
                                  @RequestParam("titulo") String titulo,
                                  @RequestParam("descripcion") String descripcion,
                                  @RequestParam("precio") Double precio,
                                  @RequestParam("categoria") String categoria,
                                  @RequestParam(value = "portada", required = false) MultipartFile portada) {
        Juego juegoExistente = juegoService.obtenerJuegoPorId(id);
        Usuario logueado = usuarioService.obtenerUsuarioLogeado();
        if (juegoExistente != null && juegoExistente.getUsuario().getId() == logueado.getId()) {
            juegoExistente.setTitulo(titulo);
            juegoExistente.setDescripcion(descripcion);
            juegoExistente.setPrecio(precio);
            juegoExistente.setCategoria(categoria);
            juegoService.guardarJuego(juegoExistente, portada);
        }
        return "redirect:/mis-juegos";
    }

    @PostMapping("/juegos/eliminar/{id}")
    public String eliminarJuego(@PathVariable Long id) {
        Juego juego = juegoService.obtenerJuegoPorId(id);
        Usuario logueado = usuarioService.obtenerUsuarioLogeado();
        if (juego != null && juego.getUsuario().getId() == logueado.getId()) {
            juegoService.eliminarJuego(id);
        }
        return "redirect:/mis-juegos";
    }

}