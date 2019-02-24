package model;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;



/**
 * Classe para alinhar o t�tulo e o texto de todas as c�lulas de uma c�lula
 * @author raquelms203
 * 
 */
@SuppressWarnings("serial")
public class CellRenderer extends DefaultTableCellRenderer {
	
	DefaultTableCellRenderer renderer;
	
	public CellRenderer(JTable table) 
	{
		renderer = (DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer();
	    renderer.setHorizontalAlignment(JLabel.CENTER);
	    this.setHorizontalAlignment(CENTER);	
	    
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
	
	
	
	
}

//Para chamar em um JFrame
//
//tabelaSenha.setDefaultRenderer(Object.class, new CellRenderer(tabela));
