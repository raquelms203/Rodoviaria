package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
public class teste extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					teste frame = new teste();
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
	public teste() {
		
		 
		    @SuppressWarnings("resource")
			Scanner ler = new Scanner(System.in);
		    int i, n;
		 
		    System.out.printf("Informe o número para a tabuada:\n");
		    n = ler.nextInt();
		 
		    FileWriter arq = null;
			try {
				arq = new FileWriter("C:\\Users\\raque\\Desktop");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    PrintWriter gravarArq = new PrintWriter(arq);
		 
		    gravarArq.printf("+--Resultado--+%n");
		    for (i=1; i<=10; i++) {
		      gravarArq.printf("| %2d X %d = %2d |%n", i, n, (i*n));
		    }
		    gravarArq.printf("+-------------+%n");
		 
		    try {
				arq.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		    System.out.printf("\nTabuada do %d foi gravada com sucesso em \"d:\\tabuada.txt\".\n", n);
		  
		 
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}
