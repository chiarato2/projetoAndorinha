package repository;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import model.Comentario;
import model.seletor.ComentarioSeletor;

@Stateless
public class ComentarioRepository extends AbstractCrudRepository {

	public void inserir(Comentario comentario) {
		comentario.setData(Calendar.getInstance());
		super.em.persist(comentario);
	}

	public void atualizar(Comentario comentario) {
		comentario.setData(Calendar.getInstance());
		super.em.merge(comentario);
	}

	public void remover(int id) {
		
		Comentario comentario = this.consultar(id);
		super.em.remove(comentario);
	}

	public Comentario consultar(int id) {
		
		return super.em.find(Comentario.class, id);
	}

	public List<Comentario> listarTodos(){
		
		return this.pesquisar(new ComentarioSeletor());		
	}
	
	public List<Comentario> pesquisar(ComentarioSeletor seletor) {
		
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT c FROM Comentario c JOIN c.usuario JOIN c.tweet t JOIN t.usuario ");
		
		this.criarFiltro(jpql, seletor);
		
		Query query = super.em.createQuery(jpql.toString());
		
		this.adicionarParametros(query, seletor);
		
		return query.getResultList();
	}
	
	
	public Long contar(ComentarioSeletor seletor) {
		
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT COUNT(c) FROM Comentario c ");
		
		this.criarFiltro(jpql, seletor);
		
		Query query = super.em.createQuery(jpql.toString());
		
		this.adicionarParametros(query, seletor);
		
		return (Long)query.getSingleResult();
	}
	
	private void criarFiltro(StringBuilder jpql, ComentarioSeletor seletor) {
		
		if(seletor.possuiFiltro()) {
			jpql.append("WHERE ");
			boolean primeiro = true;
			if(seletor.getId() != null) {
				
				jpql.append("c.id = :id ");
				primeiro = false;
			}
			
			if (seletor.getIdUsuario() != null) {
				if (!primeiro) {
					
					jpql.append("AND ");
				}else {
					
					primeiro = false;
				}
				
				jpql.append("c.usuario.id = :id_usuario ");
			}
			
			if (seletor.getIdTweet() != null) {
				if (!primeiro) {
					
					jpql.append("AND ");
				}else {
					
					primeiro = false;
				}
				
				jpql.append("c.tweet.id = :id_tweet ");
			}
			
			if(seletor.getConteudo() != null && !seletor.getConteudo().trim().isEmpty()) {
				if(!primeiro) {
					jpql.append("AND ");
	
				}else {
					
					primeiro = false;
				}
				
				jpql.append("c.conteudo LIKE :conteudo ");
			}
			
			if (seletor.getData() != null) {
				if (!primeiro) {
					
					jpql.append("AND ");
				}else {
					
					primeiro = false;
				}
				
				jpql.append("date(t.data) = :data_postagem ");
			}
		}
	}
	
	private void adicionarParametros(Query query, ComentarioSeletor seletor) {
		int indice = 1;

		if (seletor.possuiFiltro()) {
			if ( seletor.getId() != null ) {
				query.setParameter(indice++, seletor.getId());
			}

			if (seletor.getConteudo() != null && !seletor.getConteudo().trim().isEmpty() ) {
				query.setParameter("conteudo", String.format("%%%s%%", seletor.getConteudo()) );
			}
			
			if (seletor.getData() != null) {
				query.setParameter("data_postagem", seletor.getData().getTimeInMillis());
			}
			
			if (seletor.getIdUsuario() != null) {
				query.setParameter("id_usuario", seletor.getIdUsuario());
			}
			
			if (seletor.getIdTweet() != null) {
				query.setParameter("id_tweet", seletor.getIdTweet());
			}
		}
	}
	
}