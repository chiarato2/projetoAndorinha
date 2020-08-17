package repository;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import model.Comentario;
import model.Tweet;
import model.dto.ComentarioDTO;
import model.dto.TweetDTO;
import model.seletor.ComentarioSeletor;
import model.seletor.TweetSeletor;
import repository.base.AbstractCrudRepository;

@Stateless
public class ComentarioRepository extends AbstractCrudRepository<Comentario> {
	
	@Override
	public List<Comentario> listarTodos() {
		return pesquisar(new ComentarioSeletor());
	}
	
	public List<Comentario> pesquisar(ComentarioSeletor seletor){
		return super.createEntityQuery()
				.equal("id", seletor.getId())
				.equal("tweet.id", seletor.getIdTweet())
				.equal("usuario.id", seletor.getIdUsuario())
				.like("conteudo", seletor.getConteudo())
				.equal("data", seletor.getData())
				.setFirstResult(seletor.getOffset())
				.setMaxResults(seletor.getLimite())
				.list();
	}
	
	public Long contar(ComentarioSeletor seletor){
		return super.createCountQuery()
				.equal("id", seletor.getId())
				.equal("tweet.id", seletor.getIdTweet())
				.equal("usuario.id", seletor.getIdUsuario())
				.like("conteudo", seletor.getConteudo())
				.equal("data", seletor.getData())
				.setFirstResult(seletor.getOffset())
				.setMaxResults(seletor.getLimite())
				.count();
	}
	
	public List<ComentarioDTO> pesquisarDTO(ComentarioSeletor seletor){
		return super.createTupleQuery()
				.select("id", "tweet.id as idTweet", "usuario.id as idUsuario", "usuario.nome as nomeUsuario", "data", "conteudo")
				.join("usuario")
				.join("tweet")
				.equal("id", seletor.getId())
				.equal("tweet.id", seletor.getIdTweet())
				.equal("usuario.id", seletor.getIdUsuario())
				.like("conteudo", seletor.getConteudo())
				.equal("data", seletor.getData())
				.setFirstResult(seletor.getOffset())
				.setMaxResults(seletor.getLimite())
				.list(ComentarioDTO.class);
				
	}
	
	
}