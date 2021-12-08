package com.ads.api.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ads.api.rest.model.Usuario;
import com.ads.api.rest.repository.UsuarioRepository;

@RestController
@RequestMapping(value = "/usuario")
public class IndexController {

	@Autowired
	private UsuarioRepository userRepo;

	/*
	 * @GetMapping(value = "/{id/codigovenda/{venda}", produces = "application/pdf")
	 * public ResponseEntity<Usuario> relatorio(@PathVariable (value = "id") Long id
	 * , @PathVariable (value = "venda") Long venda) {
	 * 
	 * Optional<Usuario> user = userRepo.findById(id);
	 * 
	 * // seria um retorno de relatório return new
	 * ResponseEntity<Usuario>(user.get(),HttpStatus.OK); }
	 */

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> init(@PathVariable(value = "id") Long id) {

		Optional<Usuario> user = userRepo.findById(id);

		return new ResponseEntity<Usuario>(user.get(), HttpStatus.OK);
	}

	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>> user() {

		List<Usuario> list = (List<Usuario>) userRepo.findAll();

		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);

	}

	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> salvar(@RequestBody Usuario user) {

		Usuario userSalvo = userRepo.save(user);

		return new ResponseEntity<Usuario>(userSalvo, HttpStatus.OK);
	}

	/*
	 * @SuppressWarnings({ "unchecked", "rawtypes" })
	 * 
	 * @PostMapping(value = "/{iduser}/idvenda/{idvenda}", produces =
	 * "application/json") public ResponseEntity<?> salvarVendas(@PathVariable Long
	 * idsuer,
	 * 
	 * @PathVariable Long idvenda) {
	 * 
	 * Aqui processar vendas //Usuario userSalvo = userRepo.save(user);
	 * 
	 * return new ResponseEntity("id user :" + idsuer + "idvenda :" + idvenda,
	 * HttpStatus.OK); }
	 */

}
