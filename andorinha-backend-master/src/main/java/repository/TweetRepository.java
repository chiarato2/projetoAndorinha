package repository;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import model.Tweet;
import model.dto.TweetDTO;
import model.seletor.TweetSeletor;
import repository.base.AbstractCrudRepository;


@Stateless
public class TweetRepository extends AbstractCrudRepository<Tweet> {
	
	public List<Tweet> pesquisar(TweetSeletor seletor){
		return super.createEntityQuery()
				.innerJoinFetch("usuario")
				.equal("id", seletor.getId())
				.equal("usuario.id", seletor.getIdUsuario())
				.like("conteudo", seletor.getConteudo())
				.equal("data", seletor.getData())
				.setFirstResult(seletor.getOffset())
				.setMaxResults(seletor.getLimite())
				.list();
	}
	
	public Long contar(TweetSeletor seletor){
		return super.createCountQuery()
				.innerJoinFetch("usuario")
				.equal("id", seletor.getId())
				.equal("usuario.id", seletor.getIdUsuario())
				.like("conteudo", seletor.getConteudo())
				.equal("data", seletor.getData())
				.setFirstResult(seletor.getOffset())
				.setMaxResults(seletor.getLimite())
				.count();
	}
	
	public List<TweetDTO> pesquisarDTO(TweetSeletor seletor){
		return super.createTupleQuery()
				.select("id", "usuario.id as idUsuario", "usuario.nome as nomeUsuario", "data", "conteudo")
				.join("usuario")
				.equal("id", seletor.getId())
				.equal("usuario.id", seletor.getIdUsuario())
				.like("conteudo", seletor.getConteudo())
				.equal("data", seletor.getData())
				.setFirstResult(seletor.getOffset())
				.setMaxResults(seletor.getLimite())
				.list(TweetDTO.class);
	}
}
