package br.com.alura.forum.controller.dto;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import br.com.alura.forum.modelo.Topico;
import lombok.Getter;

@Getter
public class TopicoDTO {

	private Long id;
	
	private String titulo;
	
	private String mensagem;
	
	private LocalDateTime dataCriacao;

	public TopicoDTO(Topico topico) {
		this.id = topico.getId();
		this.titulo = topico.getTitulo();
		this.mensagem = topico.getMensagem();
		this.dataCriacao = topico.getDataCriacao();
	}

	// Converção de Topico para TopicoDTO
	public static Page<TopicoDTO> converter(Page<Topico> topicos) {
		return topicos.map(TopicoDTO::new);
		//		return topicos.stream().map(TopicoDTO::new).collect(Collectors.toList());
	}
}