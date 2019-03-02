package controller;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.io.*;
public class NotaFiscal extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NotaFiscal frame = new NotaFiscal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@SuppressWarnings("resource")
	public void criar_txt() {
		File diretorio = new File("C:\\Users\\raque\\Documents\\Rodoviaria\\notas fiscais");
		boolean status = diretorio.mkdir();
		System.out.println(status);
		
		File arq = new File(diretorio, "arq_01.txt");
		try {
			boolean status2 = arq.createNewFile();
			System.out.println(status2);
		} catch (IOException e) {
			// TODO: handle exception
		}
		
//		File files = new File("C:\\Users\\raque\\Documents\\Rodoviaria");
//		for (File file : files.listFiles())
//			System.out.println(file);
		
		try {
			// SOBREESCREVER: TRUE PARA ESCREVER SEM APAGAR O CONTEÚDO ANTIGO, FALSE PARA APAGAR.
			FileWriter fileWriter = new FileWriter(arq, true);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println("Teste1");
			printWriter.println("Teste2");
			
			printWriter.flush();
			printWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	/**
	 * Create the frame.
	 */
	public NotaFiscal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 406, 478);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		criar_txt();
	}

}
