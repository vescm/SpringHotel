package edu.les.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.les.entity.UserEntity;
import edu.les.exception.ExceptionHandler;
import edu.les.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	public UserRepository userRepository;

	@Autowired
	public AddressService addressService;

	@Autowired
	public CredentialService credentialService;

	public boolean add(UserEntity userEntity) throws ExceptionHandler {
		boolean result = false;
		try {
			this.userRepository.save(userEntity);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExceptionHandler();
		}
		return result;
	}

	public boolean hasErrors(UserEntity u) throws ExceptionHandler {
		boolean result = true;
		List<String> errorFields = new ArrayList<String>();

		if (u == null) {
			return result;
		}

		if (u.getUserCpf().length() == 0 || u.getUserCpf().length() != 11) {
			errorFields.add("CPF");
		}

		if (u.getUsername().length() == 0 || u.getUsername().length() > 35) {
			errorFields.add("Name");
		}

		// TODO: validate user role entity only for admin user
		if (u.getUserRole() == null) {
			errorFields.add("User Role");
		}

		if (u.getHouseNumber() == 0) {
			errorFields.add("House Number");
		}

		if (u.getTelephoneNumber().length() > 15) {
			errorFields.add("Telephone");
		}

		if (u.getCelphoneNumber().length() > 15) {
			errorFields.add("Celphone");
		}

		if (u.getDateOfBirth() == null) {
			errorFields.add("Birthday");
		}

		// TODO: check if is needed to validate user status
		// because it is hardcoded at the register

		try {
			// catch exception gotten inside those classes
			// no need to receive boolean results
			this.addressService.hasErrors(u.getAddressEntity());
			this.credentialService.hasErrors(u.getCredentialEntity());
		} catch (ExceptionHandler h) {
			errorFields.addAll(h.getErrors());
		}
		
		if (errorFields.isEmpty()) {
			result = false;
		} else {
			throw new ExceptionHandler(errorFields);
		}
		
		return result;
	}
}