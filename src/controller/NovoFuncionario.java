package controller;


import java.awt.EventQueue;

import javax.swing.border.EmptyBorder;

import model.SqliteConnection;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import javax.swing.*;

import java.awt.SystemColor;


/**
 * @author raquelms203
 *
 */
@SuppressWarnings("serial")
public class NovoFuncionario extends JFrame {

	private JPanel contentPane;
	private JTextField txtNome;
	private JTextField txtUser;
	private JTextField txtCargo;
	private JPasswordField txtSenha;
	private Connection connec = null;
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |  javax.swing.UnsupportedLookAndFeelException ex) {
           System.err.println(ex);        	
        }
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NovoFuncionario frame = new NovoFuncionario();
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
	 * Função para regitrar um novo funcionário no banco de dados. Caso há algum campo vazio, exibe um pop up com um alerta.
	 * 
	 */
	public void registrar() {
		String txtpass = new String(txtSenha.getPassword());
		
		if (txtNome.getText() == "" || txtCargo.getText() == "" || txtUser.getText() == "" || txtpass == "") {
			JOptionPane.showMessageDialog(null, "Campo(s) vazio!");
			return;
		}
		
		try {
			connec = SqliteConnection.dbBilheteria();
			String query = "INSERT INTO funcionarios (nome, user, senha, cargo) VALUES (?, ?, ?, ?) ";
			PreparedStatement ps = connec.prepareStatement(query);
			ps.setString(1, txtNome.getText().toUpperCase());
			ps.setString(2, txtUser.getText());
			String passText = new String (txtSenha.getPassword());
			ps.setString(3, passText);
			ps.setString(4, txtCargo.getText());
			ps.execute();
			ps.close();
			connec.close();
			JOptionPane.showMessageDialog(null, "Funcionário registrado com sucesso!");
			this.setVisible(false);

			} catch (SQLException e2) {
				JOptionPane.showMessageDialog(null, "Campo(s) vazio!");
			}	
	}

	/**
	 * Create the frame.
	 */
	public NovoFuncionario() {
		
		setBackground(new Color(144, 238, 144));
		setBounds(100, 100, 406, 478);
		contentPane = new JPanel();
		
		contentPane.setBackground(SystemColor.control);
		contentPane.setForeground(new Color(240, 240, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblRegistrarNovoFuncionrio = new JLabel("Dados Funcion\u00E1rio\r\n");
		lblRegistrarNovoFuncionrio.setBounds(103, 11, 201, 26);
		lblRegistrarNovoFuncionrio.setFont(new Font("Tahoma", Font.PLAIN, 20));
		contentPane.add(lblRegistrarNovoFuncionrio);
		
		JLabel lblNome = new JLabel("Nome Completo");
		lblNome.setBounds(66, 62, 103, 14);
		contentPane.add(lblNome);
		
		txtNome = new JTextField();
		txtNome.setBounds(66, 82, 256, 26);
		contentPane.add(txtNome);
		txtNome.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("User");
		lblNewLabel.setBounds(66, 124, 46, 14);
		contentPane.add(lblNewLabel);
		
		txtUser = new JTextField();
		txtUser.setBounds(66, 149, 256, 26);
		contentPane.add(txtUser);
		txtUser.setColumns(10);
		
		JLabel lblCargo = new JLabel("Cargo");
		lblCargo.setBounds(66, 255, 46, 14);
		contentPane.add(lblCargo);
		
		txtCargo = new JTextField();
		txtCargo.setBounds(66, 280, 256, 26);
		contentPane.add(txtCargo);
		txtCargo.setColumns(10);
		
		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setBounds(66, 190, 46, 14);
		contentPane.add(lblSenha);
		
		txtSenha = new JPasswordField();
		txtSenha.setToolTipText("");
		txtSenha.setBounds(66, 215, 256, 26);
		contentPane.add(txtSenha);
		
		btnRegistrar = new JButton("Registrar\r\n");		
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registrar();											
			}
		});
		
		btnRegistrar.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnRegistrar.setBounds(147, 334, 103, 48);
		contentPane.add(btnRegistrar);
		this.getRootPane().setDefaultButton(btnRegistrar);
		btnRegistrar.requestFocus();	
		closeFrame();
	}
	
	public void closeFrame() {
		this.setVisible(false);
	}
}
