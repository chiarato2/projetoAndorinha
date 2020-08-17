package repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;
import model.seletor.UsuarioSeletor;
import repository.base.AbstractCrudRepository;

@Stateless
public class UsuarioRepository extends AbstractCrudRepository<Usuario> {
	
	public List<Usuario> pesquisar(UsuarioSeletor seletor){
		return super.createEntityQuery()
				.equal("id", seletor.getId())
				.like("nome", seletor.getNome())
				.setFirstResult(seletor.getOffset())
				.setMaxResults(seletor.getLimite())
				.list();
	}
	
	public Long contar(UsuarioSeletor seletor){
		return super.createCountQuery()
				.equal("id", seletor.getId())
				.like("nome", seletor.getNome())
				.setFirstResult(seletor.getOffset())
				.setMaxResults(seletor.getLimite())
				.count();
	}
}