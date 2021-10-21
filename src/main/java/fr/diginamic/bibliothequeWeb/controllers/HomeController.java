package fr.diginamic.bibliothequeWeb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

	public HomeController() {
		// TODO Auto-generated constructor stub
	}
	
	@GetMapping
	public String home(Model model) {
		String dtitre = "Welcome Bibliotheque app";
		model.addAttribute("titre", dtitre);
		return "homePage";
	}

}
