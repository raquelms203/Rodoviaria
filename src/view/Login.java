package view;

import java.awt.EventQueue;
import java.awt.Image;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

import java.sql.*;
import javax.swing.*;

import model.SqliteConnection;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author raquelms203
 *
 */
public class Login {

	Connection connec = null;

	private JFrame frame;
	private JTextField txtLogin;
	private JPasswordField txtSenha;
	private JButton btnEntrar;

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		/// BLOCO TRY/CATCH PARA ESCOLHA DO TEMA, ALÉM DO 'WINDOWS', TEM 'METAL' E
		/// 'NIMBUS', TODOS NESSE PROJETO SÃO WINDOWS.
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| javax.swing.UnsupportedLookAndFeelException ex) {
			System.err.println(ex);
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Functions.
	 */

	/**
	 * Função para conferir se o valor digitado está registrado no sistema. E
	 * inicializa conforme o cargo do usuário.
	 */
	public void validar() {

		try {
			Connection connec = SqliteConnection.dbBilheteria(); /// CONECTA COM O BANCO DE DADOS
			String query = "SELECT * FROM funcionarios WHERE user=? AND senha=? ";
			PreparedStatement prep = connec.prepareStatement(query); /// 'PREPARA' A QUERY
			prep.setString(1, txtLogin.getText()); /// INSERE O VALOR QUE O USUÁRIO DIGITOU NO PRIMEIRO "?"
			String passText = new String(txtSenha.getPassword()); /// CONVERTE A 'MÁSCARA' DA SENHA EM STRING
			prep.setString(2, passText); /// INSERE O VALOR QUE O USUÁRIO DIGITOU NO SEGUNDO "?"

			ResultSet rs = prep.executeQuery(); /// EXECUTA A QUERY E GUARDA OS RESULTADOS EXISTENTES

			if (rs.next()) { /// PARA A VALIDAÇÃO, DEVE EXISTIR APENAS UM RESULTADO NO RESULTSET
				JOptionPane.showMessageDialog(null, "Bem vindo ao sistema!");

				if (rs.getString("cargo").equalsIgnoreCase("adm")) {
					ViewADM va = new ViewADM(txtLogin.getText()); /// CONSTRUTOR DE OUTRA FRAME
					va.setVisible(true); /// EXIBE A FRAME

				} else if (rs.getString("cargo").equalsIgnoreCase("caixa")) {
					ViewBilheteria vb = new ViewBilheteria(txtLogin.getText());
					vb.setVisible(true);
				}

				frame.setVisible(false);
				rs.close();
				prep.close();
				connec.close();

			} else {/// NÃO HOUVE RESULTADOS CORRESPONDENTES
				JOptionPane.showMessageDialog(null, "Usuário ou senha incorretos!\nTente novamente", "Erro!",
				JOptionPane.WARNING_MESSAGE);
			}
			txtLogin.setText("");
			txtSenha.setText("");

		} catch (SQLException e) { /// CASO NAO LIGASSE NO BANCO DE DADOS
			JOptionPane.showMessageDialog(null, e);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(152, 251, 152));
		frame.getContentPane().setForeground(new Color(255, 255, 255));
		frame.setBounds(100, 100, 559, 351);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); /// QUANDO FECHA O FRAME ENCERRA O PROGRAMA
		frame.getContentPane().setLayout(null);
		txtLogin = new JTextField();
		txtLogin.setBackground(new Color(255, 255, 255));
		txtLogin.setBounds(250, 84, 228, 37);
		frame.getContentPane().add(txtLogin);
		txtLogin.setColumns(10);

		JLabel lblLogin = new JLabel("Login");
		lblLogin.setBounds(250, 59, 46, 14);
		frame.getContentPane().add(lblLogin);

		JLabel lblNewLabel = new JLabel("Senha");
		lblNewLabel.setBounds(250, 125, 46, 14);
		frame.getContentPane().add(lblNewLabel);

		txtSenha = new JPasswordField();
		txtSenha.addKeyListener(new KeyAdapter() { /// LISTENER QUANDO 'ENTER' NA SENHA É CLICADO
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					validar();
			}
		});
		txtSenha.setBounds(250, 150, 228, 37);
		frame.getContentPane().add(txtSenha);

		btnEntrar = new JButton("Entrar");
		btnEntrar.setForeground(new Color(0, 0, 0));
		btnEntrar.setBackground(new Color(128, 0, 0));

		btnEntrar.addActionListener(new ActionListener() { /// LISTENER QUANDO 'ENTER' NO BOTÃO ENTRAR É CLICADO
			public void actionPerformed(ActionEvent arg0) {
				validar();
			}
		});

		btnEntrar.setBounds(295, 225, 94, 37);
		frame.getContentPane().add(btnEntrar);

		JLabel label = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/lock.png")).getImage(); /// CONVERTE IMAGE EM IMAGEICON
		label.setIcon(new ImageIcon(img)); /// USA A IMAGEICON NA LABEL
		label.setBounds(66, 66, 131, 153);
		frame.getContentPane().add(label);
	}
}
