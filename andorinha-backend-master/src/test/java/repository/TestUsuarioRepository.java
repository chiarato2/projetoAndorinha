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
		int idRemover = 8;
		this.usuarioRepository.remover(idRemover);
		
		assertThat( idRemover ).isNull();
		
	}
	
	@Test
	public void testa_atualizar_usuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		int id = 8;
		String nome = "william";
		//usei uma maneira diferente para dar update no banco, necessário descomentar as linhas no "Usuario"
		Usuario user = new Usuario(id, nome);  
		
		this.usuarioRepository.atualizar(user);
		
		assertThat( user ).isNotNull();
		assertThat( user.getNome() ).isEqualTo("william");
		assertThat( user.getId() ).isEqualTo(id);
	}
	
	@Test
	public void testa_listar_todos_usuarios() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		this.usuarioRepository.listarTodos();
	}
	//william


}

/* Hallan
@RunWith(AndorinhaTestRunner.class)
public class TestUsuarioRepository {

	private static final int ID_USUARIO_CONSULTA = 1;
	private static final int ID_USUARIO_SEM_TWEET = 5;

	private UsuarioRepository usuarioRepository;

	@Before
	public void setUp() {
		this.usuarioRepository = new UsuarioRepository();
	}

	@Test
	public void testa_se_usuario_foi_inserido() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Usuario user = new Usuario();
		user.setNome("Usuario do Teste de Unidade");
		this.usuarioRepository.inserir(user);

		Usuario inserido = this.usuarioRepository.consultar(user.getId());

		assertThat( user.getId() ).isGreaterThan(0);

		assertThat( inserido ).isNotNull();
		assertThat( inserido.getNome() ).isEqualTo(user.getNome());
		assertThat( inserido.getId() ).isEqualTo(user.getId());
	}

	@Test
	public void testa_consultar_usuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Usuario user = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);

		assertThat( user ).isNotNull();
		assertThat( user.getNome() ).isEqualTo("Usuário 1");
		assertThat( user.getId() ).isEqualTo(ID_USUARIO_CONSULTA);
	}

	@Test
	public void testa_alterar_usuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Usuario user = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);
		user.setNome("Alterado!");

		this.usuarioRepository.atualizar(user);

		Usuario alterado = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);

		assertThat( alterado ).isEqualToComparingFieldByField(user);
	}

	@Test
	public void testa_remover_usuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Usuario user = this.usuarioRepository.consultar(ID_USUARIO_SEM_TWEET);
		assertThat( user ).isNotNull();

		this.usuarioRepository.remover(ID_USUARIO_SEM_TWEET);

		Usuario removido = this.usuarioRepository.consultar(ID_USUARIO_SEM_TWEET);
		assertThat( removido ).isNull();
	}

	@Test
	public void testa_remover_usuario_com_tweet() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		assertThatThrownBy(() -> { this.usuarioRepository.remover(ID_USUARIO_CONSULTA); })
			.isInstanceOf(ErroAoConsultarBaseException.class)
        	.hasMessageContaining("Ocorreu um erro ao remover o usuário");
	}

	@Test
	public void testa_listar_todos_os_usuarios() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		List<Usuario> usuarios = this.usuarioRepository.listarTodos();

		assertThat( usuarios ).isNotNull()
							.isNotEmpty()
							.hasSize(5)
							.extracting("nome")
							.containsExactlyInAnyOrder("Usuário 1", "Usuário 2",
			                        "Usuário 3", "Usuário 4", "Usuário 5");
	}


}
*/
