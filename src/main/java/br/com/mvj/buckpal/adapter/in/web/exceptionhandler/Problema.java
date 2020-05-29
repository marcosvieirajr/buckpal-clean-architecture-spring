package br.com.mvj.buckpal.adapter.in.web.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class Problema {
	private Integer status;
	private String message;
	private OffsetDateTime dateHora;
	private List<Campo> campos;

	@Getter
	@Setter
	public static class Campo {
		private String name;
		private String message;
		public Campo(String name, String message) {
			super();
			this.name = name;
			this.message = message;
		}
	}

}
