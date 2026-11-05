package br.com.exemplo.cadastro.controller;

import br.com.exemplo.cadastro.model.CadastroForm;
import br.com.exemplo.cadastro.service.CadastroException;
import br.com.exemplo.cadastro.service.CadastroService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CadastroController {

    private final CadastroService service;

    public CadastroController(CadastroService service) { this.service = service; }

    @GetMapping({"/", "/cadastro"})
    public String form(Model model) {
        model.addAttribute("form", new CadastroForm());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(@Valid @ModelAttribute("form") CadastroForm form,
                            BindingResult result, RedirectAttributes redirect,
                            HttpSession session) {
        if (result.hasErrors()) return "cadastro";
        try {
            var pessoa = service.cadastrar(form, session.getId());
            redirect.addFlashAttribute("nome", pessoa.getNome());
            return "redirect:/sucesso";
        } catch (CadastroException e) {
            result.reject("cadastro.erro", e.getMessage());
            return "cadastro";
        }
    }

    @GetMapping("/sucesso")
    public String sucesso() { return "sucesso"; }

    // Redireciona POST indevido em /sucesso de volta para o formulário
    @PostMapping("/sucesso")
    public String sucessoPost() { return "redirect:/"; }
}

