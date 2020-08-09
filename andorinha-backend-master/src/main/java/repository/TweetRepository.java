package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;

import model.Tweet;
import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;
import model.seletor.TweetSeletor;
import model.seletor.UsuarioSeletor;

@Stateless
public class TweetRepository extends AbstractCrudRepository {

	public void inserir(Tweet tweet) throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {
		try (Connection c = this.abrirConexao()) {

			int id = this.recuperarProximoValorDaSequence("seq_tweet");
			tweet.setId(id);

			Calendar hoje = Calendar.getInstance();

			PreparedStatement ps = c.prepareStatement("insert into tweet (id, conteudo, data_postagem, id_usuario) values (?,?,?,?)");
			ps.setInt(1, tweet.getId());
			ps.setString(2, tweet.getConteudo());
			ps.setTimestamp(3, new Timestamp(hoje.getTimeInMillis()));
			ps.setInt(4, tweet.getUsuario().getId());
			ps.execute();
			ps.close();

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao inserir o tweet", e);
		}
	}

	public void atualizar(Tweet tweet) throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {
		try (Connection c = this.abrirConexao()) {

			Calendar hoje = Calendar.getInstance();

			PreparedStatement ps = c.prepareStatement("update tweet set conteudo = ?, data_postagem = ? where id = ?");
			ps.setString(1, tweet.getConteudo());
			ps.setTimestamp(2, new Timestamp(hoje.getTimeInMillis()));
			ps.setInt(3, tweet.getId());
			ps.execute();
			ps.close();

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao atualizar o tweet", e);
		}
	}

	public void remover(int id) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = this.abrirConexao()) {

			PreparedStatement ps = c.prepareStatement("delete from tweet where id = ?");
			ps.setInt(1, id);
			ps.execute();
			ps.close();

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao remover o tweet", e);
		}

	}

	public Tweet consultar(int id) throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {
		try (Connection c = this.abrirConexao()) {

			Tweet tweet = null;

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT t.id, t.conteudo, t.data_postagem, t.id_usuario, u.nome as nome_usuario FROM tweet t ");
			sql.append("JOIN usuario u on t.id_usuario = u.id ");
			sql.append("WHERE t.id = ? ");

			PreparedStatement ps = c.prepareStatement(sql.toString());
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				tweet = new Tweet();
				tweet.setId(rs.getInt("id"));
				tweet.setConteudo(rs.getString("conteudo"));
				
				Calendar data = new GregorianCalendar();
				data.setTime( rs.getTimestamp("data_postagem") );
				tweet.setData(data);

				Usuario user = new Usuario();
				user.setId(rs.getInt("id_usuario"));
				user.setNome(rs.getString("nome_usuario"));
				tweet.setUsuario(user);
			}
			rs.close();
			ps.close();

			return tweet;

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao consultar o tweet", e);
		}
	}

	//filtros
	public List<Tweet> listarTodos() throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {
		
		return this.pesquisar( new TweetSeletor() );
		
		/*try (Connection c = this.abrirConexao()) {

			List<Tweet> tweets = new ArrayList<>();

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT t.id, t.conteudo, t.data_postagem, t.id_usuario, u.nome as nome_usuario FROM tweet t ");
			sql.append("JOIN usuario u on t.id_usuario = u.id ");

			PreparedStatement ps = c.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Tweet tweet = new Tweet();
				tweet = new Tweet();
				tweet.setId(rs.getInt("id"));
				tweet.setConteudo(rs.getString("conteudo"));

				Calendar data = new GregorianCalendar();
				data.setTime( rs.getTimestamp("data_postagem") );
				tweet.setData(data);

				Usuario user = new Usuario();
				user.setId(rs.getInt("id_usuario"));
				user.setNome(rs.getString("nome_usuario"));
				tweet.setUsuario(user);

				tweets.add(tweet);
			}
			rs.close();
			ps.close();

			return tweets;

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao consultar todos os tweets", e);
		}*/
	}
	
	public List<Tweet> pesquisar(TweetSeletor seletor) throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException{
		try (Connection c = this.abrirConexao()) {

			List<Tweet> tweets = new ArrayList<>();

			StringBuilder sql = new StringBuilder();
			//sql.append("select id, id_usuario, conteudo, data_postagem nome from tweet ");
			sql.append("SELECT t.id, t.conteudo, t.data_postagem, t.id_usuario, u.nome as nome_usuario FROM tweet t ");
			sql.append("JOIN usuario u on t.id_usuario = u.id ");

			this.criarFiltro(sql, seletor);

			PreparedStatement ps = c.prepareStatement( sql.toString() );

			this.adicionarParametros(ps, seletor);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				tweets.add( this.criarModel(rs) );
			}
			rs.close();
			ps.close();

			return tweets;

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao pesquisar os tweets", e);
		}
	}
	
	public Long contar(TweetSeletor seletor) throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException{
		
		try (Connection c = this.abrirConexao()) {

			Long id = 0L;

			StringBuilder sql = new StringBuilder();
			sql.append("select count(id) as total from tweet ");

			this.criarFiltro(sql, seletor);

			PreparedStatement ps = c.prepareStatement( sql.toString() );

			this.adicionarParametros(ps, seletor);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				id = rs.getLong("total");
			}
			rs.close();
			ps.close();

			return id;

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao contar todos os tweets", e);
		}
	}
	
	private void criarFiltro(StringBuilder sql, TweetSeletor seletor) {
		
		if (seletor.possuiFiltro()) {
			sql.append("WHERE ");
			boolean primeiro = true;
			if ( seletor.getIdUsuario() != null ) {
				sql.append("id_usuario = ? ");
			}
			
			if(seletor.getConteudo() != null && !seletor.getConteudo().trim().isEmpty()) {
				if(!primeiro) {
					sql.append("AND ");
	
				}
				sql.append("conteudo like ?");
	
			}
		}
			
	}
	
	private void adicionarParametros(PreparedStatement ps, TweetSeletor seletor) throws SQLException{
		int indice = 1;

		if (seletor.possuiFiltro()) {
			if ( seletor.getId() != null ) {
				ps.setInt(indice++, seletor.getId());
			}

			if (seletor.getConteudo() != null && !seletor.getConteudo().trim().isEmpty() ) {
				ps.setString(indice++, String.format("%%%s%%", seletor.getConteudo()) );
			}
		}
	}
	
	private Tweet criarModel(ResultSet rs) throws SQLException{
		Tweet tweet = new Tweet();
		Usuario user = new Usuario();
		Calendar data = new GregorianCalendar();
		
		tweet.setId(rs.getInt("id"));
		tweet.setConteudo(rs.getString("conteudo"));
		
		data.setTime( rs.getTimestamp("data_postagem") );
		tweet.setData(data);
		
		user.setId(rs.getInt("id_usuario"));
		user.setNome(rs.getString("nome_usuario"));
		tweet.setUsuario(user);
		
		return tweet;
	}
	//filtros
}
