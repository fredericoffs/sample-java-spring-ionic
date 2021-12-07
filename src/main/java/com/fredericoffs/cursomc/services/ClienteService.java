package com.fredericoffs.cursomc.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fredericoffs.cursomc.domain.Cidade;
import com.fredericoffs.cursomc.domain.Cliente;
import com.fredericoffs.cursomc.domain.Endereco;
import com.fredericoffs.cursomc.domain.enums.TipoCliente;
import com.fredericoffs.cursomc.dto.ClienteDTO;
import com.fredericoffs.cursomc.dto.ClienteNewDTO;
import com.fredericoffs.cursomc.repositories.ClienteRepository;
import com.fredericoffs.cursomc.repositories.EnderecoRepository;
import com.fredericoffs.cursomc.services.exception.DataIntegrityException;
import com.fredericoffs.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	public Cliente findById(Integer id) {
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	public List<Cliente> findAll() {
		return repository.findAll();
	}

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repository.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = findById(obj.getId());
		updateData(newObj, obj);
		return repository.save(newObj);
	}

	public void delete(Integer id) {
		findById(id);
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir pois há entidades relacionadas");
		}
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}

	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getName(), objDto.getEmail(), null, null);
	}

	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getName(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(),
				cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		if (objDto.getTelefone2() != null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		if (objDto.getTelefone3() != null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		return cli;
	}

	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setName(obj.getName());
		newObj.setEmail(obj.getEmail());
	}

}
