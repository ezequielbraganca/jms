package br.com.ezequiel.jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;

public class TesteConsumidorTopicoComercial {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		
		InitialContext context = new InitialContext();
		
		ConnectionFactory factory =(ConnectionFactory) context.lookup("ConnectionFactory"); 
		
		Connection connection = factory.createConnection();
		connection.setClientID("comercial"); //necessario setar ID para a identificacao do consumidor, 
		//caso não informado a entrega do tópico só é feita para consumidores online no momento do envio
		
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Topic topico = (Topic) context.lookup("loja");
		
		//criacao de um consumidor nao identificado
		//MessageConsumer consumer = session.createConsumer(topico);
		
		//criacoa de um consumidor identificado
		MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura");
		
		//Message message = consumer.receive(); // recebe apenas uma mensagem
		consumer.setMessageListener( new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				
				TextMessage textMessage = (TextMessage) message;
				
				try {
					System.out.println("recebendo Mensagem:"+ textMessage.getText());
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