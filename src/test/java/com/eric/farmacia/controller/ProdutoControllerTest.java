package com.eric.farmacia.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import com.eric.farmacia.model.Produto;
import com.eric.farmacia.repository.CategoriaRepository;
import com.eric.farmacia.repository.ProdutoRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProdutoControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	private Categoria c1, c2, c3;
	
	@BeforeAll
	void start(){
		produtoRepository.deleteAll();
		
		c1 = categoriaRepository.save(new Categoria(0L, "Categoria de testes T1", "Testar o programa"));
		c2 = categoriaRepository.save(new Categoria(0L, "Categoria de testes T2", "Testar o programa"));
		c3 = categoriaRepository.save(new Categoria(0L, "Categoria Qualquer T3", "Categoria"));
		
		produtoRepository.save(new Produto(0L, "Produto de Testes 1", "Produto criado para testes", 30.99, 10, c1));
		produtoRepository.save(new Produto(0L, "Produto de Testes 2", "Produto criado para testes", 35.99, 12, c3));
		produtoRepository.save(new Produto(0L, "Produto de Testes 3", "Produto criado para testes", 40.99, 15, c2));
		produtoRepository.save(new Produto(0L, "Produto 1", "Produto", 50.99, 13, c2));
		produtoRepository.save(new Produto(0L, "Produto 2", "Produto", 60.99, 16, c3));
		produtoRepository.save(new Produto(0L, "Produto 3", "Produto", 70.99, 18, c1));
	}
	
	@Test
	@DisplayName("Criar um produto")
	public void deveCriarUmProduto() {
		HttpEntity<Produto> corpoRequisicao = new HttpEntity<Produto>(new Produto(0L, "Produto de Testes T4", "Produto criado para testes", 40.99, 15, c2));
		
		ResponseEntity<Produto> corpoResposta = testRestTemplate.exchange("/produtos", HttpMethod.POST, corpoRequisicao, Produto.class);
		
		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Atualização de um produto")
	public void deveAtualizarProduto() {
		Produto produtoExistente = produtoRepository.save(new Produto(0L, "Produto de Testes T5", "Criado para testes", 35.99, 12, c3));
		
		Produto produtoNovo = new Produto(produtoExistente.getId(), "Produto de Testes T6", "Criado para testes", 70.99, 18, c1);
		
		HttpEntity<Produto> corpoRequisicao = new HttpEntity<Produto>(produtoNovo);
		
		ResponseEntity<Produto> corpoResposta = testRestTemplate
				.exchange("/produtos", HttpMethod.PUT, corpoRequisicao, Produto.class);
		
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Listar todos os produtos")
	public void deveListarTodasOsProdutos() {
		produtoRepository.save(new Produto(0L, "Produto de Testes T7", "Produto criado para testes", 30.99, 10, c1));
		produtoRepository.save(new Produto(0L, "Produto de Testes T8", "Produto criado para testes", 35.99, 12, c3));
		
		ResponseEntity<String> resposta = testRestTemplate
				.exchange("/produtos", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Buscar produtos por nome")
	public void deveBuscarProdutosPorNome() {
		produtoRepository.save(new Produto(0L, "Produto de Testes T9", "Produto criado para testes", 10.99, 32, c2));
		produtoRepository.save(new Produto(0L, "Produto T10", "Produto", 16.99, 24, c2));
		
		ResponseEntity<String> resposta = testRestTemplate
				.exchange("/produtos/nome/Qualquer", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Buscar produtos por descricao")
	public void deveBuscarProdutosPorDescricao() {
		produtoRepository.save(new Produto(0L, "Produto de Testes T11", "Produto criado para testes", 10.99, 32, c1));
		produtoRepository.save(new Produto(0L, "Produto T12", "Produto", 16.99, 24, c2));
		
		ResponseEntity<String> resposta = testRestTemplate
				.exchange("/produtos/descricao/criado", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Apagar produto por ID")
	public void deveApagarProdutoPorId() {
		Produto produto = produtoRepository.save(new Produto(0L, "Produto de Testes T13", "Produto criado para testes", 14.99, 22, c3));
		
		ResponseEntity<String> resposta = testRestTemplate
				.exchange("/produtos/" + produto.getId(), HttpMethod.DELETE, null, String.class);
		
		assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Buscar produtos por preço")
	public void deveBuscarProdutosPorPrecoMax() {
		produtoRepository.save(new Produto(0L, "Produto de Testes T14", "Produto criado para testes", 10.99, 32, c3));
		produtoRepository.save(new Produto(0L, "Produto T15", "Produto", 16.99, 24, c2));
		
		ResponseEntity<String> resposta = testRestTemplate
				.exchange("/produtos/preco/11", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
	}