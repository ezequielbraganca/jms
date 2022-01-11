package br.com.ezequiel.jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class TesteConsumidorFilaTransacional {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		
		InitialContext context = new InitialContext();
		
		ConnectionFactory factory =(ConnectionFactory) context.lookup("ConnectionFactory"); 
		
		Connection connection = factory.createConnection();
		connection.start();
		
		Session session = connection.createSession(true, Session.SESSION_TRANSACTED); //CLIENT_ACKNOWLEDGE = define que  o consumidor é responsavel por confirmar o recebimento
		
		Destination fila = (Destination) context.lookup("financeiro");
		MessageConsumer consumer = session.createConsumer(fila);
		
		//Message message = consumer.receive(); // recebe apenas uma mensagem
		consumer.setMessageListener( new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				
				TextMessage textMessage = (TextMessage) message;
				
				try {
					System.out.println("recebendo Mensagem:"+ textMessage.getText());
				    session.commit();
				} catch (JMSException e) {
					try {
						session.rollback();
					} catch (JMSException e1) {
						e1.printStackTrace();
					}
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