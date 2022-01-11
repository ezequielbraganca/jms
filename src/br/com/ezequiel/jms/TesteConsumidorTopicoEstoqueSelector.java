package br.com.ezequiel.jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;

import br.com.ezequiel.jms.modelo.Pedido;

public class TesteConsumidorTopicoEstoqueSelector {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		
		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","br.com.ezequiel.jms.modelo");
		InitialContext context = new InitialContext();
		
		ConnectionFactory factory =(ConnectionFactory) context.lookup("ConnectionFactory"); 
		
		Connection connection = factory.createConnection();
		connection.setClientID("estoque"); //necessario setar ID para a identificacao do consumidor, 
		//caso não informado a entrega do tópico só é feita para consumidores online no momento do envio
		
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Topic topico = (Topic) context.lookup("loja");
		
		//criacao de um consumidor nao identificado
		//MessageConsumer consumer = session.createConsumer(topico);
		
		//criacoa de um consumidor identificado
		MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura", "ebook is null OR ebook=false", false);
		
		//Message message = consumer.receive(); // recebe apenas uma mensagem
		consumer.setMessageListener( new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				
				ObjectMessage objectMessage = (ObjectMessage) message;
				
				try {
					Pedido pedido = (Pedido) objectMessage.getObject();
					System.out.println("recebendo Mensagem:"+ pedido.toString());
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		
		
		new Scanner(System.in).nextLine();
		
		session.close();
		connection.close();
		context.close();

	}

}