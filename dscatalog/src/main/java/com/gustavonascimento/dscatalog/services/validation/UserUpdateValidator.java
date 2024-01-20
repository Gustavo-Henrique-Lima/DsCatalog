package com.gustavonascimento.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.gustavonascimento.dscatalog.controllers.exceptions.FieldMessage;
import com.gustavonascimento.dscatalog.entities.User;
import com.gustavonascimento.dscatalog.entities.dto.UserUpdateDTO;
import com.gustavonascimento.dscatalog.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserRepository repository;

	@Override
	public void initialize(UserUpdateValid ann) {
	}

	@Override
	public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

		List<FieldMessage> list = new ArrayList<>();

		@SuppressWarnings("unchecked")
		var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Long userId = Long.parseLong(uriVars.get("id"));

		User user = repository.findByEmail(dto.getEmail());
		// Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à
		// lista
		if (user != null && userId != user.getId()) {
			list.add(new FieldMessage("email", "Esse email já está cadastrado"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}