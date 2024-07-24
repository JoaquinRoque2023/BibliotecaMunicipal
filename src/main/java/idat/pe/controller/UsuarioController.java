package idat.pe.controller;

import java.time.LocalDate;
import java.util.List;

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
import org.springframework.web.server.ResponseStatusException;

import idat.pe.Respuesta;
import idat.pe.model.Usuario;
import idat.pe.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioService serv;
	
	@GetMapping
	public ResponseEntity<Object> buscarTodo(){
		List<Usuario> listadousuarios = serv.buscarTodo();
		return new ResponseEntity<Object>(listadousuarios, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Object> buscarPorID(@PathVariable("id") int idusuario) {
		Usuario usuario = serv.buscarPorID(idusuario);
		if (usuario == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "¡Usuario no encontrado!");
		}

		return new ResponseEntity<Object>(usuario, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Object> crear(@RequestBody Usuario usuarioNuevo) {
		usuarioNuevo.setEstado("ACTIVO");
        usuarioNuevo.setFechaRegistro(LocalDate.now());
	
		Usuario usuarioCreado = serv.crear(usuarioNuevo);
		Respuesta respuesta = new Respuesta("OK",
				"Usuario creado correctamente : " + usuarioCreado.getIdUsuario());
		return new ResponseEntity<Object>(respuesta, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Object> actualizar(@PathVariable("id") int idActualizar,
			@RequestBody Usuario usuarioActualizar) {
		Usuario usuario = serv.buscarPorID(idActualizar);
		if (usuario == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "¡Usuario no encontrado!");
		}
		
		usuarioActualizar.setIdUsuario(idActualizar);
		usuarioActualizar.setEstado(usuario.getEstado());
        usuarioActualizar.setFechaRegistro(usuario.getFechaRegistro());
        
		serv.actualizar(usuarioActualizar);
		Respuesta respuesta = new Respuesta("OK","Usuario actualizado correctamente");
		return new ResponseEntity<Object>(respuesta, HttpStatus.ACCEPTED);
	}
    
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") int idEliminar)
	{
		Usuario usuario = serv.buscarPorID(idEliminar);
		if (usuario == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "¡Usuario no encontrado!");
		}
		serv.borrarPorID(idEliminar);
		Respuesta respuesta = new Respuesta("OK","Usuario eliminado correctamente");
		return new ResponseEntity<Object>(respuesta, HttpStatus.ACCEPTED);
	}
}
