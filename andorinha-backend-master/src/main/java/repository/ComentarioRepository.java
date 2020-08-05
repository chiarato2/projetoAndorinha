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

import model.Comentario;
import model.Tweet;
import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;

//william
public class ComentarioRepository extends AbstractCrudRepository{
	
	public void inserir(Comentario comentario) throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException{
		try (Connection c = this.abrirConexao()) {
			
			int id = this.recuperarProximoValorDaSequence("seq_comentario");
			comentario.setId(id);
			
			Calendar data = Calendar.getInstance();
			
			PreparedStatement ps = c.prepareStatement("insert into comentario(id, id_usuario, id_tweet, conteudo, data_postagem) values ( ?, ?, ?, ?, ?);");
			ps.setInt(1, comentario.getId());
			ps.setInt(2, comentario.getUsuario().getId());
			ps.setInt(3, comentario.getTweet().getId());
			ps.setString(4, comentario.getConteudo());
			ps.setTimestamp(5, new Timestamp(data.getTimeInMillis()));
			ps.execute();
			ps.close();
			
		}catch(SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao inserir o comentario", e);
		}
	}
	
	public void atualizar(Comentario comentario) throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException{
		try (Connection c = this.abrirConexao()) {
				
			Calendar data = Calendar.getInstance();

			PreparedStatement ps = c.prepareStatement("update comentario set conteudo = ?, data_postagem = ? where id = ?");
			ps.setString(1, comentario.getConteudo());
			ps.setTimestamp(2, new Timestamp(data.getTimeInMillis()));
			ps.setInt(3, comentario.getId());
			ps.execute();
			ps.close();
			
		}catch(SQLException e) {
				throw new ErroAoConsultarBaseException("Ocorreu um erro ao atualizar o comentario", e);
		}
	}
	
	public void remover(int id) throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException{
		try (Connection c = this.abrirConexao()) {

			PreparedStatement ps = c.prepareStatement("delete from comentario where id = ?");
			ps.setInt(1, id);
			ps.execute();
			ps.close();

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao remover o comentario", e);
		}
	}
	
	public Comentario consultar(int id) throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException{
		try (Connection c = this.abrirConexao()) {

			Comentario comentario = null;

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT c.id, c.id_usuario, c.id_tweet, c.conteudo, c.data_postagem, u.nome, t.conteudo FROM comentario c ");
			sql.append("JOIN usuario u on c.id_usuario = u.id ");
			sql.append("JOIN tweet t on c.id_tweet = t.id ");
			sql.append("WHERE c.id = ? ");

			PreparedStatement ps = c.prepareStatement(sql.toString());
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				comentario = new Comentario();
				comentario.setId(rs.getInt("id"));
				comentario.setConteudo(rs.getString("conteudo"));

				Tweet tweet = new Tweet();
				tweet.setId(rs.getInt("id_tweet"));
				tweet.setConteudo(rs.getString("conteudo"));
				comentario.setTweet(tweet);
				
				Calendar data = new GregorianCalendar();
				data.setTime( rs.getTimestamp("data_postagem") );
				comentario.setData(data);

				Usuario user = new Usuario();
				user.setId(rs.getInt("id_usuario"));
				user.setNome(rs.getString("nome"));
				comentario.setUsuario(user);
				
			}
			rs.close();
			ps.close();

			return comentario;

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao consultar o comentario", e);
		}
	}
	
	public List<Comentario> listarTodos() throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {
		try (Connection c = this.abrirConexao()) {

			List<Comentario> comentarios = new ArrayList<>();

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT c.id, c.id_usuario, c.id_tweet, c.conteudo, c.data_postagem, u.nome, t.conteudo FROM comentario c ");
			sql.append("JOIN usuario u on c.id_usuario = u.id ");
			sql.append("JOIN tweet t on c.id_tweet = t.id ");

			PreparedStatement ps = c.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Comentario comentario = new Comentario();
				comentario = new Comentario();
				comentario.setId(rs.getInt("id"));
				comentario.setConteudo(rs.getString("conteudo"));
				
				Tweet tweet = new Tweet();
				tweet.setId(rs.getInt("id_tweet"));
				tweet.setConteudo(rs.getString("conteudo"));

				Calendar data = new GregorianCalendar();
				data.setTime( rs.getTimestamp("data_postagem") );
				comentario.setData(data);

				Usuario user = new Usuario();
				user.setId(rs.getInt("id_usuario"));
				user.setNome(rs.getString("nome"));
				comentario.setUsuario(user);

				comentarios.add(comentario);
			}
			rs.close();
			ps.close();

			return comentarios;

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao consultar todos os comentários", e);
		}
	}
}
//william