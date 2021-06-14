package com.fredericoffs.cursomc.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fredericoffs.cursomc.domain.Categoria;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@GetMapping
	public List<Categoria> Listar() {
		Categoria c1 = new Categoria(1, "Informática");
		Categoria c2 = new Categoria(1, "Escritório");
		
		List<Categoria> list = new ArrayList<>(Arrays.asList(c1,c2));
		
		return list;
	}

}
