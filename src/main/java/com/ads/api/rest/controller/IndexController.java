package com.ads.api.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> update(@RequestBody Usuario user) {

		/* da pra fazer outras coisas antes de atualizar */

		Usuario userSalvo = userRepo.save(user);

		return new ResponseEntity<Usuario>(userSalvo, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String delete(@PathVariable("id") Long id) {
		
		userRepo.deleteById(id); /*Iria deletar todas a vendas do usúario*/
		
		return "usuario Excluido!";
	}
	
	@DeleteMapping(value = "/{id}/venda", produces = "application/text")
	public String deleteVenda(@PathVariable("id") Long id) {
		
		userRepo.deleteById(id);
		
		return "usuario Excluido!";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PutMapping(value = "/{iduser}/idvenda/{idvenda}", produces ="application/json") 
	public ResponseEntity<?> updatevendas(@PathVariable Long idsuer,
	   @PathVariable Long idvenda) {

		/* da pra fazer outras coisas antes de atualizar */

		//Usuario userSalvo = userRepo.save(user);

		return new ResponseEntity("Vendas atualizadas", HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping(value = "/{iduser}/idvenda/{idvenda}", produces ="application/json") 
	public ResponseEntity<?> salvarVendas(@PathVariable Long idsuer,
	   @PathVariable Long idvenda) {
	  
	  //Usuario userSalvo = userRepo.save(user);
	  
	  return new ResponseEntity("id user :" + idsuer + "idvenda :" + idvenda,HttpStatus.OK); }

}
