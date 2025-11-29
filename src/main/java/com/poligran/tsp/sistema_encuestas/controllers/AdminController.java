package com.poligran.tsp.sistema_encuestas.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poligran.tsp.sistema_encuestas.dtos.EmpresaDTO;
import com.poligran.tsp.sistema_encuestas.dtos.UsuarioDTO;
import com.poligran.tsp.sistema_encuestas.entities.Empresa;
import com.poligran.tsp.sistema_encuestas.entities.Usuario;
import com.poligran.tsp.sistema_encuestas.services.EmpresaService;
import com.poligran.tsp.sistema_encuestas.services.UsuarioService;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final EmpresaService empresaService;
    private final UsuarioService usuarioService;

    @GetMapping("")
    public String home(Model model) {
        var usuarios = usuarioService.listAll()
                .stream()
                .map(u -> UsuarioDTO.builder()
                        .id(u.getId())
                        .email(u.getEmail())
                        .nameEmpresa(u.getEmpresa() != null ? u.getEmpresa().getNombre() : "")
                        .build())
                .toList();

        var empresas = empresaService.listAll();

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("empresas", empresas);
        return "admin";
    }

    @GetMapping("/create/empresa")
    public String createEmpresaForm(Model model) {
        model.addAttribute("empresa", new EmpresaDTO());
        return "create_empresa";
    }

    @PostMapping("/create/empresa")
    public String createEmpresa(EmpresaDTO empresaDTO, RedirectAttributes redirectAttributes) {
        logger.info("Empresa : {}", empresaDTO);
        empresaService.save(empresaDTO);
        redirectAttributes.addFlashAttribute("message", "Empresa creada exitosamente");
        return "redirect:/admin";
    }

    @GetMapping("/edit/empresa/{id}")
    public String editEmpresaForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Empresa empresa = empresaService.findById(id);
            EmpresaDTO empresaDTO = EmpresaDTO.builder()
                    .nombre(empresa.getNombre())
                    .nit(empresa.getNit())
                    .direccion(empresa.getDireccion())
                    .build();

            model.addAttribute("empresa", empresaDTO);
            model.addAttribute("empresaId", id);
            return "create_empresa";
        } catch (Exception e) {
            logger.error("Error al buscar empresa: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Empresa no encontrada");
            return "redirect:/admin";
        }
    }

    @PostMapping("/edit/empresa/{id}")
    public String editEmpresa(@PathVariable Long id, EmpresaDTO empresaDTO, RedirectAttributes redirectAttributes) {
        try {
            Empresa empresa = empresaService.findById(id);
            empresa.setNombre(empresaDTO.getNombre());
            empresa.setNit(empresaDTO.getNit());
            empresa.setDireccion(empresaDTO.getDireccion());

            empresaService.save(empresa);
            logger.info("Empresa actualizada: {}", empresaDTO);
            redirectAttributes.addFlashAttribute("message", "Empresa actualizada exitosamente");
        } catch (Exception e) {
            logger.error("Error al actualizar empresa: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al actualizar empresa");
        }
        return "redirect:/admin";
    }

    @GetMapping("/delete/empresa/{id}")
    public String deleteEmpresa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Empresa empresa = empresaService.findById(id);
            if (empresa.getUserList() != null && !empresa.getUserList().isEmpty()) {
                redirectAttributes.addFlashAttribute("error",
                        "No se puede eliminar la empresa porque tiene usuarios asociados");
            } else {
                empresaService.deleteById(id);
                logger.info("Empresa eliminada con ID: {}", id);
                redirectAttributes.addFlashAttribute("message", "Empresa eliminada exitosamente");
            }
        } catch (Exception e) {
            logger.error("Error al eliminar empresa: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al eliminar empresa");
        }
        return "redirect:/admin";
    }

    @GetMapping("/create/usuario")
    public String createUsuarioForm(Model model) {
        var empresaList = empresaService.listAll();
        model.addAttribute("empresas", empresaList);
        model.addAttribute("usuario", new UsuarioDTO());
        logger.info("{}", empresaList);
        return "create_usuario";
    }

    @PostMapping("/create/usuario")
    public String createUsuario(UsuarioDTO usuario, RedirectAttributes redirectAttributes) {
        try {
            logger.info("User : {}", usuario);
            usuarioService.save(usuario);
            redirectAttributes.addFlashAttribute("message", "Usuario creado exitosamente");
        } catch (Exception e) {
            logger.error("Error al crear usuario: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al crear usuario");
        }
        return "redirect:/admin";
    }

    @GetMapping("/edit/usuario/{id}")
    public String editUsuarioForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.findById(id);
            UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                    .email(usuario.getEmail())
                    .nameEmpresa(usuario.getEmpresa() != null ? usuario.getEmpresa().getNombre() : "")
                    .build();

            var empresaList = empresaService.listAll();
            model.addAttribute("empresas", empresaList);
            model.addAttribute("usuario", usuarioDTO);
            model.addAttribute("usuarioId", id);
            return "edit_usuario";
        } catch (Exception e) {
            logger.error("Error al buscar usuario: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/admin";
        }
    }

    @PostMapping("/edit/usuario/{id}")
    public String editUsuario(@PathVariable Long id, UsuarioDTO usuarioDTO, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.findById(id);
            usuario.setEmail(usuarioDTO.getEmail());

            if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
                usuario.setPassword(usuarioService.getPasswordEncoder().encode(usuarioDTO.getPassword()));
            }

            if (usuarioDTO.getNameEmpresa() != null && !usuarioDTO.getNameEmpresa().isEmpty()) {
                Empresa empresa = empresaService.findByNombre(usuarioDTO.getNameEmpresa());
                usuario.setEmpresa(empresa);
            }

            usuarioService.save(usuario);
            logger.info("Usuario actualizado: {}", usuarioDTO);
            redirectAttributes.addFlashAttribute("message", "Usuario actualizado exitosamente");
        } catch (Exception e) {
            logger.error("Error al actualizar usuario: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al actualizar usuario");
        }
        return "redirect:/admin";
    }

    @GetMapping("/delete/usuario/{id}")
    public String deleteUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deleteById(id);
            logger.info("Usuario eliminado con ID: {}", id);
            redirectAttributes.addFlashAttribute("message", "Usuario eliminado exitosamente");
        } catch (Exception e) {
            logger.error("Error al eliminar usuario: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al eliminar usuario");
        }
        return "redirect:/admin";
    }
}
