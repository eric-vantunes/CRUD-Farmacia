package com.eric.farmacia.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.eric.farmacia.model.Categoria;
import com.eric.farmacia.repository.CategoriaRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoriaControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@BeforeAll
	void start(){
		
		categoriaRepository.save(new Categoria(0L, "Categoria de Testes 1", "Categoria criada para testes"));
		categoriaRepository.save(new Categoria(0L, "Categoria de Testes 2", "Categoria criada para testes"));
		categoriaRepository.save(new Categoria(0L, "Categoria de Testes 3", "Categoria criada para testes"));
		categoriaRepository.save(new Categoria(0L, "Categoria novo Testes ", "Categoria"));
		categoriaRepository.save(new Categoria(0L, "Categoria novo Testes ", "Categoria"));
		categoriaRepository.save(new Categoria(0L, "Categoria novo Testes ", "Categoria"));
		
		categoriaRepository.deleteAll();

	}

	@Test
	@DisplayName("Cadastrar Categoria")
	public void deveCriarUmaCategoria() {

		HttpEntity<Categoria> corpoRequisicao = new HttpEntity<Categoria>(new Categoria(0L, "Categoria de Testes", "Isso é uma categoria criada para testes"));
		
		ResponseEntity<Categoria> corpoResposta = testRestTemplate.exchange("/categorias", HttpMethod.POST, corpoRequisicao, Categoria.class);
		
		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
	}

	@Test
	@DisplayName("Atualizar uma Categoria")
	public void deveAtualizarCategoria() {
			Categoria categoriaExistente = categoriaRepository.save(new Categoria(0L, "Categoria de Testes", "Categoria criada para testes"));
			
			Categoria categoriaNova = new Categoria(categoriaExistente.getId(), "Categoria nova de Testes", "Categoria criada para testes");
			
			HttpEntity<Categoria> corpoRequisicao = new HttpEntity<Categoria>(categoriaNova);
			
			ResponseEntity<Categoria> corpoResposta = testRestTemplate
					.exchange("/categorias", HttpMethod.PUT, corpoRequisicao, Categoria.class);
			
			assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
	}

	@Test
	@DisplayName("Listar todos as categorias")
	public void deveListarTodasAsCategorias() {
		categoriaRepository.save(new Categoria(0L, "Categoria de Testes T1", "Categoria criada para testes"));
		categoriaRepository.save(new Categoria(0L, "Categoria de Testes T2", "Categoria criada para testes"));
		
		ResponseEntity<String> resposta = testRestTemplate
				.exchange("/categorias", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Buscar categorias por nome")
	public void deveBuscarCategoriasPorNome() {
		categoriaRepository.save(new Categoria(0L, "Categoria de Testes T3", "Categoria criada para testes"));
		categoriaRepository.save(new Categoria(0L, "Categoria Qualquer T4", "Categoria qualquer"));
		
		ResponseEntity<String> resposta = testRestTemplate
				.exchange("/categorias/nome/Qualquer", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
		
	@Test
	@DisplayName("Buscar categorias por descrição")
	public void deveBuscarCategoriasPorDescricao() {
		categoriaRepository.save(new Categoria(0L, "Categoria de Testes T5", "Categoria criada para testes"));
		categoriaRepository.save(new Categoria(0L, "Categoria Qualquer T6", "Categoria qualquer"));
		
		ResponseEntity<String> resposta = testRestTemplate
				.exchange("/categorias/descricao/para", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Deletar categoria por ID")
	public void deveApagarCategoriaPorId() {
		Categoria categoria = categoriaRepository.save(new Categoria(0L, "Categoria de Testes T7", "Categoria criada para testes"));
		
		ResponseEntity<String> resposta = testRestTemplate
				.exchange("/categorias/" + categoria.getId(), HttpMethod.DELETE, null, String.class);
		
		assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
	}
	
}
