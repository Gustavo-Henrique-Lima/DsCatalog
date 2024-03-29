package com.gustavonascimento.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavonascimento.dscatalog.entities.Role;
import com.gustavonascimento.dscatalog.entities.User;
import com.gustavonascimento.dscatalog.entities.dto.RoleDTO;
import com.gustavonascimento.dscatalog.entities.dto.UserDTO;
import com.gustavonascimento.dscatalog.entities.dto.UserInsertDTO;
import com.gustavonascimento.dscatalog.entities.dto.UserUpdateDTO;
import com.gustavonascimento.dscatalog.repositories.RoleRepository;
import com.gustavonascimento.dscatalog.repositories.UserRepository;
import com.gustavonascimento.dscatalog.services.exceptions.DataBaseException;
import com.gustavonascimento.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;
	
	private BCryptPasswordEncoder password = new BCryptPasswordEncoder();
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> objs = repository.findAll(pageable);
		return objs.map(x -> new UserDTO(x));
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		copyDtoToEntity(dto, entity);
		entity.setPassword(password.encode(dto.getPassword()));
		entity = repository.save(entity);
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			User entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new UserDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id não encontrado: " + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id não encontrado: " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Violação de integridade");
		}
	}

	private void copyDtoToEntity(UserDTO dto, User entity) {
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
		for (RoleDTO roleDto : dto.getRoles()) {
			Role role = roleRepository.getReferenceById(roleDto.getId());
			entity.getRoles().add(role);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User entity = repository.findByEmail(username);
		if(entity==null) {
			throw new UsernameNotFoundException("Email não encontrado");
		}
		return entity;
	}
}