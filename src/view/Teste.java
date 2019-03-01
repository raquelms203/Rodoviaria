package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JButton;

public class Teste extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |  javax.swing.UnsupportedLookAndFeelException ex) {
           System.err.println(ex);        	
        }
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Teste frame = new Teste();
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
	public Teste() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1115, 508);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.YELLOW);
		menuBar.setBounds(938, 5, 24, 24);
		contentPane.add(menuBar);
		
		Image imgoff = new ImageIcon(this.getClass().getResource("/off.png")).getImage();
		
		JMenu menu = new JMenu("\"\"");
		menu.setOpaque(true);
		menu.setBackground(new Color(220, 220, 220));
		menu.setIcon(new ImageIcon(imgoff));
		menuBar.add(menu);
		
		JMenuItem menuSair = new JMenuItem("Sair");
		menuSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(1);
			}
		});
		
		JMenuItem menuRenov = new JMenuItem("Renovar Passagens");
		menuRenov.setBackground(Color.RED);
		menu.add(menuRenov);
		
		JMenuItem menuTrocar = new JMenuItem("Trocar Usu\u00E1rio");
		menu.add(menuTrocar);
		menu.add(menuSair);
	
	}
}
