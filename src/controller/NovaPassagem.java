package controller;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.border.EmptyBorder;

import model.SqliteConnection;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowEvent;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;

/**
 * @author raquelms203
 *
 */
@SuppressWarnings("serial")
public class NovaPassagem extends JFrame {

	private JPanel contentPane;
	private JTextField txtDestino;
	private JTextField txtPartida;
	private JTextField txtChegada;
	private JTextField txtPreco;
	private JButton btnRegistrar;

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
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| javax.swing.UnsupportedLookAndFeelException ex) {
			System.err.println(ex);
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NovaPassagem frame = new NovaPassagem();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Functions.
	 */

	/**
	 * @param cidade Recebe uma cidade e verifica no banco de dados se ela já
	 *               existe.
	 * @return valor boolean
	 */
	public boolean cidade_registrada(String cidade) {

		boolean flag = false;
		try {
			Connection connec = SqliteConnection.dbBilheteria();
			String query = "SELECT cidade FROM cidades WHERE cidade=?";
			PreparedStatement prep = connec.prepareStatement(query);
			prep.setString(1, cidade);
			ResultSet rs = prep.executeQuery();

			if (rs.next())
				flag = true;

			prep.close();
			rs.close();
			connec.close();
			return flag;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		}
		return false;
	}

	/**
	 * Função para validar os dados digitados pelo usuário no banco de dados. Se
	 * houver campos vazios envia um pop up com aviso e limpa os campos de texto. Se
	 * a cidade da passagem ainda não existir na tabela 'cidades', cadastra lá
	 * também.
	 * 
	 */
	public void registrar() {

		if (txtChegada.getText().compareTo("") == 0 || txtDestino.getText().compareTo("") == 0
				|| txtPreco.getText().compareTo("") == 0 || txtPartida.getText().compareTo("") == 0) {

			JOptionPane.showMessageDialog(null, "Campo(s) vazio!");
			return;
		}

		try {
			Connection connec = SqliteConnection.dbBilheteria();
			if (!cidade_registrada(txtDestino.getText())) {
				String query0 = "INSERT INTO cidades (cidade) VALUES ('" + txtDestino.getText() + "') ";
				PreparedStatement prep0 = connec.prepareStatement(query0);
				prep0.execute();
				prep0.close();
			}

			String query = "INSERT INTO passagens (destino_cidade, partida_horario, destino_horario, preco) VALUES (?, ?, ?, ?);"
					+ "		INSERT INTO poltronas (A1, A2, A3, A4, B1, B2, )";

			PreparedStatement prep = connec.prepareStatement(query);
			prep.setString(1, txtDestino.getText().toUpperCase());
			prep.setString(2, txtPartida.getText());
			prep.setString(3, txtChegada.getText());
			prep.setString(4, txtPreco.getText());
			prep.execute();
			prep.close();
			connec.close();
			JOptionPane.showMessageDialog(null, "Nova passagem registrada com sucesso!");
			this.dispose();

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		}

	}

	/**
	 * Create the frame.
	 */
	public NovaPassagem() {
		addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent arg0) {
				btnRegistrar.requestFocus();
			}

			public void windowLostFocus(WindowEvent arg0) {
			}
		});
		setBounds(100, 100, 406, 478);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Nova Passagem");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(102, 11, 244, 52);
		contentPane.add(lblNewLabel);

		txtDestino = new JTextField();
		txtDestino.setBounds(55, 86, 268, 33);
		contentPane.add(txtDestino);
		txtDestino.setColumns(10);

		JLabel lblDestino = new JLabel("Destino");
		lblDestino.setBounds(55, 69, 46, 14);
		contentPane.add(lblDestino);

		txtPartida = new JTextField();
		txtPartida.setBounds(55, 156, 104, 33);
		contentPane.add(txtPartida);
		txtPartida.setColumns(10);

		txtChegada = new JTextField();
		txtChegada.setBounds(219, 156, 104, 33);
		contentPane.add(txtChegada);
		txtChegada.setColumns(10);

		JLabel lblPartida = new JLabel("Partida");
		lblPartida.setBounds(55, 140, 46, 14);
		contentPane.add(lblPartida);

		JLabel lblChegada = new JLabel("Chegada");
		lblChegada.setBounds(219, 140, 46, 14);
		contentPane.add(lblChegada);

		txtPreco = new JTextField();
		txtPreco.setBounds(55, 220, 104, 33);
		contentPane.add(txtPreco);
		txtPreco.setColumns(10);

		JLabel lblPreo = new JLabel("Pre\u00E7o");
		lblPreo.setBounds(55, 205, 46, 14);
		contentPane.add(lblPreo);

		btnRegistrar = new JButton("Registrar");
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				registrar();
			}
		});
		btnRegistrar.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnRegistrar.setBounds(142, 332, 103, 48);
		contentPane.add(btnRegistrar);
	}
}
