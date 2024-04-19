package com.springboot.sgah.backend.apirest.rm;

import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_ERROR;
import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_MENSAJE;
import static com.springboot.sgah.backend.apirest.rm.Constants.MESSAGE_ERROR_BD;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

public class ErrorMessageUtil {

	private ErrorMessageUtil() {
	}

	public static Map<String, Object> getErrorMessage(Exception e) {
		Map<String, Object> response = new HashMap<>();

		response.put(TEXT_MENSAJE, MESSAGE_ERROR_BD);
		response.put(TEXT_ERROR, e.getMessage().concat(": ").concat(e.getCause().getMessage()));

		return response;
	}

	public static Map<String, Object> getFieldsErrorMessage(BindingResult result) {
		Map<String, Object> response = new HashMap<>();

		List<String> errors = result.getFieldErrors().stream()
				.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
				.collect(Collectors.toList());

		response.put("errors", errors);

		return response;
	}

}
