package repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;
import runner.AndorinhaTestRunner;

@RunWith(AndorinhaTestRunner.class)
public class TestUsuarioRepository {

	private UsuarioRepository usuarioRepository;

	@Before
	public void setUp() {
		this.usuarioRepository = new UsuarioRepository();
	}

	@Test
	public void testa_se_usuario_foi_inserido() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Usuario user = new Usuario();
		user.setNome("william");
		this.usuarioRepository.inserir(user);

		Usuario inserido = this.usuarioRepository.consultar(user.getId());

		assertThat( user.getId() ).isGreaterThan(0);

		assertThat( inserido ).isNotNull();
		assertThat( inserido.getNome() ).isEqualTo(user.getNome());
		assertThat( inserido.getId() ).isEqualTo(user.getId());
	}

	@Test
	public void testa_consultar_usuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		int idConsulta = 4;
		Usuario user = this.usuarioRepository.consultar(idConsulta);

		assertThat( user ).isNotNull();
		assertThat( user.getNome() ).isEqualTo("william");
		assertThat( user.getId() ).isEqualTo(idConsulta);
	}
	
	//william
	@Test
	public void testa_remover_usuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {		
		int idRemover = 7;
		this.usuarioRepository.remover(idRemover);
		
	}
	
	@Test
	public void testa_atualizar_usuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		int id = 8;
		String nome = "william";
		//usei uma maneira diferente para dar update no banco, necessário descomentar as linhas no "Usuario"
		Usuario user = new Usuario(id, nome);  
		
		this.usuarioRepository.atualizar(user);
		
	}
	
	@Test
	public void testa_listar_todos_usuarios() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		this.usuarioRepository.listarTodos();
	}
	//william


}
