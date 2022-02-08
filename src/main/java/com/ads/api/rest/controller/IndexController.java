package com.ads.api.rest.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ads.api.rest.model.UserDTO;
import com.ads.api.rest.model.Usuario;
import com.ads.api.rest.repository.UsuarioRepository;
import com.google.gson.Gson;

@CrossOrigin(origins = "*")
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
	
	// Serviço RESTful
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<UserDTO> init(@PathVariable(value = "id") Long id) {

		Optional<Usuario> user = userRepo.findById(id);

		return new ResponseEntity<UserDTO>(new UserDTO(user.get()), HttpStatus.OK);
	}
	
	/*GERAR UMA LISTA DE USUARIO*/

	@GetMapping(value = "/", produces = "application/json")
	@CacheEvict(value = "cacheusuarios", allEntries = true)
	@CachePut("cacheusuarios")
	public ResponseEntity<List<Usuario>> user() throws InterruptedException {

		List<Usuario> list = (List<Usuario>) userRepo.findAll();
		
	//	Thread.sleep(6000); // segura 6 segundos

		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);

	}

	/* SALVAR USUARIO*/
	
	@PostMapping(value = "/", produces = "application/json")
	
	public ResponseEntity<Usuario> salvar(@RequestBody Usuario user) throws Exception {
		
		/*faz um insert no banco relacionando o Id > telefone com Id > usuario*/
		for (int pos = 0; pos < user.getTelefones().size(); pos++) {
			
			user.getTelefones().get(pos).setUsuario(user);
		}
		
		// Cosumindo API publica externa 
		URL url = new URL("https://viacep.com.br/ws/"+user.getCep()+"/json/");
		URLConnection connection = url.openConnection();
		InputStream stream = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		
		String cep = "";
		StringBuilder jsonCep = new StringBuilder();
		
		while ((cep = reader.readLine()) != null) {
			jsonCep.append(cep);
		}
		

		Usuario userAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);
		
		user.setCep(userAux.getCep());
		user.setLogradouro(userAux.getLogradouro());
		user.setComplemento(userAux.getComplemento());
		user.setBairro(userAux.getBairro());
		user.setLocalidade(userAux.getLocalidade());
		user.setUf(userAux.getUf());
		
		
		// Cosumindo API publica externa 
		
		String senhacriptografada = new BCryptPasswordEncoder().encode(user.getSenha());
		user.setSenha(senhacriptografada);
		
		Usuario userSalvo = userRepo.save(user);

		return new ResponseEntity<Usuario>(userSalvo, HttpStatus.OK);
	}
	
	/*ATUALIZAR USUARIO*/

	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> update(@RequestBody Usuario user) {

		/* da pra fazer outras coisas antes de atualizar */

		/*faz um insert no banco relacionando o Id > telefone com Id > usuario*/
		for (int pos = 0; pos < user.getTelefones().size(); pos++) {
			
			user.getTelefones().get(pos).setUsuario(user);
		}
		
		Usuario usertemporario = userRepo.findUserByLogin(user.getLogin());
		
		if (!usertemporario.getSenha().equals(user.getSenha())) { // senhas diferentes 
			
			String senhacriptografada = new BCryptPasswordEncoder().encode(user.getSenha());
			user.setSenha(senhacriptografada);
			
		} 
		
		Usuario userSalvo = userRepo.save(user);

		return new ResponseEntity<Usuario>(userSalvo, HttpStatus.OK);
	}
	
	/*EXCLUIR USUARIO*/

	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String delete(@PathVariable("id") Long id) {

		userRepo.deleteById(id); /* Iria deletar todas a vendas do usúario */

		return "usuario Excluido!";
	}
	
	// consultar Usuario por Nome
	@GetMapping(value = "/usuarioPorNome/{nome}", produces = "application/json")
	@CachePut("cacheusuarios")
	public ResponseEntity<List<Usuario>> usuarioPorNome(@PathVariable("nome") String nome) throws InterruptedException {

		List<Usuario> list = (List<Usuario>) userRepo.findUserByNome(nome);
		
	//	Thread.sleep(6000); // segura 6 segundos

		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);

	}

	/*
	 * @DeleteMapping(value = "/{id}/venda", produces = "application/text") public
	 * String deleteVenda(@PathVariable("id") Long id) {
	 * 
	 * userRepo.deleteById(id);
	 * 
	 * return "usuario Excluido!"; }
	 */

	/*
	 * @SuppressWarnings({ "unchecked", "rawtypes" })
	 * 
	 * @PutMapping(value = "/{iduser}/idvenda/{idvenda}", produces
	 * ="application/json") public ResponseEntity<?> updatevendas(@PathVariable Long
	 * idsuer,
	 * 
	 * @PathVariable Long idvenda) {
	 * 
	 * da pra fazer outras coisas antes de atualizar
	 * 
	 * //Usuario userSalvo = userRepo.save(user);
	 * 
	 * return new ResponseEntity("Vendas atualizadas", HttpStatus.OK); }
	 */

	/*
	 * @SuppressWarnings({ "unchecked", "rawtypes" })
	 * 
	 * @PostMapping(value = "/{iduser}/idvenda/{idvenda}", produces
	 * ="application/json") public ResponseEntity<?> salvarVendas(@PathVariable Long
	 * idsuer,
	 * 
	 * @PathVariable Long idvenda) {
	 * 
	 * //Usuario userSalvo = userRepo.save(user);
	 * 
	 * return new ResponseEntity("id user :" + idsuer + "idvenda :" +
	 * idvenda,HttpStatus.OK); }
	 */

}
