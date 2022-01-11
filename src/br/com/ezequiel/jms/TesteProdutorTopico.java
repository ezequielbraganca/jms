package br.com.ezequiel.jms;

import java.io.StringWriter;
import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.xml.bind.JAXB;

import br.com.ezequiel.jms.modelo.Pedido;
import br.com.ezequiel.jms.modelo.PedidoFactory;

public class TesteProdutorTopico {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		
		InitialContext context = new InitialContext();
		
		ConnectionFactory factory =(ConnectionFactory) context.lookup("ConnectionFactory"); 
		
		Connection connection = factory.createConnection();
		
		
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Destination topico = (Destination) context.lookup("loja");
		
		MessageProducer producer = session.createProducer(topico);
		
		Pedido pedido = new PedidoFactory().geraPedidoComValores();
		
		StringWriter writes = new StringWriter();
		JAXB.marshal(pedido, writes);
		
		String pedidoXml = writes.toString();
		
//		Message message = session.createTextMessage(pedidoXml);
//		message.setBooleanProperty("ebook", false);
//		producer.send(message);
//		
//		message = session.createTextMessage(pedidoXml);
//		message.setBooleanProperty("ebook", true);
//		producer.send(message);
		
		Message messagePedido = session.createObjectMessage(pedido);
		producer.send(messagePedido);
		
		
		new Scanner(System.in).nextLine();
		
		session.close();
		connection.close();
		context.close();

	}

}