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
					NotaFiscal frame = new NotaFiscal("", -1, "", "", "", "", "", "", "", "", "", "", "");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	/**
	 * Create the frame.
	 */
	public NotaFiscal(String data, int id, String cidade_partida, String cidade_destino, String horario_partida,
			String horario_destino, String cliente, String poltrona, String caixa, String total, String troco,
			String txt_mes_ano, String txt_data) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 406, 478);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// CRIA A PASTA 
		File diretorio = new File("C:\\Users\\raque\\Documents\\- Períodos Anteriores\\6 Período\\Eclipse\\Rodoviaria\\notas fiscais\\"+txt_mes_ano);
		boolean status = diretorio.mkdir();
		System.out.println(status);
		
		// CRIA O TXT
		File arq = new File(diretorio, cliente+" "+txt_data+".txt");
		try {
			boolean status2 = arq.createNewFile();
			System.out.println(status2);
		} catch (IOException e) {
			// TODO: handle exception
		}
		
		// ESCREVE NO TXT
		try {
			// SOBREESCREVER: TRUE PARA ESCREVER SEM APAGAR O CONTEÚDO ANTIGO, FALSE PARA APAGAR.
			FileWriter fileWriter = new FileWriter(arq, false);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println("RODOVIARIA \r\n" + 
					"JOAO MONLEVADE - MG\r\n" + 
					"============================\r\n" + 
					"DATA: "+data+"\r\n" +
					"ID: "+id+"\r\n" + 
					"\r\n" + 
					"============================\r\n" + 
					"PARTIDA: "+cidade_partida+"\r\n" + 
					"HORARIO: "+horario_partida+"\r\n" + 
					"DESTINO: "+cidade_destino+"\r\n" + 
					"HORARIO: "+horario_destino+"\r\n" + 
					"\r\n" + 
					"============================\r\n" + 
					"CAIXA: "+caixa+"\r\n" + 
					"CLIENTE: "+cliente+"\r\n" + 
					"POLTRONA: "+poltrona+"\r\n" + 
					"\r\n" + 
					"TOTAL: "+total+"\r\n" + 
					"TROCO: "+troco+" \r\n" + 
					"\r\n" + 
					"============================\r\n" + 
					"\r\n" + 
					"OBRIGADO, VOLTE SEMPRE!\r\n" +
					"============================");
			
			printWriter.flush();
			printWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}

//File files = new File("C:\\Users\\raque\\Documents\\Rodoviaria");
//for (File file : files.listFiles())
//	System.out.println(file);
