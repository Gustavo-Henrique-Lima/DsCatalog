package com.gustavonascimento.dscatalog.entities.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.gustavonascimento.dscatalog.entities.User;

<<<<<<< HEAD
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

=======
>>>>>>> dd0078b39675b32bf70c9cd88311c43f6f244eb0
public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
<<<<<<< HEAD
	@Size(min=5,max=60, message = "O campo nome deve conter entre 5 e 60 caracteres")
	@NotBlank(message="O campo nome é obrigatório")
	private String firstName;
	private String lastName;
	@Email(message = "Informe um email válido")
=======
	private String firstName;
	private String lastName;
>>>>>>> dd0078b39675b32bf70c9cd88311c43f6f244eb0
	private String email;
	Set<RoleDTO> roles = new HashSet<>();

	public UserDTO() {
	}

	public UserDTO(Long id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public UserDTO(User entity) {
		this.id = entity.getId();
		this.firstName = entity.getFirstName();
		this.lastName = entity.getLastName();
		this.email = entity.getEmail();
		entity.getRoles().forEach(role->this.roles.add(new RoleDTO(role)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}
}