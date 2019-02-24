package controller;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import model.SqliteConnection;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;



/**
 * @author raquelms203
 *
 */
@SuppressWarnings("serial")
public class AlterarFuncionario extends JFrame {
	private JTextField txtPesquisa;
	private JTextField txtNome;
	private JTextField txtUser;
	private JTextField txtCargo;
	private Connection connec =  null;
	private JTextField txtSenha;
	private JTextField txtID;
	@SuppressWarnings("rawtypes")
	private JList listaInfo;
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
					AlterarFuncionario frame = new AlterarFuncionario();
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
		
		/** Função que verifica se a informação digitada existe no banco de dados. Se não, o campo fica vermelho (return false) e se sim
		 * o campo fica em verde e exibe as todas as informações do usuário.  
		 * @return valor boolean
		 * 		 
		 */
		public boolean pesquisa () {
		connec = SqliteConnection.dbBilheteria();
		String query="";
				
		try {
			query="SELECT * FROM funcionarios WHERE "+listaInfo.getSelectedValue().toString()+"=? ";
			PreparedStatement prep = connec.prepareStatement(query);
			prep.setString(1, txtPesquisa.getText());
			ResultSet rs = prep.executeQuery();
			if (!rs.next()) {
				txtPesquisa.setBackground(Color.red);
				return false;
			}				
				txtPesquisa.setBackground(Color.green);
				txtNome.setText(rs.getString("nome"));
				txtUser.setText(rs.getString("user"));
				txtSenha.setText(rs.getString("senha"));
				txtCargo.setText(rs.getString("cargo"));
				txtID.setText(rs.getString("id"));
				
				rs.close();
				prep.close();
				connec.close();						
				return true;
			
		} catch (SQLException e) {			
			JOptionPane.showMessageDialog(null, e);
			return false;
		} 
			
	}
	
	/**
	 * Função que atualiza o banco de dados com as informações digitadas nos campos de texto.
	 */
	public void atualiza () {
		String query="";
		PreparedStatement prep=null;
		Connection connec = SqliteConnection.dbBilheteria();
		
		query="UPDATE funcionarios SET nome='"+txtNome.getText()+"', user='"+txtUser.getText()+"', senha='"+txtSenha.getText()+
			"', cargo='"+txtCargo.getText()+"' WHERE "+listaInfo.getSelectedValue().toString()+"='"+txtPesquisa.getText()+"' ";
		try {
			prep = connec.prepareStatement(query);
			prep.execute();			
			JOptionPane.showMessageDialog(null, "Usuário atualizado com sucesso!");
			prep.close();
			connec.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);		
		}
		this.setVisible(false);
	}
	
	
	/**
	 * Função para apagar uma linha no banco de dados e exibe uma caixa de diálogo pop up de confirmação para a ação. 
	 */
	public void apaga () {
		String query="";
		PreparedStatement prep=null;
		Connection connec = SqliteConnection.dbBilheteria();
		
		query="DELETE FROM funcionarios WHERE "+listaInfo.getSelectedValue().toString()+"='"+txtPesquisa.getText()+"' ";
		
		try {
			prep = connec.prepareStatement(query);
			int op = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja apagar?", "Aviso", 2, JOptionPane.QUESTION_MESSAGE);
			
			if (op==2) {
				return;
			}
			prep.execute();
			prep.close();
			connec.close();
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);		
		}
		this.setVisible(false);
	}
	

	/**
	 * Create the frame.
	 */ 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AlterarFuncionario() {
		getContentPane().setBackground(SystemColor.control);
		setBackground(new Color(135, 206, 235));
		setBounds(100, 100, 406, 478);
		getContentPane().setLayout(null);
		
		// CONVERTE IMAGE EM IMAGEICON, PARA USAR EM ICONES DE JBUTTON:
		Image imgsearch = new ImageIcon(this.getClass().getResource("/search.png")).getImage(); 
		
		JList list = new JList();
		list.setBounds(25, 32, 1, 1);
		getContentPane().add(list);
		
		JPanel painelPesquisar = new JPanel();
		painelPesquisar.setBackground(SystemColor.control);
		painelPesquisar.setBounds(55, 72, 297, 52);
		getContentPane().add(painelPesquisar);
		painelPesquisar.setLayout(null);
		
		listaInfo = new JList();
		listaInfo.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(105, 105, 105)));
		listaInfo.setBounds(0, 0, 54, 52);
		painelPesquisar.add(listaInfo);
		listaInfo.setVisibleRowCount(3);
		listaInfo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaInfo.setModel(new AbstractListModel() {
			String[] values = new String[] {"ID", "User", "Nome"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		listaInfo.setSelectedIndex(1);
		
		JPanel painelModificar = new JPanel();
		painelModificar.setBackground(SystemColor.control);
		painelModificar.setBounds(55, 147, 266, 182);
		getContentPane().add(painelModificar);
		painelModificar.setLayout(null);
		painelModificar.setVisible(false);
		
		txtPesquisa = new JTextField();
		txtPesquisa.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER) {
					if(pesquisa()) {
						painelModificar.setVisible(true);
					}
				}
			}
		});
		txtPesquisa.setBounds(76, 18, 190, 20);
		painelPesquisar.add(txtPesquisa);
		txtPesquisa.setColumns(10);
		
		JButton btnPesquisa = new JButton("");
		btnPesquisa.setBounds(277, 18, 20, 20);
		painelPesquisar.add(btnPesquisa);
		/// USO DO IMAGEICON DE PESQUISA
		btnPesquisa.setIcon(new ImageIcon(imgsearch));
		
		btnPesquisa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(pesquisa()) 
					painelModificar.setVisible(true);
			}
		});
						
		txtNome = new JTextField();
		txtNome.setBounds(0, 15, 266, 20);
		painelModificar.add(txtNome);
		txtNome.setColumns(10);
		
		txtUser = new JTextField();
		txtUser.setBounds(0, 62, 266, 20);
		painelModificar.add(txtUser);
		txtUser.setColumns(10);
		
		txtCargo = new JTextField();
		txtCargo.setBounds(0, 162, 157, 20);
		painelModificar.add(txtCargo);
		txtCargo.setColumns(10);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(0, 0, 46, 14);
		painelModificar.add(lblNome);
		
		JLabel lblUser = new JLabel("User");
		lblUser.setBounds(0, 46, 46, 14);
		painelModificar.add(lblUser);
		
		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setBounds(0, 93, 46, 14);
		painelModificar.add(lblSenha);
		
		JLabel lblCargo = new JLabel("Cargo");
		lblCargo.setBounds(0, 144, 46, 14);
		painelModificar.add(lblCargo);
		
		txtSenha = new JTextField();
		txtSenha.setBounds(0, 113, 266, 20);
		painelModificar.add(txtSenha);
		txtSenha.setColumns(10);
		
		JLabel lblId = new JLabel("ID");
		lblId.setBounds(188, 144, 46, 14);
		painelModificar.add(lblId);
		
		txtID = new JTextField();
		txtID.setBounds(188, 162, 78, 20);
		painelModificar.add(txtID);
		txtID.setColumns(10);
		
		JButton btnAtualizar = new JButton("Atualizar");
		btnAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				atualiza();					
			}
		});
		btnAtualizar.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnAtualizar.setBounds(55, 354, 120, 39);
		getContentPane().add(btnAtualizar);
		
		JLabel lblSelecione = new JLabel("Alterar Funcion\u00E1rio\r\n");
		lblSelecione.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblSelecione.setBounds(104, 26, 217, 20);
		getContentPane().add(lblSelecione);
		
		JButton btnApagar = new JButton(" Apagar\r\n");
		btnApagar.setHorizontalAlignment(SwingConstants.LEFT);
		Image imgdelete = new ImageIcon(this.getClass().getResource("/delete.png")).getImage();
		btnApagar.setIcon(new ImageIcon(imgdelete)); 
		btnApagar.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnApagar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				apaga();
			}
		});
		btnApagar.setBounds(201, 354, 120, 39);
		getContentPane().add(btnApagar);
	}
}



