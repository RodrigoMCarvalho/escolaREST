package com.escolaRest.javaClient;

public class JavaSpringClientTest {

	public static void main(String[] args) {

//		Estudante estudantePost = new Estudante();
//		estudantePost.setNome("Pelé");
		
		JavaClientDAO dao = new JavaClientDAO();
		
		System.out.println(dao.findById(23L));

	}
}